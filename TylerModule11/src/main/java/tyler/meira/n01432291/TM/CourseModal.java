/*
 * Tyler Meira
 * N01432291
 * Lab 9
 */

package tyler.meira.n01432291.TM;

public class CourseModal {

    // variables for our course
    // name and description.
    private String courseName;
    private String courseDescription;
    private String id;

    // creating constructor for our variables.
    public CourseModal(String id, String courseName, String courseDescription) {
        this.id = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
    public CourseModal() {}

    // creating getter and setter methods.
    public String getCourseName() {
        return courseName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}

