# InformationSecurity Project
Written by Antonio Coppe

## What is provided

The application provided is a web application that allows: 

- Login management;
- Reading, sending and receiving e-mails;

As a web server *Tomcat 10.0.16* was used, as a database *MySQL* and the applications to manage the database *DBeaver* and *Docker*. The application is provided at: https://github.com/francxx96/ExamProject and is totally "insecure".

## Discovered Vulnerabilities

### SQL Injection
Via the login form is possible to inject queries such as:
```mysql
'DROP TABLE [user];-- 	        --> Deleting the user table
'DROP TABLE mail;--		--> Deleting the mail table
'DROP TABLE *;--		--> Deleting all tables
```

### XSS Reflected

Via the register form is possible to inject runnable scripts such the one below:

```javascript
<script>alert(This is a Virus!!!)</script>
```

### Sniffing Attack
With the use of a tool called ***Wireshark***(A free and open-source packet analyzer) is possible to capture *HTTP* traffic and analyze it with various tools. I will list below the step by step process for capturing the aformentioned information:
- Run The Web Application and login;
- Open wireshark and select the interface;
- Go back to the browser and send an email;
- Filter the result in wireshark to http;
- Find the *POST* and *GET* requests;
- Open and Read the content under HTML Form URL Encoded;

![This is an image](https://github.com/AntonioCoppe/InformationSecurityProject/blob/main/WiresharkMailExploited.png)
![This is an image](https://github.com/AntonioCoppe/InformationSecurityProject/blob/main/WiresharkPacketSniffing.png)


## Implementation of a "Secure" application

### SQL Injection
A solution i found was to use parametrized queries. That means you don't concatenate user-supplied values. Instead you use placesholders for those values ensuring the values themselves are never part of the text of the query.

Example of the code for the login.java class:

```java
String email = request.getParameter("email");
		String pwd = request.getParameter("password");

		String query = "SELECT * FROM [user] WHERE email = ? AND password = ?";

		try {

			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, pwd);
			ResultSet sqlRes = statement.executeQuery();

			if (sqlRes.next()) {
				request.setAttribute("email", sqlRes.getString(3));
				request.setAttribute("password", sqlRes.getString(4));

				System.out.println("Login succeeded!");
				request.setAttribute("content", "");
				request.getRequestDispatcher("home.jsp").forward(request, response);

			} else {
				System.out.println("Login failed!");
				request.getRequestDispatcher("login.html").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("login.html").forward(request, response);
		}

```


