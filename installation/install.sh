sudo apt install mysql-client
sudo mysql_secure_installation -password

sudo mysql -h localhost -u root -p < database-setup-v1.sql
