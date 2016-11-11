package Servlet1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import javax.naming.Context;
import javax.naming.InitialContext;

// servlet class
@WebServlet("/servlet_class")
public class servlet_class extends HttpServlet {

	// variables and objects
	static Connection conn = null;
	private static final long serialVersionUID = 1L;

	// http doGet method
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// start html form, setup title and other options
		out.print("<html><head><title>Employees</title> "
				+ "<style> table { font-family: arial, sans-serif; border-collapse: collapse;width: 80%; }"
				+ "td, th { text-align: left; padding: 8px; } tr:nth-child(even) "
				+ "{background-color: #dddddd}</style></head><body><font size=\"20\">Employees</font>");
		// html table
		out.print(
				"<table><tr> <th>First Name</th> <th>Last Name</th> <th>Email</th> <th>Phone Number</th> <th>Hire Date</th> "
						+ "<th>Salary</th> </tr>");
		// html back button
		out.print("<form action= \"/ServletDemo/index.html\" ><input type= \"submit\" value= \"Go Back\" /></form>");
		
		// html form button
		out.print("<form method = \"POST\" action= \"/ServletDemo/servlet_class\" ><input type=\"hidden\" name=\"method\" value=\"htmlform\">"
				+ "<input type= \"submit\" value= \"Insert Employee\" /></form>");

		try {

			// select data
			CallableStatement cs = conn.prepareCall("{ call servlet_package.myselect(?) }");
			cs.registerOutParameter(1, OracleTypes.CURSOR);
			cs.execute();

			// ResultSet object only works to display data if query returns data
			ResultSet rs = (ResultSet) cs.getObject(1);

			// show data in html form
			while (rs.next()) {

				out.print("<tr> <td>" + rs.getString("first_name") + "</td> <td>" + rs.getString("last_name")
						+ "</td> <td>" + rs.getString("email") + "</td> <td>" + rs.getString("phone_number")
						+ "</td> <td>" + rs.getString("hire_date") + "</td> <td>" + rs.getString("salary")
						// update button
						+ "</td> <td><form method = \"POST\"><input type=\"hidden\" name=\"method\" value=\"htmlformupdate\">"
						+ "<input type=\"hidden\" name=\"employee_id\" value=\"" + rs.getString("employee_id") + "\">"
						+ "<input type = \"submit\" value= \"Update\"></form></td>"
						// delete button
						+ "<td><form method = \"POST\"><input type=\"hidden\" name=\"method\" value=\"delete\">"
						+ "<input type=\"hidden\" name=\"employee_id\" value=\"" + rs.getString("employee_id") + "\">"
						+ "<input type = \"submit\" value= \"Delete\"></form></td> </tr>");
						
						
			}
			rs.close();
			cs.close();
			out.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		// end html form
		out.print("</body></html>");

	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String method = request.getParameter("method");
		if(method.equals("delete"))
		{
			System.out.println("DoDelete");
			String employee_id = request.getParameter("employee_id");
			doDelete(employee_id, request, response);
		}
		else if(method.equals("patch"))
		{
			System.out.println("DoPatch");
			String employee_id = request.getParameter("employee_id");
			doPatch(employee_id, request, response);
		}
		else if(method.equals("put"))
		{
			System.out.println("DoPut");
			doPut(request, response);
		}
		else if(method.equals("post"))
		{
			System.out.println("DoPost");
			doPost1(request, response);
		}
		else if(method.equals("htmlform"))
		{
			System.out.println("HTML Form");
			HTMLform(method, "null", request, response);
		}
		else if(method.equals("htmlformupdate"))
		{
			System.out.println("HTML Form");
			String employee_id = request.getParameter("employee_id");
			HTMLform(method, employee_id, request, response);
		}
		else
		{
			System.out.println("DoGet");
			doGet(request, response);
		}
		
	}
	
	protected void doPost1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				// get html form parameters
				String first_name = request.getParameter("first_name");
				String last_name = request.getParameter("last_name");
				String email = request.getParameter("email");
				String phone_number = request.getParameter("phone_number");
				String hire_date = request.getParameter("hire_date");
				String salary = request.getParameter("salary");
		 
