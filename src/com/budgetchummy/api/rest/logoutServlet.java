package com.budgetchummy.api.rest;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/logout", "/BudgetChummy/logout"})
public class logoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public logoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(session == null)
		{
			response.sendRedirect("login.jsp");
		}
		session.removeAttribute("useremail");
		session.removeAttribute("user_id");
		session.removeAttribute("account_id");
		session.invalidate();
//		response.sendRedirect("FirstPage.jsp");
	}

}
