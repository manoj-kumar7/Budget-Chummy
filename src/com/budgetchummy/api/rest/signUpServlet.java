package com.budgetchummy.api.rest;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetchummy.api.util.SignUpUtil;


/**
 * Servlet implementation class signUpServlet
 */
@WebServlet(urlPatterns = {"/api/v1/signUp", "/BudgetChummy/api/v1/signUp", "/api/v1/signUp/google", "/BudgetChummy/api/v1/signUp/google"})
public class signUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public signUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/signUp") || requestURI.matches("/BudgetChummy/api/v1/signUp"))
        {
			SignUpUtil.signUpWithBC(request, response);
        }
		else if(requestURI.matches("/api/v1/signUp/google") || requestURI.matches("/BudgetChummy/api/v1/signUp/google"))
		{
			SignUpUtil.signUpWithGoogle(request, response);
		}
	}

}
