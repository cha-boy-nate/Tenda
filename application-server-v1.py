from flask import Flask, request, jsonify
import MySQLdb

db= MySQLdb.connect(host="localhost", user="dev", password="password", db="testDatabase")

cur=db.cursor()

app = Flask(__name__)

#this is a basic example of how flask routes are set up for application server
@app.route("/")
def index():
    return "This is the application server for Tenda"

#this is an example of retreiving something from the database.
@app.route("/user/lookup/<id>", methods=['GET'])
def user(id):
    cur.execute("Select * from testTable where user_id="+ id +";")
    for row in cur.fetchall():
        testData = row[1]
    return jsonify(result={testData:id})

#this is an example of adding something to the database.
@app.route("/user/add/<string:email>/<string:password>", methods=['GET', 'POST'])
def addUser(email, password):
    sql = "insert into testTable (email, password) values ('" + email + "','"+password + "')"
    cur.execute(sql)
    db.commit()
    return jsonify(result={"status": 200})



#these are the functions we need to create
#Description: Users and managers can sign up with their email address and set the username and password for the account.
#Parameters: Email, username, password, firstname, lastname.
#Verfiy new emails and usernames are unique.
@app.route("/createAccount/<string:email>/<string:firstname>/<string:lastname>/<string:username>/<string:password>", methods=['GET', 'POST'])
def createAccount(email, firstname, lastname, username, password):
    sql = "insert into accountsTable (email, firstname, lastname, username, password) values (%s, %s, %s, %s, %s)"
    val = (email, firstname, lastname, username, password)
    cur.execute(sql, val)
    db.commit()
    return jsonify(result={"status": 200})

#Description: A user can become a manager by creating an event. They can then specify the details of the event and send an invitation by the user’s email.
@app.route("/createEvent/", methods=['GET', 'POST'])
def createEvent():
    return jsonify(result={"status": 200})

#Description: Users will receive an invitation through email to reply to the event. 
@app.route("/joinEvent/", methods=['GET', 'POST'])
def joinEvent():
    return jsonify(result={"status": 200})

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
