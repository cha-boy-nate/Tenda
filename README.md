# Tenda
Tenda is a location based verification system. Simply create an event, invite your friends, and see who attends.


Running application server:\
  cd application_server/\
  source tenda_env/bin/activate\
  gunicorn --bind 0.0.0.0:80000 wsgi\
