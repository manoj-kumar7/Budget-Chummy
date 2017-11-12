package com.budgetchummy.api.rest;


import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.budgetchummy.api.util.IncomeUtil;


@WebServlet(urlPatterns = {"/api/v1/income", "/BudgetChummy/api/v1/income", "/api/v1/income/edit", "/BudgetChummy/api/v1/income/edit", "/api/v1/income/delete", "/BudgetChummy/api/v1/income/delete"})
public class incomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public incomeServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/income") || requestURI.matches("/BudgetChummy/api/v1/income"))
        {
			IncomeUtil.getIncomes(request, response);
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.matches("/api/v1/income") || requestURI.matches("/BudgetChummy/api/v1/income"))
        {
			IncomeUtil.addIncome(request, response);
        }
		else if(requestURI.matches("/api/v1/income/edit") || requestURI.matches("/BudgetChummy/api/v1/income/edit"))
		{
			IncomeUtil.editIncome(request, response);
		}
		else if(requestURI.matches("/api/v1/income/delete") || requestURI.matches("/BudgetChummy/api/v1/income/delete"))
		{
			IncomeUtil.deleteIncome(request, response);
		}
	}

}
