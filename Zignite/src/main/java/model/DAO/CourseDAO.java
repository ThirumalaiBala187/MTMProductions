package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DTO.Courses;

public class CourseDAO {

	public CourseDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<Courses> getCourses() {
		List<Courses> availableCourses = new ArrayList<>();
		try(Connection connection = Database.getConnection()) {			
			String query = "Select * from Course";	
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();			
			while (resultSet.next()) {
				availableCourses.add(new Courses(resultSet.getInt("Course_Id"), resultSet.getString("Course_Name"),resultSet.getInt("Duration"), resultSet.getInt("Ratings"), resultSet.getInt("Learners"), resultSet.getString("Genre"), resultSet.getString("Image_URL")));
			}
			return availableCourses;
		}
		
		catch (SQLException e) {			
			System.out.println(e.getMessage());			
		}		
		return null;
		
	}

}