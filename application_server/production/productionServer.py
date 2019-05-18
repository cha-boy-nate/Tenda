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
	decoded = decoded.split("&")
	#Set the variable email password to the value given by the user (value is after = sign)
	password = decoded[0].split("=")
	password = "(\'" + password[1] + "\',)" #formatting sql needs
	email = decoded[1].split("=")
	email = email[1]
	#check to see if the password matches email's password in database.
	checker = verifyPassword(email, password, cur)
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
	user_id = decoded[1].split("=")
	response = decoded[2].split("=")
	event_id = event_id[1]
	user_id = user_id[1]
	response = response[1]
	#Commit values to database.
	commitInvitationResponse(event_id, user_id, response, cur, db)
	return jsonify(result={"status":200})

#Route to create a new event.
@application.route("/createEvent", methods=["POST"])
def createEvent():
	#Decodes request into variable. 
	decoded = unquote(request.get_data())
	commitEvent("1", "test name", "2019-04-24", "00:00:00", "00:00:00", "10.00", "this is a test eventDescription", cur, db)
	return jsonify(result={"status":200})

#Route to create a new account.
@application.route("/createAccount", methods=["POST"])
def createAccount():
	#Get the data from the request.
	response = str(request.get_data())
	response = response.replace("%40", "@")
	response = response.replace("key=", "")
	response = response[:-1]
	#Split the response data into variables.
	email, firstName, lastName, password = response.split("+", 4)
	sql = "insert into User(firstName, lastName, email, password) values('"+firstName+"','"+lastName+"','"+email+"','"+password+"');"
	cur.execute(sql)
	db.commit()
	return jsonify(result={"status":200})

@application.route("/createAttendenceRecord", methods=["POST"])
def createAttendenceRecord():
    #Get the data from the request.
    response = str(request.get_data())
    decoded = unquote(response)
    decoded = decoded.split("&")
    event_id = decoded[0].split('=')[1]
    user_id = decoded[1].split('=')[1]
    response = decoded[2].split('=')[1]
    time = decoded[3].split('=')[1]
    date = decoded[4].split('=')[1]
    print(str(event_id) + " " + str(user_id) + " " + str(response) + " " + str(time) + " " + str(date))
    return jsonify(result=200)

'''THIS IS THE END OF THE POST REQUEST ROUTES'''
#################################################
'''THIS IS THE START OF THE GET REQUEST ROUTES'''

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
        #print("Got a request for timeline")
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
    print("ID value given was: " + idVal)
    returnDictionary = []
    singlePerson = {}
    test = {}
    counter = 0
    finalSum = {}
    summary = {}
    summary['in_Attendence'] = '10'
    summary['planned_Attendence'] = '8'
    finalSum["summary_statisitics"] = summary
    returnDictionary.append(finalSum.copy())
    while(counter < 5):
        counter = counter + 1
        singlePerson['user_id'] = str(counter)
        singlePerson['duration'] = '1 hour'
        test['user'] = singlePerson
        returnDictionary.append(test.copy())
    print(returnDictionary)
    return jsonify(result=returnDictionary)

'''THIS IS THE END OF THE GET REQUEST ROUTES'''

#THIS NEEDS TO STAY AT THE BOTTOM OF THE FILE
#This basically tells flask to run if the envirnment is in development mode.
if __name__ == '__main__':
	application.run(debug=True)
