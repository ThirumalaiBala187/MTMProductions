package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DTO.Courses;

/**
 * Servlet implementation class CourseServlet
 */
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        @SuppressWarnings("unchecked")
		List<Courses> allCourses = (List<Courses>) getServletContext().getAttribute("Courses");
        		
        	StringBuilder json = new StringBuilder("[");
        	for(int i=0;i<allCourses.size();i++) {
        		Courses course = allCourses.get(i);
        		json.append("{\"name\":\"").append(course.getCourseName()).append("\",").append("\"duration\":\"").append(course.getDurationWeeks()).append("\",").append("\"ratings\":\"").append(course.getRatings()).append("\",").append("\"learners\":\"").append(course.getLearnersCount()).append("\",").append("\"genre\":\"").append(course.getGenreString()).append("\",").append("\"url\":\"").append(course.getImageUrl()).append("\"}");
        		if(i<allCourses.size()-1) json.append(",");
        	}
        	json.append("]");
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	response.getWriter().write(json.toString());

        	
	    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
