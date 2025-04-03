/*
 * Tyler Meira
 * N01432291
 * Lab 9
 */

package tyler.meira.n01432291.TM;

public class VideoModal {

    // variables for our course
    // name and description.
    private String courseDescription;

    // creating constructor for our variables.
    public VideoModal(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    // creating getter and setter methods.

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}

