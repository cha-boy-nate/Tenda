def getOS():
    import MySQLdb
    db = MySQLdb.connect(host="localhost", user="testuser", password="test!Password1", db="Tenda")
    return db
