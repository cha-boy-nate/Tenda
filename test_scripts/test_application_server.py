from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route("/")
def home():
  return jsonify(result={"status":200})
  
@app.route("/test")
def test():
  print("\nPrinting Test\n")
  return jsonify(result={"status":200})
