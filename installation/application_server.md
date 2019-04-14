This file explains how to publish the flask server in a production environment.
First install needed packages:
- upgrade the current server with (sudo apt-get update)
- download nginx and the other tools help to export the flask application to a web server (sudo apt-get install python-pip python-dev nginx)
- install virtual envirnment (sudo pip install virtualenv)

Second activate virtual envirnment:
- start virtual environment for flask application with (virtualenv tendaEnvironment)
- activate virtual environment (source tendaEnvironment/bin/activate)

Third install flask and gunicorn
- using pip install flask and gunicorn (pip install gunicorn flask)

Fourth create wsgi for application entry point
- see Tenda/application_server/wsgi.py

Fifth invoke gunicorn to start with wsgi entry point
- (gunicorn --bind 0.0.0.0:8000 wsgi)
