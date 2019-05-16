def getManagerAssociatedEvents(user_id, cur):
	query = "Select distinct event_id from Event where manager_id="+user_id+";"
	cur.execute(query)
	result = cur.fetchall()
	returnVal = {}
	final = []
	for i in result:
		value = str(i[0])
		test = returnVal["event_id"] = value
		final.append(test)
	return final

def getEventDetails(event_id, cur):
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


def commitInvitationResponse(event_id, user_id, response, cur, db):
	sql = "Insert Into Events_to_Attendees(event_id, user_id, response) values ("+event_id+", "+user_id+", \'"+response+"\');"
	cur.execute(sql)
	db.commit()

def commitEvent(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription, cur, db):
	sql = "insert into Event(manager_id, eventName, eventDate, eventTime, eventDuration, eventRadius, eventDescription) values ("+manager_id+",'"+eventName+"','"+eventDate+"','"+eventTime+"','"+eventDuration+"','"+eventRadius+"','"+eventDescription+"');"
	cur.execute(sql)
	db.commit()

def verifyPassword(email, testpassword, cur):
	email = "\'"+email+"\'"
	query = "Select password from User where email="+email+";"
	cur.execute(query)
	password = str(cur.fetchone())
	if testpassword == password:
		return 1
	else:
		return 0

#Function will return userID for the specified email.
def getUserID(email, cur):
	query = "Select user_id from User where email=\'"+email+"\';"
	cur.execute(query)
	row = cur.fetchall()
	num = str(row)
	num = num.replace(",", "")
	num = num.replace("(", "")
	num = num.replace(")", "")
	num = num.replace("L", "")
	return num
	
def stripResponse(inputVal):
	inputVal = str(re.findall('\d+', str(inputVal)))
	inputVal = inputVal.replace("'", "")
	inputVal = inputVal.replace("[", "")
	inputVal = inputVal.replace("]", "")
	return inputVal

def getUserData(user_id, cur):
	query = "Select firstName, lastName, email from User where user_id="+user_id+";"
	cur.execute(query)
	val = cur.fetchone()
	returnVal = {}
	returnVal["firstName"] = val[0]
	returnVal["lastName"] = val[1]
	returnVal["email"] = val[2]
	print(returnVal)
	return returnVal

def getAssociatedEvents(user_id, cur):
	query = "Select distinct event_id from Events_to_Attendees where user_id="+user_id+" and response='yes';"
	cur.execute(query)
	val = cur.fetchall()
	returnVal = {}
	final = []
	for i in val:
		value = str(i[0])
		test = returnVal["event_id"] = value
		final.append(test)
	return final