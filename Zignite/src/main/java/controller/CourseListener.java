package controller;

import java.util.List;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.DAO.CourseDAO;
import model.DTO.Courses;

/**
 * Application Lifecycle Listener implementation class CourseListener
 *
 */
@WebListener
public class CourseListener implements ServletContextListener, ServletContextAttributeListener {

    /**
     * Default constructor. 
     */
    public CourseListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
	public void contextDestroyed(ServletContextEvent event) {
		event.getServletContext().removeAttribute("Courses");
	}

	/**
     * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
		
		List<Courses> availableCourses = CourseDAO.getCourses();
		event.getServletContext().setAttribute("Courses", availableCourses);
		
	}
	
}
