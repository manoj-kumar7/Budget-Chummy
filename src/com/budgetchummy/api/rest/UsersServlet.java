package com.budgetchummy.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetchummy.api.util.UsersUtil;


@WebServlet(urlPatterns = {"/api/v1/getUsers", "/BudgetChummy/api/v1/getUsers", "/api/v1/addUser", "/BudgetChummy/api/v1/addUser", "/api/v1/deleteUser", "/BudgetChummy/api/v1/deleteUser", "/api/v1/joinAccount", "/BudgetChummy/api/v1/joinAccount"})
public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public UsersServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/getUsers") || requestURI.matches("/BudgetChummy/api/v1/getUsers"))
        {
			UsersUtil.getUsers(request, response);
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/addUser") || requestURI.matches("/BudgetChummy/api/v1/addUser"))
        {
			UsersUtil.addUser(request, response);
        }
		else if(requestURI.matches("/api/v1/joinAccount") || requestURI.matches("/BudgetChummy/api/v1/joinAccount"))
		{
			UsersUtil.joinAccount(request, response);
		}
		else if(requestURI.matches("/api/v1/deleteUser") || requestURI.matches("/BudgetChummy/api/v1/deleteUser"))
		{
			UsersUtil.deleteUser(request, response);
		}
	}

}
