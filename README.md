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
"><script>alert(document.cookie)</script>
```

### XSS Stored

Via the Object or Body field is possible to inject runnable javascript code such the one below:
```javascript
<img src=x onerror="alert('Pop-up window via stored XSS');"
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

"Safe" Code for the loginServlet.java class:

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

"Safe" Code for the RegisterServlet.java class:

```java
try {

			PreparedStatement statement = conn.prepareStatement(SelectQuery);
			statement.setString(1, email);
			ResultSet sqlRes = statement.executeQuery();

			if (sqlRes.next()) {
				System.out.println("Email already registered!");
				request.getRequestDispatcher("register.html").forward(request, response);

			} else {

				String InsertQuery = "SET NOCOUNT ON INSERT INTO [user] (name, surname, email, password ) VALUES (?, ?, ?, ?)";

				PreparedStatement Insertstatement = conn.prepareStatement(InsertQuery);
				Insertstatement.setString(1, name);
				Insertstatement.setString(2, surname);
				Insertstatement.setString(3, email);
				Insertstatement.setString(4, pwd);

				@SuppressWarnings("unused")
				boolean sqlInsRes = Insertstatement.execute();

				request.setAttribute("email", email);
				request.setAttribute("password", pwd);

				System.out.println("Registration succeeded!");
				request.getRequestDispatcher("home.jsp").forward(request, response);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("register.html").forward(request, response);
		}
```


