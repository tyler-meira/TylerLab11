package tyler.meira.n01432291.TM;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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
    private WebView webView;

    private VideoAdapter adapter;
    private ArrayList<VideoModal> videoDesc;

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

        courseRV = view.findViewById(R.id.tylRecycle1);
        webView = view.findViewById(R.id.tylWebView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);


        if (courseRV == null) {
            throw new NullPointerException(getString(R.string.recyclerview_not_found_check_xml_id));
        }

        fillArrayList();
        buildRecyclerView();

        // Add the OnItemTouchListener here to detect clicks
        courseRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Detect if a touch event is a click (use GestureDetector)
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = rv.getChildAdapterPosition(childView);

                        if(position == 0) {
                            webView.loadUrl("https://www.youtube.com/embed/AF8d72mA41M?si=ojoifn9zhz5QqU8r");
                        }else if(position == 1) {
                            webView.loadUrl("https://www.youtube.com/embed/iSNsgj1OCLA?si=mxQkPoTN_xAnjN26");
                        }else if(position == 2) {
                            webView.loadUrl("https://www.youtube.com/embed/w5ebcowAJD8?si=3cuuWxuRWOQHtWgC");
                        }


                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Not needed for clicks but required by interface
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // Not needed for clicks but required by interface
            }
        });

        return view;
    }

    private void fillArrayList() {
        videoDesc = new ArrayList<>();
        String[] descriptions = getResources().getStringArray(R.array.Videos);

        if (descriptions.length == 0) {
            throw new IllegalStateException(getString(R.string.r_array_videos_is_empty_check_strings_xml));
        }

        for (String desc : descriptions) {
            videoDesc.add(new VideoModal(desc));
        }
    }

    private void buildRecyclerView() {
        adapter = new VideoAdapter(videoDesc, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }
}
