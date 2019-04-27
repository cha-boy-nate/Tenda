from flask import Flask, request, jsonify

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
        query2 = "Select user_id from User where email="+email+";"
        cur.execute(query2)
        user_id = cur.fetchone()
        val = stripResponse(user_id)
        userData = getUserData(val)
        events = getAssociatedEvents(val)
        events = stripResponse(events)
        eventDetails = getEventDetails(events)
        returnData = {}
        returnData['user-data'] = userData
        returnData['event-id'] = events
        returnData['event-details-for-id-' + events] = eventDetails
        return returnData
    else:
        return 0

#
def getUserID(email):
    query = "Select user_id from User where email=\'"+email+"\';"
    cur.execute(query)
    return cur.fetchone()

def getEventDetails(event_id):
    query = "Select eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription from Event where event_id="+event_id+";"
    cur.execute(query)
    return cur.fetchone()

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
    return val

def getAssociatedEvents(user_id):
    query = "Select distinct event_id from Events_to_Attendees where user_id="+user_id+" and response='yes';"
    cur.execute(query)
    val = cur.fetchone()
    return val

def commitEvent(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription):
	sql = "insert into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("+manager_id+",'"+eventName+"','"+eventDate+"','"+eventTime+"','"+eventDuration+"','"+eventRadius+"','"+eventDescription+"');"
	print(sql)
	cur.execute(sql)
	db.commit()

def commitInvitationResponse(event_id, user_id, response):
	sql = "Insert Into Events_to_Attendees(event_id, user_id, response) values ("+event_id+", "+user_id+", \'"+response+"\');"
	print(sql)
	cur.execute(sql)
	db.commit()

#Default root used only in testing
@application.route("/")
def index():
	return jsonify(result={"status":200})

#This returns all the events that a user has said they will attend.
@application.route("/event/<idVal>/", methods=['GET'])
def event(idVal):
	query = "Select distinct event_id from Events_to_Attendees where user_id="+idVal+" and response='yes';"
	cur.execute(query)
	val = cur.fetchall()
	resultsVal = "events:["
	for x in val:
		q = "select eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription from Event where event_id="+str(x[0])+";"
		cur.execute(q)
		responseVal = cur.fetchone()
		resultsVal = resultsVal + "{eventName:\'"+str(responseVal[0])+"\', eventName:\'"+str(responseVal[1])+"\', eventTime:\'"+str(responseVal[2])+"\', eventDuration:\'"+str(responseVal[3])+"\', eventRaidus:\'"+str(responseVal[4])+"\', eventDescription:\'"+str(responseVal[5])+"\'},"
		resultsVal = resultsVal + "]"
	return json.dumps(resultsVal)

#route for user's email and password submission. Calls verify password.
@application.route("/login", methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        response = str(request.get_data())
        response = response.replace("%40", "@")
        response = response.replace("'", "")
        response = response.replace("&", "")
        response = response.split("=")
        response = "{\"email\":\""+response[0]+"\",\"password\":\""+response[1]+"\"}"
        response = json.loads(response)
        password = "(\'" + response["password"] + "\',)"
        checker = verifyPassword(response["email"], password)
        if checker != 0:
            print(str(checker))
            return jsonify(result={"status":200, 'data':str(checker)})
        else:
            return jsonify(result={"status":405})
    else:
        print("not post request")
        return jsonify(result={"status":400})

#may need to add a "maybe" field to the response field in the database.
#route for users to send a response to a invatation. Calls commitInvitationResponse
@application.route("/createResponse", methods=["GET", "POST"])
def createResponse():
	print('test print val')
	commitInvitationResponse("3", "1", "yes")
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
		print("get method detected")
		return jsonify(result={"status":400})

@application.route("/accountInformation/<idVal>/", methods=["GET"])
def accountInformation(idVal):
	return jsonify(result={"result":str(getUserData(idVal))})
