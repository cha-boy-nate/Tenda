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
    #val = (email, password)
    cur.execute(sql)
    db.commit()
    return jsonify(result={"status": 200})

app.run(host='0.0.0.0', port=80)
