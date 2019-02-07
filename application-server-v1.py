from flask import Flask, request
import MySQLdb

db = MySQLdb.connect("localhost", "dev", "password", "testDatabase")
cur= db.cursor()

app = Flask(__name__)

@app.route("/")
def index():
	return "This is the application server for Tenda."

@app.route("/user/lookup/<id>", methods=['GET'])
def user(id):
	cur.execute("Select * from testTable wherer user_id="+id+";")
	for row in cur.fetchall():
		testData = row[1]
	return testData

app.run(host='0.0.0.0', port=80)

if __name__ == "__main__":
	app.run()