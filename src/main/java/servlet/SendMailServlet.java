package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import org.owasp.encoder.Encode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendMailServlet
 */
@WebServlet("/SendMailServlet")
public class SendMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String USER = "sa";
	private static final String PWD = "Riva96_shared_db";
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=examDB;encrypt=true;trustServerCertificate=true;";
    
	private static Connection conn;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	try {
			Class.forName(DRIVER_CLASS);
			
		    Properties connectionProps = new Properties();
		    connectionProps.put("user", USER);
		    connectionProps.put("password", PWD);
	
	        conn = DriverManager.getConnection(DB_URL, connectionProps);
		    
		    //System.out.println("User \"" + USER + "\" connected to database.");
    	
    	} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }
	
	
	/**
	 * Encodes for HTML text content.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForHtmlContent(String s) {
		return Encode.forHtmlContent(s);
	}
	
	/**
	 * This method encodes for JavaScript strings contained within HTML script attributes (such as onclick).
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJavaScriptAttribute(String s) {
		return Encode.forJavaScriptAttribute(s);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String sender = request.getParameter("email").replace("'", "''");;
		String receiver = request.getParameter("receiver").replace("'", "''");;
		String subject = request.getParameter("subject").replace("'", "''");;
		String body = request.getParameter("body").replace("'", "''");;
		String timestamp = new Date(System.currentTimeMillis()).toInstant().toString();
		
		//Encode for protection against attacks
		String encodedSubject = encodeForJavaScriptAttribute(subject);
		String encodedBody = encodeForJavaScriptAttribute(encodeForHtmlContent(body));
		
		System.out.println(encodeForJavaScriptAttribute(subject));
		System.out.println(encodeForJavaScriptAttribute(encodeForHtmlContent(body)));

		try (Statement st = conn.createStatement()) {
			st.execute(
				"INSERT INTO mail ( sender, receiver, subject, body, [time] ) "
				+ "VALUES ( '" + sender + "', '" + receiver + "', '" + encodedSubject + "', '" + encodedBody + "', '" + timestamp + "' )"
			);
			System.out.print("\nMAIL SENT AT \t" + timestamp);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		request.setAttribute("email", sender);
		request.getRequestDispatcher("home.jsp").forward(request, response);
	}

}
