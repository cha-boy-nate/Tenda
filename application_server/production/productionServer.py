from flask import Flask, request, jsonify
from urllib import unquote
import re
import json
import setup

db = setup.getOS()
cur = db.cursor()

application = Flask(__name__)
#Description: function verifies that the email and password provided by user matches a registered account
#Parameters: email and password
#Output: if the email and password match the password from the designated email in the database users events are returned
#		 else function returns 0
def verifyPassword(email, testpassword):
    email = "\'"+email+"\'"
    query = "Select password from User where email="+email+";"
    cur.execute(query)
    password = str(cur.fetchone())
    if testpassword == password:
        return 1
    else:
        return 0

def getUserID(email):
    query = "Select user_id from User where email=\'"+email+"\';"
    cur.execute(query)
    row = cur.fetchall()
    num = str(row)
    num = num.replace(",", "")
    num = num.replace("(", "")
    num = num.replace(")", "")
    num = num.replace("L", "")
    return num

def getEventDetails(event_id):
    query = "Select eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription, event_id from Event where event_id="+str(event_id)+";"
    cur.execute(query)
    event = cur.fetchone()
    returnVal = {}
    returnVal["name"] = event[0]
    returnVal["date"] = str(event[1])
    returnVal["time"] = str(event[2])
    returnVal["duration"] = str(event[3])
    returnVal["radius"] = str(event[4])
    returnVal["description"] = str(event[5])
    returnVal["event_id"] = str(event[6])
    return returnVal

def stripResponse(inputVal):
    inputVal = str(re.findall('\d+', str(inputVal)))
    inputVal = inputVal.replace("'", "")
    inputVal = inputVal.replace("[", "")
    inputVal = inputVal.replace("]", "")
    return inputVal

def getUserData(user_id):
    query = "Select firstName, lastName, email from User where user_id="+user_id+";"
    cur.execute(query)
    val = cur.fetchone()
    returnVal = {}
    returnVal["firstName"] = val[0]
    returnVal["lastName"] = val[1]
    returnVal["email"] = val[2]
    print(returnVal)
    return returnVal

def getAssociatedEvents(user_id):
    query = "Select distinct event_id from Events_to_Attendees where user_id="+user_id+" and response='yes';"
    cur.execute(query)
    val = cur.fetchall()
    returnVal = {}
    final = []
    counter = 0
    for i in val:
        value = str(i[0])
        test = returnVal["event_id"] = value
        final.append(test)
    return final

def getManagerAssociatedEvents(user_id):
    query = "Select distinct event_id from Event where manager_id="+user_id+";"
    cur.execute(query)
    return cur.fetchall()

def commitEvent(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription):
	sql = "insert into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("+manager_id+",'"+eventName+"','"+eventDate+"','"+eventTime+"','"+eventDuration+"','"+eventRadius+"','"+eventDescription+"');"
	print(sql)
	cur.execute(sql)
	db.commit()

def commitInvitationResponse(event_id, user_id, response):
	sql = "Insert Into Events_to_Attendees(event_id, user_id, response) values ("+event_id+", "+user_id+", \'"+response+"\');"
	cur.execute(sql)
	db.commit()

#Default root used only in testing
@application.route("/")
def index():
	return jsonify(result={"status":200})

#This returns event details matching id given.
@application.route("/event/<idVal>/", methods=['GET'])
def event(idVal):
        event = getEventDetails(idVal)
        if event == None:
            print("no response")
            return jsonify(result={"status":"no event with id"})
        else:
            return jsonify(result=event)

#route for user's email and password submission. Calls verify password.
@application.route("/login", methods=['POST'])
def login():
    if request.method == 'POST':
        response = str(request.get_data())
        print("Login attempted from: " + response)
        decoded = unquote(response)
        decoded = decoded.split("&")
        password = decoded[0].split("=")
        email = decoded[1].split("=")
        password = password[1]
        email = email[1]
        password = "(\'" + password + "\',)"
        checker = verifyPassword(email, password)
        if checker != 0:
            return jsonify(result={"status":200, 'data':str(checker)})
    else:
        return jsonify(result={"status":400})

#may need to add a "maybe" field to the response field in the database.
#route for users to send a response to a invatation. Calls commitInvitationResponse
@application.route("/createResponse", methods=["POST"])
def createResponse():
        decoded = unquote(request.get_data())
        decoded = decoded.split("&")
        event_id = decoded[0].split("=")
        user_id = decoded[1].split("=")
        response = decoded[2].split("=")
        event_id = event_id[1]
        user_id = user_id[1]
        response = response[1]
        commitInvitationResponse(event_id, user_id, response)
	return jsonify(result={"status":200})

#route to create a new event.
@application.route("/createEvent", methods=["GET", "POST"])
def createEvent():
	if request.method == 'POST':
		response = str(request.get_data())
		print(response)
		commitEvent("1", "test name", "2019-04-24", "00:00:00", "00:00:00", "10.00", "this is a test eventDescription")
		return jsonify(result={"status":200})
	else: 
		return jsonify(result={"status":400})

#need to add step that checks to make sure there are no duplicate emails.
#route to create a new account.
@application.route("/createAccount", methods=["GET", "POST"])
def createAccount():
	if request.method == 'POST':
		response = str(request.get_data())
		response = response.replace("%40", "@")
		response = response.replace("key=", "")
		response = response[:-1]
		email, firstName, lastName, password = response.split("+", 4)
		sql = "insert into User(firstName, lastName, email, password) values('"+firstName+"','"+lastName+"','"+email+"','"+password+"');"
		cur.execute(sql)
		db.commit()
		return jsonify(result={"status":200})
	else:
		return jsonify(result={"status":400})

@application.route("/accountInformation/<idVal>/", methods=["GET"])
def accountInformation(idVal):
	return jsonify(result=str(getUserData(idVal)))

@application.route("/getUserIDNum/<email>/", methods=["GET"])
def getUserIDNum(email):
    userID = str(getUserID(email))
    return jsonify(result={"user_id":userID})

@application.route("/timeline/<idVal>/", methods=["GET"])
def timeline(idVal):
    events = getAssociatedEvents(idVal)
    finalDictionary = []
    result = {}
    counter = 0
    test = []
    for event in events:
        test.append(int(event))
    for event in test:
        result["event"] = str(getEventDetails(event))
        finalDictionary.append(result.copy())
    return jsonify(result=finalDictionary)

@application.route("/myEvents/<idVal>/", methods=["GET"])
def myEvents(idVal):
    x = getManagerAssociatedEvents(idVal)
    print("associated events: " + str(x))
    result = ''
    for i in x:
        result += str(getEventDetails(i[0]))
    return jsonify(result={"status":result})