				try {
					// create and execute query
					System.out.println("Calling myinsert Procedure");
					conn.setAutoCommit(true);

					CallableStatement cs = conn.prepareCall("{ call servlet_package.myinsert(?,?,?,?,?,?) }");
					cs.setString(1, first_name);
					cs.setString(2, last_name);
					cs.setString(3, email);
					cs.setString(4, phone_number);
					cs.setString(5, hire_date);
					cs.setString(6, salary);
					cs.execute();

					// close objects
					cs.close();
					// I don't close connection because doGet needs to use it next

				} catch (SQLException se) {
					se.printStackTrace();
				}
				// execute doGet
				doGet(request, response);

			}
		
	
	
	protected void doDelete(String employee_id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
				try {
					// create and execute query
					System.out.println("Calling mydelete Procedure");
					conn.setAutoCommit(true);

					CallableStatement cs = conn.prepareCall("{ call servlet_package.mydelete(?) }");
					cs.setString(1, employee_id);
					cs.execute();

					// close objects
					cs.close();

				} catch (SQLException se) {
					se.printStackTrace();
				}
				// execute doGet
				doGet(request, response);
			
	}
	
	protected void doPatch(String employee_id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				// get html form parameters
				String first_name = request.getParameter("first_name");
				String last_name = request.getParameter("last_name");
				String email = request.getParameter("email");
				String phone_number = request.getParameter("phone_number");
				String hire_date = request.getParameter("hire_date");
				String salary = request.getParameter("salary");
		
				try {
					// create and execute query
					System.out.println("Calling mypatch Procedure");
					conn.setAutoCommit(true);
		
					CallableStatement cs = conn.prepareCall("{ call servlet_package.myupdate(?,?,?,?,?,?,?) }");
					cs.setString(1, employee_id);
					cs.setString(2, first_name);
					System.out.println(employee_id);
					cs.setString(3, last_name);
					cs.setString(4, email);
					cs.setString(5, phone_number);
					cs.setString(6, hire_date);
					cs.setString(7, salary);
					cs.execute();

					// close objects
					cs.close();
		
				} catch (SQLException se) {
					se.printStackTrace();
				}
				// execute doGet
				doGet(request, response);
		
		
		
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		
		
	}
	
	protected void HTMLform(String method, String employee_id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(method.equals("htmlformupdate"))
		{
			
			try {
				// create and execute query
				System.out.println("Calling selectrecord Procedure");
				conn.setAutoCommit(true);
	
				CallableStatement cs = conn.prepareCall("{ call servlet_package.selectrecord(?,?) }");
				cs.registerOutParameter(1, OracleTypes.CURSOR);
				cs.setString(2, employee_id);
				cs.execute();
				
				// ResultSet object only works to display data if query returns data
				ResultSet rs = (ResultSet) cs.getObject(1);
				

				while (rs.next()) {
				
				// html back button
				out.print("<form method=\"GET\" action= \"/ServletDemo/servlet_class\" ><input type= \"submit\" value= \"Go Back\" /></form>");
				
				// display database table
				out.print("<!DOCTYPE html><html><head><title>HTML Form</title><body>"
						+ "<form method=\"POST\" action=\"/ServletDemo/servlet_class\">"
						+ "<br> <input type=\"hidden\" name=\"method\" value=\"patch\"><br>"
						+ "<input type=\"hidden\" name=\"employee_id\" value=\"" + rs.getString("employee_id") + "\">"
						+ "First Name:<br> <input type=\"text\" name=\"first_name\" value=\"" + rs.getString("first_name") + "\"><br>"
						+ "Last Name:<br> <input type=\"text\" name=\"last_name\" value=\"" + rs.getString("last_name") + "\"><br>"
						+ "Email:<br> <input type=\"text\" name=\"email\" value=\"" + rs.getString("email") + "\"><br>"
						+ "Phone Number:<br> <input type=\"text\" name=\"phone_number\" value=\"" + rs.getString("phone_number") + "\"><br>"
						+ "Hire Date:<br> <input type=\"text\" name=\"hire_date\" value=\"" + rs.getString("hire_date") + "\"><br>"
						+ "Salary:<br> <input type=\"text\" name=\"salary\" value=\"" + rs.getString("salary") + "\"><br>"
						+ "<input type=\"submit\" value=\"Submit\"></form></body></html>");
				}
				// close objects
				rs.close();
				cs.close();
				out.close();
	
			} catch (SQLException se) {
				se.printStackTrace();
			}
			
			// end of try catch
			
			
		}
		else
		{
			// html back button
			out.print("<form method=\"GET\" action= \"/ServletDemo/servlet_class\" ><input type= \"submit\" value= \"Go Back\" /></form>");
			
			// display database table
			out.print("<!DOCTYPE html><html><head><title>HTML Form</title><body>"
					+ "<form method=\"POST\" action=\"/ServletDemo/servlet_class\">"
					+ "<br> <input type=\"hidden\" name=\"method\" value=\"post\"><br>"
					+ "First Name:<br> <input type=\"text\" name=\"first_name\" value=\"James\"><br>"
					+ "Last Name:<br> <input type=\"text\" name=\"last_name\" value=\"Madison\"><br>"
					+ "Email:<br> <input type=\"text\" name=\"email\" value=\"james@gmail.com\"><br>"
					+ "Phone Number:<br> <input type=\"text\" name=\"phone_number\" value=\"333.444.5555\"><br>"
					+ "Hire Date:<br> <input type=\"text\" name=\"hire_date\" value=\"10-JUN-15\"><br>"
					+ "Salary:<br> <input type=\"text\" name=\"salary\" value=\"45000\"><br><br>"
					+ "<input type=\"submit\" value=\"Submit\"></form></body></html>");
		}
		 
				
	}
	

	public void init(ServletConfig config) throws ServletException {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");
			conn = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
