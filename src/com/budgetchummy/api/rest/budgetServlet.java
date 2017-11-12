package com.budgetchummy.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetchummy.api.util.BudgetUtil;

@WebServlet(urlPatterns = {"/api/v1/budget", "/BudgetChummy/api/v1/budget","/api/v1/budget/edit", "/BudgetChummy/api/v1/budget/edit","/api/v1/budget/delete", "/BudgetChummy/api/v1/budget/delete"})
public class budgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public budgetServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/budget") || requestURI.matches("/BudgetChummy/api/v1/budget"))
        {
			BudgetUtil.getBudgets(request, response);
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/budget") || requestURI.matches("/BudgetChummy/api/v1/budget"))
        {
			BudgetUtil.addBudget(request, response);
        }
		else if(requestURI.matches("/api/v1/budget/edit") || requestURI.matches("/BudgetChummy/api/v1/budget/edit"))
		{
			BudgetUtil.editBudget(request, response);
		}
		else if(requestURI.matches("/api/v1/budget/delete") || requestURI.matches("/BudgetChummy/api/v1/budget/delete"))
		{
			BudgetUtil.deleteBudget(request, response);
		}
	}

}
