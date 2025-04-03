/*
 * Tyler Meira
 * N01432291
 * Lab 9
 */

package tyler.meira.n01432291.TM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private ArrayList<CourseModal> courseModalArrayList;
    private Context context;


    public CourseAdapter(ArrayList<CourseModal> courseModalArrayList, Context context) {
        this.courseModalArrayList = courseModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModal course = courseModalArrayList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseDesc.setText(course.getCourseDescription());
    }

    @Override
    public int getItemCount() {
        return courseModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Match these IDs to your XML!
            courseName = itemView.findViewById(R.id.tylTVCourse);
            courseDesc = itemView.findViewById(R.id.tylTVCourseDesc);
        }
    }
}
