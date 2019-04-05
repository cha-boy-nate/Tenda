def getOS():
	value = 0;
	while value == 0:
		os = input("Please select:\n1 - for Linux\n2 - for MacOS\nEnter: ")
		if os == "1":
			import MySQLdb
			db = MySQLdb.connect(host="localhost", user="root", password="password", db="Tenda")
			value = 1
			return db
		elif os == "2":
			import pymysql
			pymysql.install_as_MySQLdb()
			db = pymysql.connect(host="localhost", user="root", password="password", db="Tenda")
			value = 1
			return db
		else:
			print("Invalid Input.")
