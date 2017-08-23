package com.budgetchummy.api.rest;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/budgetServlet")
public class budgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public budgetServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String page_name = request.getParameter("page_name");
		int budget_type = Integer.parseInt(request.getParameter("budget-type"));
		long tag_id=-1;
		if(budget_type == 1)
		{
			tag_id = Long.parseLong(request.getParameter("tag_id"));
		}
		int budget_repeat = Integer.parseInt(request.getParameter("budget-repeat"));
		String start_date=null,end_date=null;
		long start=0,end=0;
	    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	    Date dateobj = new Date();
	    df.setTimeZone(TimeZone.getTimeZone("IST"));
	    start_date = df.format(dateobj);
	    end_date = df.format(dateobj);
		if(budget_repeat == 0)
		{
			start_date = request.getParameter("budget-start-date");
			end_date = request.getParameter("budget-end-date");
		}
		else if(budget_repeat == 2)
		{
			end_date = Datehelper.addDaysToDate(start_date, 6);
		}
		else if(budget_repeat == 3)
		{
			end_date = Datehelper.endOfMonth(start_date);
		}
		else if(budget_repeat == 4)
		{
			end_date = Datehelper.endOfYear(start_date);
		}
		start = Datehelper.dateToEpoch(start_date);
		end = Datehelper.dateToEpoch(end_date);

		float budget_amount = Float.parseFloat(request.getParameter("budget-amount"));
		String budget_description = request.getParameter("budget-description");
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null;
			st = con.createStatement();
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			Object acc_attribute = session.getAttribute("account_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			long accid = Long.parseLong(String.valueOf(acc_attribute));
			String query = "insert into budget(account_id,budget_type,tag_id,repeat_period,amount,description,added_by,start_date,end_date) values("+accid+","+budget_type+","+tag_id+","+budget_repeat+","+budget_amount+",'"+budget_description+"',"+userid+","+start+","+end+");";
			
			st.executeUpdate(query);
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("home.jsp?page='"+page_name+"'");
		
	}

}
