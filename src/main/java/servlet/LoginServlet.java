package servlet;

import jakarta.servlet.http.HttpServlet;
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

import org.owasp.encoder.Encode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String USER = "sa";
	private static final String PWD = "Riva96_shared_db";
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=examDB;encrypt=true;trustServerCertificate=true;";

	private static Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	public void init() throws ServletException {
		try {
			Class.forName(DRIVER_CLASS);

			Properties connectionProps = new Properties();
			connectionProps.put("user", USER);
			connectionProps.put("password", PWD);

			conn = DriverManager.getConnection(DB_URL, connectionProps);

			System.out.println("User \"" + USER + "\" logged into the database.");

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
	 */
	public static String encodeForJava(String s) {
		return Encode.forJava(s);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		/*
		 * Here is available the sql Injeciton ""free""" code
		 */

		String email = request.getParameter("email");
		String pwd = request.getParameter("password");

		String query = "SELECT * FROM [user] WHERE email = ? AND password = ?";

		try {

			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, md5(pwd));
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
		
		/*
		 * Here is available the Vulnerable code
		 */

		/*
		 * try (Statement st = conn.createStatement()) { ResultSet sqlRes =
		 * st.executeQuery( "SELECT * " + "FROM [user] " + "WHERE email='" + email +
		 * "' " + "AND password='" + pwd + "'" );
		 * 
		 * if (sqlRes.next()) { request.setAttribute("email", sqlRes.getString(3));
		 * request.setAttribute("password", sqlRes.getString(4));
		 * 
		 * System.out.println("Login succeeded!"); request.setAttribute("content", "");
		 * request.getRequestDispatcher("home.jsp").forward(request, response);
		 * 
		 * 
		 * } else { System.out.println("Login failed!");
		 * request.getRequestDispatcher("login.html").forward(request, response); }
		 * 
		 * } catch (SQLException e) { e.printStackTrace();
		 * request.getRequestDispatcher("login.html").forward(request, response); }
		 */
	}
}
