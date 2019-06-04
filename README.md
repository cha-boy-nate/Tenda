# Tenda
Tenda is a location based verification system. Simply create an event, invite your friends, and see who attends. This project includes the following code: 
<ul>
 <li>The source code project file for android studio to generate a mobile application.</li>
 <li>Code to set up the MySQL database.</li>
 <li>Code to virtualize an application server.</li>
</ul>
<ol>
 
 <li> 
  <h2>Background Information</h2>
  <p>Tenda is deployed using an Ubuntu based Amazon EC2 server. Tenda uses a MySQL database on the backend. It's application   server is written in Python using Flask, deployed on an apache web server. On the client-side, consumers interact with Tenda via an android application.</p> 
 </li> 
 
 <li>
  <h2>Requirements</h2>
 </li>
 
  <li>
   <h2>Installation</h2>
  <ol>
  <li><p>Download the project:<code>git clone https://github.com/cha-boy-nate/Tenda</code></p></li>
   
   <li>Install pip<p><code></code></p></li>
   
   
   <li>Install Virtual environment for flask code:<p><code></code></p></li>
   <li>Activate virtual environment:<p><code></code></p></li>
   <li>Install Flask on virtual environment:<p><code>pip install flask</code></p></li>
   <li>Install PyMySQL used for managing connections between the application server and database:<p><code>pip install pymysql</code></p></li>
   
   
   
   <li>
   <p>Setup Database</p>
   <ol>
    <li>Install MySQL <code>sudo apt install mysql-server</code></li> 
    <li>Create tables <code>sudo mysql < database-setup-v1.sql</code></li> 
    <li>Populate tables <code>sudo mysql < populate.sql</code></li> 
   </ol>
   </li>
  
   
  
  
  
 
  </ol> 
 </li>
 

  
 <li>
  <h2>File Structure</h2>
 </li>
</ol>
