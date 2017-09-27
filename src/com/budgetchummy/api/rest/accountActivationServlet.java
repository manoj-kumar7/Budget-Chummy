package com.budgetchummy.api.rest;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import com.budgetchummy.api.util.APIConstants;


@WebServlet(urlPatterns = {"/api/v1/activateAccount", "/BudgetChummy/api/v1/activateAccount"})
public class accountActivationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public accountActivationServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			String code = request.getParameter("code");
			String email = request.getParameter("email");
			boolean is_verified = false;
			long user_id = -1;
			boolean cantActivate = false;

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			
			try {
				Connection con = null;
				con = DriverManager.getConnection(url,user,mysql_password);
				PreparedStatement st=null;
				ResultSet rs = null;

				st = con.prepareStatement("select user_id, verified from users where activation_code=? AND email=?;");
				st.setString(1, code);
				st.setString(2, email);
				rs = st.executeQuery();
				if(rs.next())
				{
					is_verified = rs.getBoolean("verified");
					user_id = rs.getLong("user_id");
					rs = null;
					if(is_verified)
					{
						cantActivate = true;
					}
				}
				else
				{
					cantActivate = true;
				}

				if(cantActivate)
				{
					response.setStatus(400);
				}
				else
				{
					st = con.prepareStatement("update users set verified=true where user_id=?");
					st.setLong(1, user_id);
					int i = st.executeUpdate();
				}

				
				if(rs != null)
				{
					rs.close();
					st.close();
				}
				con.close();	
			}catch (SQLException e) {
				e.printStackTrace();
			}	

	}

}
