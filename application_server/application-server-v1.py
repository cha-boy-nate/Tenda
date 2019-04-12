from flask import Flask, request, jsonify

import json
import setup

db = setup.getOS()
cur = db.cursor()

app = Flask(__name__)

def verifyPassword(email, testpassword):
    email = "\'"+email+"\'"
    query = "Select password from User where email="+email+";"
    cur.execute(query)
    password = str(cur.fetchone())
    if testpassword == password:
        return 1
    else:
        return 0

def getAll(query, resultTitle):
    cur = db.cursor()
    cur.execute(query)
    result = []
    for row in cur.fetchall():
        result.append(row)
    print("query complete. returning ")
    return jsonify(result={resultTitle:result})

@app.route("/")
def index():
	return jsonify(result={"status":200})

@app.route("/home")
def home():
    return jsonify(result={"value":200})

@app.route("/login", methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        response = str(request.get_data())
        response = response.replace("%40", "@")
        response = response.replace("'", "")
        response = response.replace("&", "")
        response = response[1:]
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

@app.route("/user/lookup/email/<user_email>", methods=['GET'])
def getUserByEmail(user_email):
    cur.execute("Select user_id, firstName, lastName, email from User where email=\'"+ user_email +"\';")
    user_id, firstName, lastName, email = cur.fetchone()
    return jsonify(result={"id":user_id,"first name":firstName, "last name":lastName, "email":email})

#this is an example of retreiving something from the database.
@app.route("/user/lookup/<user_id>", methods=['GET'])
def getUser(user_id):
    cur.execute("Select firstName, lastName, email from User where user_id="+ user_id +";")
    firstName, lastName, email = cur.fetchone()
    return jsonify(result={"id":user_id,"first name":firstName, "last name":lastName, "email":email})

#gets invitations for user by id
@app.route("/user/event/lookup/<user_id>", methods=['GET'])
def getUsersEventByID(user_id):
    return getAll("Select event_id from Events_to_Attendees where user_id="+user_id+";", "result")

#get event details from an eventID
@app.route("/event/lookup/<event_id>", methods=['GET'])
def getEventDetails(event_id):
    cur.execute("Select manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription from Event where event_id="+ event_id +";")
    manager_id, eventName, date, time, duration, radius, description = cur.fetchone()
    return jsonify(result={"managerID":manager_id, "eventName":eventName, "date":str(date), "time":str(time), "duration":str(duration), "radius":str(radius), "description":description})

#complete
@app.route("/event/attendees/<event_id>/<response>", methods=["GET"])
def getEventAttendees(event_id, response):
    return getAll("Select user_id from Events_to_Attendees where event_id="+ event_id +" AND response='"+response+"';", "result")

#Description: Users and managers can sign up with their email address and set the username and password for the account.
@app.route("/user/add/<string:firstName>/<string:lastName>/<string:email>/<string:password>", methods=['GET', 'POST'])
def createUser(firstName, lastName, email, password):
    sql="insert into User(firstName, lastName, email, password) values('"+firstName+"','"+lastName+"','"+email+"','"+password+"')"
    cur.execute(sql)
    db.commit()
    return jsonify(result={"status": 200})

#Description: A user can become a manager by creating an event. They can then specify the details of the event and send an invitation by the user’s email.
@app.route("/event/add/<manager_id>/<eventName>/<eventDate>/<eventTime>/<eventDuration>/<eventRadius>/<eventDescription>", methods=['GET', 'POST'])
def createEvent(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription):
    sql = "insert into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("+manager_id+",'"+eventName+"','"+eventDate+"','"+eventTime+"','"+eventDuration+"','"+eventRadius+"','"+eventDescription+"');"
    cur.execute(sql)
    db.commit()
    return jsonify(result={"status": 200})

@app.route("/response/add/<event_id>/<user_id>/<response>", methods=['GET', 'POST'])
def createResponse(event_id, user_id, response):
    sql = "insert into Events_to_Attendees(event_id, user_id, response) values ("+event_id+","+user_id+",'"+response+"');"
    cur.execute(sql)
    db.commit()
    return jsonify(result={"status": 200})

#Needs to be implemented
#Description: The manager is able to view the details of the event, the people that opened the link as well as their response. As well as the duration of the user at the event. Recurring events will also have a page for statistical data. 
@app.route("/manageEvent/", methods=['GET', 'POST'])
def manageEvent():
    return jsonify(result={"status": 200})

#Description: If the app detects an error then the user will be prompted to select whether or not they wish send the automated report. The app users and managers will be able to send a report to the developer team. 
@app.route("/reportIssue/", methods=['GET', 'POST'])
def reportIssue():
    return jsonify(result={"status": 200})

#Description: In case of an event emergency managers will have the ability to notify users present at the event to locate safety.
@app.route("/notifyUsers/", methods=['GET', 'POST'])
def notifyUsers():
    return jsonify(result={"status": 200})

app.run(host='0.0.0.0', port=80)
#if __name__ == '__main__':
#    app.run()

