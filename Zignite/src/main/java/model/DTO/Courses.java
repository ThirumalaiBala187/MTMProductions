package model.DTO;

public class Courses {
	
	int courseId;
	String courseName;
	int durationWeeks;
	double ratings;
	int learnersCount;
	String genreString;
	String imageUrl;
	public Courses(int courseId, String courseName, int durationWeeks, double ratings, int learnersCount, String genreString, String imageUrl) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.durationWeeks = durationWeeks;
		this.ratings = ratings;
		this.learnersCount = learnersCount;
		this.genreString = genreString;
		this.imageUrl = imageUrl;
	}
	
	public int getCourseId() {
		return courseId;
	}
	public void setUserId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getDurationWeeks() {
		return durationWeeks;
	}
	public void setDurationWeeks(int durationWeeks) {
		this.durationWeeks = durationWeeks;
	}
	public double getRatings() {
		return ratings;
	}
	public void setRatings(double ratings) {
		this.ratings = ratings;
	}
	public int getLearnersCount() {
		return learnersCount;
	}
	public void setLearnersCount(int learnersCount) {
		this.learnersCount = learnersCount;
	}
	public String getGenreString() {
		return genreString;
	}
	public void setGenreString(String genreString) {
		this.genreString = genreString;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}	
	
}