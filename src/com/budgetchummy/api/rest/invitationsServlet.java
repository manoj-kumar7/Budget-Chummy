package com.budgetchummy.api.rest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class invitationsServlet
 */
@WebServlet(urlPatterns = {"/api/v1/invitations", "/BudgetChummy/api/v1/invitations"})
public class invitationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public invitationsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
