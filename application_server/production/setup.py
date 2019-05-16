'''
Name: loginToDatabase
Description: Function to login to database and save that connection to variable. Called during setup
Inputs: username, password
Outputs: database connection
'''
def loginToDatabase(username, password):
	import MySQLdb
	db = MySQLdb.connect(host="localhost", user=username, password=password, db="Tenda")
	return db

'''
Name: getCredentials
Description: Function to get database credentials for login to database function. Called during setup.
Inputs: None.
Outputs: username and password.
'''
def getCredentials():
	user = input("Username for database:")
	password = input("Password:")
	print(user + " : " + password)
	return user, password