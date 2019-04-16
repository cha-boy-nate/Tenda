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

