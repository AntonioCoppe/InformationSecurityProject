package servlet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String USER = "sa";
	private static final String PWD = "Riva96_shared_db";
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=examDB;encrypt=true;trustServerCertificate=true;";

	private static Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	public void init() throws ServletException {
		try {
			Class.forName(DRIVER_CLASS);

			Properties connectionProps = new Properties();
			connectionProps.put("user", USER);
			connectionProps.put("password", PWD);

			conn = DriverManager.getConnection(DB_URL, connectionProps);

			System.out.println("User \"" + USER + "\" registered to the database.");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Java program to calculate MD5 hash value
    public static String md5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    /**
	 * Encodes for a Java string.
	 *
	 * @param s
	 * @return Encoded String
	 
	public static String encodeForJava(String s) {
		return Encode.forJava(s);
	}*/

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		// The replacement escapes apostrophe special character in order to store it in
		// SQL
		String name = request.getParameter("name").replace("'", "''");
		String surname = request.getParameter("surname").replace("'", "''");
		;
		String email = request.getParameter("email").replace("'", "''");
		;
		String pwd = request.getParameter("password").replace("'", "''");
		;

		String SelectQuery = "SELECT * FROM [user] WHERE email = ?";

		/*
		 * Here is available the SQL Injection ""free""" code
		 */

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
				Insertstatement.setString(4, md5(pwd));

				@SuppressWarnings("unused")
				boolean sqlInsRes = Insertstatement.execute();

				request.setAttribute("email", email);
				request.setAttribute("password", md5(pwd));

				System.out.println("Registration succeeded!");
				request.getRequestDispatcher("home.jsp").forward(request, response);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("register.html").forward(request, response);
		}

		/*
		 * Here is available the vulnerable code to SQL injection
		 */

		/*
		 * 
		 * try (Statement st = conn.createStatement()) { ResultSet sqlRes =
		 * st.executeQuery( "SELECT * " + "FROM [user] " + "WHERE email='" + email + "'"
		 * );
		 * 
		 * if (sqlRes.next()) { System.out.println("Email already registered!");
		 * request.getRequestDispatcher("register.html").forward(request, response);
		 * 
		 * } else { st.execute( "INSERT INTO [user] ( name, surname, email, password ) "
		 * + "VALUES ( '" + name + "', '" + surname + "', '" + email + "', '" + pwd +
		 * "' )" );
		 * 
		 * request.setAttribute("email", email); request.setAttribute("password", pwd);
		 * 
		 * System.out.println("Registration succeeded!");
		 * request.getRequestDispatcher("home.jsp").forward(request, response); }
		 * 
		 * } catch (SQLException e) { e.printStackTrace();
		 * request.getRequestDispatcher("register.html").forward(request, response); }
		 */
	}

}
