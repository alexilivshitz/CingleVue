package cinglevue.controller;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinglevue.model.DBManager;
import cinglevue.model.MongoDBManager;



public class CingleVueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * doGet sends a response to the browser with the schools list.
	 * If the action was delete or update it will perform it before retrieving the list.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equals("delete")) {
			deleteSchool(request); 
		} else if (action.equals("update")) {
			updateSchool(request);
		} else if (action.equals("new")) {
			addNew(request);
		}
		response.setContentType("text/html");
		response.getWriter().write(findAllSchools());
		
	}

	/*
	 * Add new school if this code does not exist.
	 */
	private void addNew(HttpServletRequest request) {
		DBManager dbManager = new MongoDBManager();
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String emailDomain = request.getParameter("emailDomain");
		dbManager.addNew(code, name, address, emailDomain);	
	}

	/*
	 * updateSchool updates a school with a specific code with new values for all fields (except code).
	 */
	private void updateSchool(HttpServletRequest request) {
		DBManager dbManager = new MongoDBManager();
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String emailDomain = request.getParameter("emailDomain");
		dbManager.updateSchool(code, name, address, emailDomain);
	}

	/*
	 * deleteSchool deletes a school with a specific code
	 */
	private void deleteSchool(HttpServletRequest request) {
		String code = request.getParameter("code");
		if (code != null) { 
			MongoDBManager dbManager = new MongoDBManager();
			dbManager.deleteSchool(code);
		}
	}

	/*
	 * Retrieves the collection of all schools from the database
	 */
	private String findAllSchools() {
		DBManager dbManager = new MongoDBManager();
		return dbManager.findAllSchools();
	}
}
