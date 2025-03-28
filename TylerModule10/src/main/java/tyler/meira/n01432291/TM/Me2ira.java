package tyler.meira.n01432291.TM;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Me2ira extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String url;
    boolean isCelsius;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    TextView details;
    TextView temp;


    public Me2ira() {
        // Required empty public constructor
    }

    public static Me2ira newInstance(String param1, String param2) {
        Me2ira fragment = new Me2ira();
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
        View view = inflater.inflate(R.layout.fragment_me2ira, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        RadioGroup radioGroup = view.findViewById(R.id.rg);
        Spinner spinner = view.findViewById(R.id.spinner2);
        details = view.findViewById(R.id.txtDetails);
        temp = view.findViewById(R.id.txtTemperature);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.countries,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        isCelsius = sharedPreferences.getBoolean("isCelsius", true);
        if(isCelsius){
            radioGroup.check(R.id.radioCelsius);
        }
        else{
            radioGroup.check(R.id.radioFahrenheit);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCelsius) {
                editor.putBoolean("isCelsius", true);
                isCelsius = true;
                editor.apply();
            }
            if (checkedId == R.id.radioFahrenheit) {
                editor.putBoolean("isCelsius", false);
                isCelsius = false;
                editor.apply();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getSelectedItem().toString().equals("Toronto")){
                    weather("43.6532","-79.3832");
                }else if(parent.getSelectedItem().toString().equals("Amesterdam")){
                    weather("52.3676", "4.9041");
                }else if(parent.getSelectedItem().toString().equals("Lisbon")){
                    weather("38.7169", "-9.1395");
                }else if(parent.getSelectedItem().toString().equals("New York City")){
                    weather("40.7128", "-74.0060");
                }else if(parent.getSelectedItem().toString().equals("Tokyo")){
                    weather("35.6762", "139.6503");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return view;
    }

    private void weather(String longitude, String lattitude){

        url = "https://api.openweathermap.org/data/2.5/weather?lat="+longitude+"&lon="+lattitude+"&units=metric&appid=6b7a08aa941fe519faad839637ee99a8";

        Log.d("URL",url);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonObject = downloadJson(url);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayWeatherInfo(jsonObject);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String downloadJson(String link) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder bufferReader = null;
        try {
            url = new URL(link);
            bufferReader = new StringBuilder();
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream content = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    bufferReader.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert httpURLConnection != null;
            httpURLConnection.disconnect();
        }
        return bufferReader.toString();
    }

    private void displayWeatherInfo(String jsonObjectResult) {
        try {
            JSONObject weatherJson = new JSONObject(jsonObjectResult);

            JSONArray dataArray1 = weatherJson.getJSONArray("weather");
            JSONObject coordObject = weatherJson.getJSONObject("coord");
            JSONObject sysObject = weatherJson.getJSONObject("sys");
            JSONObject mainObject = weatherJson.getJSONObject("main");

            String strResults = "";

            strResults += "Lon: " + coordObject.getDouble("lon");
            strResults += "\nLat: " + coordObject.getDouble("lat");
            strResults += "\nCountry: " + sysObject.getString("country");
            strResults += "\nCity: " + weatherJson.getString("name");
            strResults += "\nHumidity: " + mainObject.getInt("humidity");

            for (int i = 0; i < dataArray1.length(); i++) {
                JSONObject jsonObject = dataArray1.getJSONObject(i);
                strResults += "\nDescription: " + jsonObject.getString("description");
            }
            if(isCelsius){
                temp.setText("Temperature: " + round(mainObject.getDouble("temp")));
            }else{
                temp.setText("Temperature: " + round(celsiusToFahrenheit(mainObject.getDouble("temp"))));
            }
            details.setText(strResults);
        } catch (Exception e) {
            Toast.makeText(getContext(), "WeatherError: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private double celsiusToFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }
    private double round(double num){
        return Math.round(num * Math.pow(10,1)) / Math.pow(10,1);
    }

}