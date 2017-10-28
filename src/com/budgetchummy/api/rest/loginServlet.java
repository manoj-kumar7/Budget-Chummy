package com.budgetchummy.api.rest;


import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.budgetchummy.api.util.LoginUtil;
/**
 * Servlet implementation class loginServlet
 */
@WebServlet(urlPatterns = {"/api/v1/login", "/BudgetChummy/api/v1/login", "/api/v1/login/google", "/BudgetChummy/api/v1/login/google"})
public class loginServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    			
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/login") || requestURI.matches("/BudgetChummy/api/v1/login"))
        {
			LoginUtil.loginWithBC(request, response);
        }
		else if(requestURI.matches("/api/v1/login/google") || requestURI.matches("/BudgetChummy/api/v1/login/google"))
		{
			LoginUtil.loginWithGoogle(request, response);
		}
	}

}
