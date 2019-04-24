from flask import Flask, request, jsonify

import json
import setup

db = setup.getOS()
cur = db.cursor()

application = Flask(__name__)

def verifyPassword(email, testpassword):
    email = "\'"+email+"\'"
    query = "Select password from User where email="+email+";"
    cur.execute(query)
    password = str(cur.fetchone())
    if testpassword == password:
        return 1
    else:
        return 0

@application.route("/")
def index():
	return jsonify(result={"status":200})

@application.route("/home")
def home():
    return jsonify(result={"value":200})

@application.route("/login", methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        response = str(request.get_data())
        print("request recieved")
        response = response.replace("%40", "@")
        response = response.replace("'", "")
        response = response.replace("&", "")
        response = response.split("=")
        response = "{\"email\":\""+response[0]+"\",\"password\":\""+response[1]+"\"}"
        response = json.loads(response)
        password = "(\'" + response["password"] + "\',)"
        checker = verifyPassword(response["email"], password)
        if checker == 1:
            return jsonify(result={"status":200})
        else:
            return jsonify(result={"status":405})
    else:
        print("not post request")
        return jsonify(result={"status":400})

@application.route("/createAccount", methods=["GET", "POST"])
def createAccount():
	if request.method == 'POST':
		#print("post method detected")
		#print(str(request.get_data()))
                response = str(request.get_data())
                response = response.replace("%40", "@")
                response = response.replace("key=", "")
                response = response[:-1]
                email, firstName, lastName, password = response.split("+", 4)
                #print(email + firstName + lastName + password)
                sql = "insert into User(firstName, lastName, email, password) values('"+firstName+"','"+lastName+"','"+email+"','"+password+"');"
                cur.execute(sql)
                db.commit()
		return jsonify(result={"status":200})
	else:
		print("get method detected")
		return jsonify(result={"status":400})
	
@application.route("/event/<idVal>/", methods=['GET'])
def getEvent(idVal):
    query = "Select distinct event_id from Events_to_Attendees where user_id="+idVal+" and response='yes';"
    cur.execute(query)
    val = cur.fetchall()
    resultsVal = "events:["
    for x in val:
        q = "select eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription from Event where event_id="+str(x[0])+";"
        cur.execute(q)
        responseVal = cur.fetchone()
        #j = jsonify(event={"eventName": str(responseVal[0]), "eventDate":str(responseVal[1]), "eventTime":str(responseVal[2]), "eventDuration":str(responseVal[3]), "eventRadius":str(responseVal[4]), "eventDuration":str(responseVal[5])})
        resultsVal = resultsVal + "{eventName:\'"+str(responseVal[0])+"\', eventName:\'"+str(responseVal[1])+"\', eventTime:\'"+str(responseVal[2])+"\', eventDuration:\'"+str(responseVal[3])+"\', eventRaidus:\'"+str(responseVal[4])+"\', eventDescription:\'"+str(responseVal[5])+"\'},"
    resultsVal = resultsVal + "]"
    return jsonify(result={"status": resultsVal})
