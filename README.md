# InformationSecurity Project
Written by Antonio Coppe

## What is provided

The application provided is a web application that allows: 

- Login management;
- Reading, sending and receiving e-mails;

Using a web server Tomcat. The application is provided at: https://github.com/francxx96/ExamProject
and is totally "insecure".

## Discovered Vulnerabilities

### SQL Injection
Via the login form is possible to inject queries such as:
```
'DROP TABLE [user];-- 	        --> Deleting the user table
'DROP TABLE mail;--		--> Deleting the mail table
'DROP TABLE *;--		--> Deleting all tables
```

### XSS Reflected

Via the register form is possible to inject runnable scripts such the one below:

```
<script>alert(This is a Virus!!!)</script>
```

### XSRF
With the use of a tool called ***Wireshark***(A free and open-source packet analyzer) is possible to capture *HTTP* traffic and analyze it with various tools. I will list below the step by step process for capturing the aformentioned information:
- Run The Web Application and login;
- Open wireshark and select the interface;
- Go back to the browser and send an email;
- Filter the result in wireshark to http;
- Find the *POST* and *GET* requests;
- Open and Read the content under HTML Form URL Encoded;

![This is an image](https://github.com/AntonioCoppe/InformationSecurityProject/blob/main/WiresharkPacketSniffing.png)
![This is an image](https://github.com/AntonioCoppe/InformationSecurityProject/blob/main/WiresharkMailExploited.png)

## Implementation of a "Secure" application

*To be done*


