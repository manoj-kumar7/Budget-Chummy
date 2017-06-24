package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet("/accountChosenServlet")
public class accountChosenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public accountChosenServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		long acc_id = Long.parseLong(request.getParameter("account_id"));
		String page_name = request.getParameter("page_name");
		HttpSession session = request.getSession();
		session.setAttribute("account_id",acc_id);
		String homeurl = new String("http://localhost:8080/BudgetChummy/home.jsp?page='"+page_name+"'");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", homeurl);		

	}

}
