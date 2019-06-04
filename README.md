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
 <ol>
  <h4>For EC2 instance</h4>
  <li>Key pair to connect to server via ssh</li>
  <li>Ubuntu server after version 16.04</li>
 </ol>

 <ol>
  <h4>For EC2 instance security group inbound rules</h4>
  <li>Allow HTTP traffic over port 80</li>
  <li>Allow SSL traffic over port 22</li>
 </ol>
 
 
 </li>
 
  <li>
   <h2>Installation</h2>
  <ol>
   <h4>Setup application server</h4>
   
   <li><p>Download the project:<code>git clone https://github.com/cha-boy-nate/Tenda</code></p></li>
   <li><p>Install pip:<code>sudo apt-get install python3-pip</code></p></li>
   <li><p>Install apache:<code>sudo apt-get install apache2</code></p></li>
   <li><p>Move configuration file to apache configuration location<code></code></p></li>
   
   <li><p>Install virtual environment for flask code:<code>pip3 install virtualenv</code></p></li>
   <li><p>Create virtual environment:<code>virtualenv venv</code></p></li>
   <li><p>Activate virtual environment:<code>source venv/bin/activate</code></li>
   <ol>
   <li><p>Install Flask on virtual environment:<code>pip install flask</code></p></li>
   <li><p>Install PyMySQL used for managing connections between the application server and database:<p><code>pip install pymysql</code></p></li>
   </ol>
   <li><p>Deactivate virtual environment with:<code>deactivate</code></p></li>
  
   </ol> 
   
   
   <ol>
   <h4>Setup Database</h4>
   <li>Install MySQL <code>sudo apt install mysql-server</code></li> 
   <li>Create tables <code>sudo mysql < database-setup-v1.sql</code></li> 
   <li>Populate tables <code>sudo mysql < populate.sql</code></li> 
   </ol>
   
  
  
  
 
  
 </li>
 

  
 <li>
  <h2>File Structure</h2>
 </li>
</ol>
