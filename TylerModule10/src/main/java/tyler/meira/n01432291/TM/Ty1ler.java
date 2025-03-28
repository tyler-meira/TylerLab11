package tyler.meira.n01432291.TM;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ty1ler extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Ty1ler() {
        // Required empty public constructor
    }

    public static Ty1ler newInstance(String param1, String param2) {
        Ty1ler fragment = new Ty1ler();
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
        View view = inflater.inflate(R.layout.fragment_ty1ler, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        Button btnDownload = view.findViewById(R.id.btnDownload);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.spinnerChoices,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner.getSelectedItem().toString().equals("Pikachu")){
                    loading(view);
                    setImage("https://preview.redd.it/a-pikachu-i-drew-for-a-deviantart-event-v0-s45ljvl87za81.jpg?auto=webp&s=e3f5d369b137d7a731e92ef9c739f8aa32ab3c0e",view);
                }
                if(spinner.getSelectedItem().toString().equals("Charmander")){
                    loading(view);
                    setImage("https://d.newsweek.com/en/full/1653395/pokemon-go-charmander-community-day-2020.jpg?w=1600&h=1600&l=37&t=22&q=88&f=ef11e70682f86d078d7438316f3704ff",view);
                }
                if(spinner.getSelectedItem().toString().equals("Buizel")){
                    loading(view);
                    setImage("https://i.pinimg.com/736x/f1/19/0c/f1190c31b90d6e62bc5c99eea2d5d948.jpg",view);
                }
                if(spinner.getSelectedItem().toString().equals("Venasaur")){
                    loading(view);
                    setImage("https://lozshop.b-cdn.net/wp-content/uploads/2019/11/CHAKRA-6606-Medium-Pokemon-Venusaur.png",view);
                }

            }
        });


        return view;
    }

    public void setImage(String link, View view) {
        executorService.execute(() -> {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    view.post(() -> {
                        ImageView imageView = view.findViewById(R.id.imageView);
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(view.getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.e("setImage", "Error loading image", e);
            }
        });
    }

    public void loading(View view){
        ImageView imageView = view.findViewById(R.id.imageView);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        imageView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }, 5000);




    }


}