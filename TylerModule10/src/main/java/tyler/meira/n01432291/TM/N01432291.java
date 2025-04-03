package tyler.meira.n01432291.TM;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class N01432291 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public N01432291() {
        // Required empty public constructor
    }

    private RecyclerView courseRV;

    private CourseAdapter adapter;
    private ArrayList<CourseModal> videoDesc;

    public static N01432291 newInstance(String param1, String param2) {
        N01432291 fragment = new N01432291();
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
        View view = inflater.inflate(R.layout.fragment_n01432291, container, false);

        courseRV = view.findViewById(R.id.recycleView);

        if (courseRV == null) {
            throw new NullPointerException("RecyclerView not found. Check XML ID.");
        }

        fillArrayList();
        buildRecyclerView();

        return view;
    }

    private void fillArrayList() {
        videoDesc = new ArrayList<>();
        String[] descriptions = getResources().getStringArray(R.array.Videos);

        if (descriptions.length == 0) {
            throw new IllegalStateException("R.array.Videos is empty. Check strings.xml.");
        }

        for (String desc : descriptions) {
            videoDesc.add(new CourseModal(desc));
        }
    }

    private void buildRecyclerView() {
        adapter = new CourseAdapter(videoDesc, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }
}
