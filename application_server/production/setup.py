def getOS():
    import MySQLdb
    db = MySQLdb.connect(host="localhost", user="root", password="password", db="Tenda")
    return db
