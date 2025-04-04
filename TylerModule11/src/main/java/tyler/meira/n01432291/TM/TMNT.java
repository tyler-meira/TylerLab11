package tyler.meira.n01432291.TM;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class TMNT extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private EditText editCourseName,editCourseDesc;
    private Button btnAdd,btnDelete;
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseList;
    private DatabaseReference databaseReference;


    public TMNT() {
        // Required empty public constructor
    }

    public static TMNT newInstance(String param1, String param2) {
        TMNT fragment = new TMNT();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t_m_n_t, container, false);

        editCourseName = view.findViewById(R.id.tylEditName);
        editCourseDesc = view.findViewById(R.id.tylEditDesc);
        btnAdd = view.findViewById(R.id.tylBtnAdd);
        btnDelete = view.findViewById(R.id.tylBtnDelete);
        recyclerView = view.findViewById(R.id.tylCourseRV);

        editCourseName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList,getContext());
        recyclerView.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Courses");

        fetchCourses();

        btnAdd.setOnClickListener(v -> addCourse());
        btnDelete.setOnClickListener(v -> deleteAllCourses());

        return view;
    }


    private void addCourse(){
        String name = editCourseName.getText().toString().trim();
        String desc = editCourseDesc.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name)) {
            editCourseName.setError(getString(R.string.course_name_is_required));
            return;
        }

        if (!isValidCourseName(name)) {
            editCourseName.setError(getString(R.string.invalid_format_use_abcd_1234));
            return;
        }

        if (TextUtils.isEmpty(desc)) {
            editCourseDesc.setError(getString(R.string.course_description_is_required));
            return;
        }

        String id = databaseReference.push().getKey();
        CourseModal course = new CourseModal(id,name,desc);
        databaseReference.child(id).setValue(course);

        editCourseName.setText("");

        editCourseDesc.setText("");

        Toast.makeText(getContext(), getString(R.string.course_added), Toast.LENGTH_SHORT).show();
    }

    private boolean isValidCourseName(String name) {
        return Pattern.matches("^[A-Z]{4}-\\d{3,4}$", name);
    }

    private void deleteAllCourses() {
        if (courseList.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.no_data_to_delete), Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.removeValue();
        courseList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), getString(R.string.all_courses_deleted), Toast.LENGTH_SHORT).show();
    }

    private void fetchCourses(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CourseModal modal = dataSnapshot.getValue(CourseModal.class);
                    courseList.add(modal);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.couldnt_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }



}