# Tenda

Installation:
1. Steps for Database:
    a. Install mysql to machine. Setting root password to "password" during install (this can be changed later).
    b. Turn install.sh into an exexutable by running the command "chmod +x install.sh".
    c. Run the executable "./install".
    d. Keep the password as "password" and answer yes to all the questions in the installation wizard.
 
2. Steps for Application-Server:
    a. Install python3 to machine. 
        i. First check if its installed with "python3 --version".
        ii. Skip to part b if version is after 3.5.0.
        iii. If not installed, install with "sudo apt-get python3".
    b. Install Flask to machine.
        i. If pip is install used "pip install flask".
        ii. Otherwise use "sudo apt-get install python3-flask".
    c. Run the application server with "python3 application-server-v1.py".
  
3. Steps for Application:
    a. Open project folder in android studio.
    b. Export app to plugged in device.
  
