package com.budgetchummy.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetchummy.api.util.ExpenseUtil;

@WebServlet(urlPatterns = {"/api/v1/expense", "/BudgetChummy/api/v1/expense", "/api/v1/expense/edit", "/BudgetChummy/api/v1/expense/edit", "/api/v1/expense/delete", "/BudgetChummy/api/v1/expense/delete"})
public class expenseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public expenseServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/expense") || requestURI.matches("/BudgetChummy/api/v1/expense"))
        {
			ExpenseUtil.getExpenses(request, response);
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/expense") || requestURI.matches("/BudgetChummy/api/v1/expense"))
        {
			ExpenseUtil.addExpense(request, response);
        }
		else if(requestURI.matches("/api/v1/expense/edit") || requestURI.matches("/BudgetChummy/api/v1/expense/edit"))
		{
			ExpenseUtil.editExpense(request, response);
		}
		else if(requestURI.matches("/api/v1/expense/delete") || requestURI.matches("/BudgetChummy/api/v1/expense/delete"))
		{
			ExpenseUtil.deleteExpense(request, response);
		}	
	}

}
