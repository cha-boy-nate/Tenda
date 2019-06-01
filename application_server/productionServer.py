from flask import Flask, request, jsonify
from urllib import unquote
import re
import json
import setup
from database_manipulators import *

#database_username, database_password = setup.getCredentials()

db = setup.loginToDatabase("flask", "password")
cur = db.cursor()

application = Flask(__name__)

#Route used for testing.
@application.route("/", methods=['GET', 'POST'])
def index():
	return jsonify(result={"status":200})

#################################################
# Routing is divided into Get and Post requests #
# First: POST requests                          #
# Second: GET requests                          #
#################################################

'''THIS IS THE START OF THE POST REQUESTS'''

#Route for user's email and password submission. Calls verify password.
@application.route("/login", methods=['POST'])
def login():
	#Set variable response to data sent over request.
	decoded = unquote(request.get_data())
	#print(str(request.get_data()))
        #Split to response into the two parts (email and password)
	
        print(decoded)
        decoded = decoded.split("&")
	#Set the variable email password to the value given by the user (value is after = sign)
	
        password = decoded[0].split("=")
	password = "(\'" + password[1] + "\',)" #formatting sql needs
	email = decoded[1].split("=")
        print(decoded[1])
        #print(password)
        #for i in email:
        #    print(str(i))
	#check to see if the password matches email's password in database.
	#checker = verifyPassword(email, password, cur)
	checker = 0
        if checker != 0:
		return jsonify(result={"status":200, 'data':str(checker)})
	else:
		return jsonify(result={"status": 405})

#Route for users to to join an event.
@application.route("/createResponse", methods=["POST"])
def createResponse():
	#Decodes request into variable.
	decoded = unquote(request.get_data())
	#Split request into parts.
	decoded = decoded.split("&")
	#Get all values from request.
	event_id = decoded[0].split("=")
	user_id = decoded[2].split("=")
	response = decoded[1].split("=")
	event_id = event_id[1]
	user_id = user_id[1]
	response = response[1]
	#Commit values to database.
	commitInvitationResponse(event_id, user_id, response, cur, db)
        return jsonify(result={"status"})



#Route to create a new event.
@application.route("/createEvent", methods=["POST"])
def createEvent():
	#Decodes request into variable. 
	decoded = unquote(request.get_data())
        print(decoded)
        decoded = decoded.split("&")
        
        radius = decoded[0].split("=")
        eventTitle = decoded[1].split("=")
        eventDescription = decoded[2].split("=")
        eventTime = decoded[3].split("=")
        latitude = decoded[4].split("=")
        userID = decoded[6].split("=")
        longitude = decoded[7].split("=")
        eventDate = decoded[8].split("=")
        
        duration = decoded[5].split("=")
        duration = duration[1]
        print("Duration: " + str(duration))

        radius = radius[1]
        eventTitle = eventTitle[1]
        eventDescription = eventDescription[1]
        eventTime = eventTime[1]
        latitude = latitude[1]
        userID = userID[1]
        longitude = longitude[1]
        eventDate = eventDate[1]      

        eventTitle = eventTitle.replace("+", " ")
        eventDescription = eventDescription.replace("+", " ")
        
        eventTime = eventTime + ":00"

        print("radius: " + radius)
        print("title: " + eventTitle)
        print("description: " + eventDescription)
        print("time: " + eventTime)
        print("date: " + eventDate)
        print("user id: " + userID)
        print("latitude: " + latitude)
        print("longitude: " + longitude)
        
	commitEvent(userID, eventTitle, eventDate, eventTime, duration, radius, eventDescription, latitude, longitude, cur, db)
	return jsonify(result={"status":200})

#Route to create a new account.
@application.route("/createAccount", methods=["POST"])
def createAccount():
	#Get the data from the request.
	response = str(request.get_data())
        decoded = unquote(response)
        decoded = decoded.split("&")
        password = decoded[0].split("=")[1]
        email = decoded[3].split("=")[1]
        first_name = decoded[2].split("=")[1]
        last_name = decoded[1].split("=")[1]
        print("password: " + password)
        print("first name: " + first_name)
        print("last name: " + last_name)
        print("email: " + email)
	sql = "insert into User(firstName, lastName, email, password) values('"+firstName+"','"+lastName+"','"+email+"','"+password+"');"
	cur.execute(sql)
	db.commit()
	return jsonify(result={"status":200})

@application.route("/createAttendenceRecord", methods=["POST"])
def createAttendenceRecord():
    #Get the data from the request.
    response = str(request.get_data())
    decoded = unquote(response)
    print(decoded)
    decoded = decoded.split("&")
    event_id = decoded[0].split('=')[1]
    user_id = decoded[1].split('=')[1]
    response = decoded[2].split('=')[1]
    commitAttendenceRecord(user_id, event_id, response, cur, db)
    return jsonify(result=200)


'''THIS IS THE END OF THE POST REQUEST ROUTES'''
#################################################
'''THIS IS THE START OF THE GET REQUEST ROUTES'''

@application.route("/", methods=['GET'])
def indexTest():
    print("Got Request")
    return 'test'


#Route returns event details matching id given.
@application.route("/event/<idVal>/", methods=['GET'])
def event(idVal):
	#Get event details for single event.
	event = getEventDetails(idVal, cur)
	if event == None:
		return jsonify(result={"status":"event not found"})
	else:
		return jsonify(result=event)

#Route for getting account information by userID. (only first/last name and email)
@application.route("/accountInformation/<idVal>/", methods=["GET"])
def accountInformation(idVal):
	return jsonify(result=str(getUserData(idVal, cur)))

#Route for getting userID by email.
@application.route("/getUserIDNum/<email>/", methods=["GET"])
def getUserIDNum(email):
	return jsonify(result={"user_id":str(getUserID(email, cur))})

#Returns a json of events that the user has said they are attending.
@application.route("/timeline/<idVal>/", methods=["GET"])
def timeline(idVal):
        print("Got a request for timeline")
	#Get list of event_ids that the specified user plans to attend.
	events = getAssociatedEvents(idVal, cur)
	#Variables used to format the response.
	eventDictionary = []
	singleEvent = {}
	for event in events:
		event_id = int(event) #cast events as ints
		#Get event details for event by ID.
		singleEvent["event"] = str(getEventDetails(event_id, cur))
		#Add the single event to the return value.
		eventDictionary.append(singleEvent.copy())
	return jsonify(result=eventDictionary)

#Returns a json of events that the specified user has created.
@application.route("/myEvents/<idVal>/", methods=["GET"])
def myEvents(idVal):
	#Get list of event_id's that the user has created.
	events = getManagerAssociatedEvents(idVal, cur)
	#Variables needed for return.
	eventDictionary = []
	singleEvent = {}
	for event in events:
		event_id = int(event)
		#Get event details for event by ID.
		singleEvent["event"] = str(getEventDetails(event_id, cur))
		#Add the single event to the return value.
		eventDictionary.append(singleEvent.copy())
	return jsonify(result=eventDictionary)

@application.route("/attendenceData/<idVal>/", methods=["GET"])
def attendenceData(idVal):
    users = getUsersForEvent(cur, idVal)
    value = []
    for user in users:
        userData = getUserAttendenceData(cur, idVal, user)
        if userData != 0:
            value.append(userData)
    return jsonify(result=value)
'''THIS IS THE END OF THE GET REQUEST ROUTES'''

#THIS NEEDS TO STAY AT THE BOTTOM OF THE FILE
#This basically tells flask to run if the envirnment is in development mode.
if __name__ == '__main__':
	application.run(debug=True)
