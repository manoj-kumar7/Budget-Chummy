package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;

@WebServlet("/searchServlet")
public class searchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public searchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		long date = Datehelper.dateToEpoch(request.getParameter("date"));

		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		long userid=-1;
		long accid=-1;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null;
			st = con.createStatement();
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			Object acc_attribute = session.getAttribute("account_id");
			userid = Long.parseLong(String.valueOf(user_attribute));
			accid = Long.parseLong(String.valueOf(acc_attribute));
			String query = "select amount,description from transactions where account_id="+accid+" AND date="+date+" AND transaction_type='income';";
			ResultSet rs=null;
			rs = st.executeQuery(query);
			String description=null;
			int amount=-1;
			JSONArray income_arr = new JSONArray();
			JSONObject income_obj = new JSONObject();
			while(rs.next())
			{
				description=rs.getString("description");
				amount=rs.getInt("amount");
				income_obj.put("description", description);
				income_obj.put("amount", amount);
				income_arr.add(income_obj.toJSONString());
				income_obj.clear();
			}
			query = "select amount,description from transactions where account_id="+accid+" AND date='"+date+"' AND transaction_type='expense';";
			rs=null;
			rs = st.executeQuery(query);
			JSONArray expense_arr = new JSONArray();
			JSONObject expense_obj = new JSONObject();	
			while(rs.next())
			{
				description=rs.getString("description");
				amount=rs.getInt("amount");
				expense_obj.put("description", description);
				expense_obj.put("amount", amount);
				expense_arr.add(expense_obj.toJSONString());
				expense_obj.clear();
			}
			rs.close();
			st.close();
			con.close();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			JSONObject final_obj = new JSONObject();
			final_obj.put("income_data", income_arr);
			final_obj.put("expense_data", expense_arr);
			response.getWriter().print(final_obj.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
