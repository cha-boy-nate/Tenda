# Tenda
Tenda is a location based verification system. Simply create an event, invite your friends, and see who attends. This project includes the following code: 
<ul>
 <li>The source code project file for android studio to generate a mobile application.</li>
 <li>Code to set up the MySQL database.</li>
 <li>Code to virtualize an application server.</li>
</ul>
<ol>
 <li> 
  <h4>Background Information</h4>
 </li> 
 <li>
  <h4>Installation</h4>
 </li>
 <li>
  <h4>Requirements</h4>
 <li>
   
   
<h4></h4>
<h4></h4>
</ol>


Running application server:\
  cd application_server/\
  source tenda_env/bin/activate\
  gunicorn --bind 0.0.0.0:80000 wsgi\
