def getOS():
    import MySQLdb
    db = MySQLdb.connect(host="localhost", user="flask", password="password", db="Tenda")
    return db
