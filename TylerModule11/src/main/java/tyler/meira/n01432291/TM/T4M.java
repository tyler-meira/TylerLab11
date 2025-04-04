package tyler.meira.n01432291.TM;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class T4M extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private String mParam1;
    private String mParam2;

    TextView date;
    TextClock textClock;
    AdView adView;
    Button btnLocation;
    int adCount = 0;
    private boolean isLocationPermissionGranted = false;
    private Snackbar snackbar;
    private LocationManager lm;

    public T4M() {
        // Required empty public constructor
    }

    public static T4M newInstance(String param1, String param2) {
        T4M fragment = new T4M();
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
        View view = inflater.inflate(R.layout.fragment_t4_m, container, false);
        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1);
            }
        }

        // Get views
        date = view.findViewById(R.id.tylDate);
        textClock = view.findViewById(R.id.tylTextClock);
        adView = view.findViewById(R.id.tylAdView);
        btnLocation = view.findViewById(R.id.tylButton);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");

        date.setText(currentDate.format(new Date()));
        textClock.setText(currentTime.format(new Date()));

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adCount++;
                Toast.makeText(getContext(), R.string.tyler_meira + adCount, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(getContext(), R.string.tyler_meira_failed + adCount, Toast.LENGTH_LONG).show();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(getContext(), R.string.tyler_meira_success + adCount, Toast.LENGTH_LONG).show();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLocationPermission()) {
                    getLocation();
                } else {
                    requestLocationPermission();
                }
            }
        });

        return view;
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void getLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(getContext(), getString(R.string.please_enable_location_services), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Location location = null;

            if (isGPSEnabled) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location == null && isNetworkEnabled) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                Snackbar.make(getView(), getString(R.string.location) + lat + getString(R.string.comma) + lon, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Do Nothing
                            }
                        })
                        .show();
                notification(lat, lon);
            } else {
                Toast.makeText(getContext(), getString(R.string.no_location_available_try_moving_to_get_a_fix), Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.location_permission_needed))
                    .setMessage(getString(R.string.this_app_needs_location_permission_to_show_your_current_location))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                    },
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
                Toast.makeText(getContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                getLocation();
            } else {
                snackbar = Snackbar.make(getView(),
                                getString(R.string.location_permission_required_for_this_feature),
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, v -> {
                            // Open app settings when user taps SETTINGS
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            snackbar.dismiss();
                        })
                        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.red));

                snackbar.show();
            }
        }
    }

    public void notification(String lat, String lon) {
        // Create channel (should be done in onCreate)
        createNotificationChannel();

        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "LocationChannel",
                    "Location Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.shows_your_current_location));
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{500, 500});

            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "LocationChannel")
                .setSmallIcon(R.drawable.pin)
                .setContentTitle(getString(R.string.current_location))
                .setContentText(String.format((getString(R.string.lat_s_lon_s)), lat, lon))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
                nm.getNotificationChannel("LocationChannel").canBypassDnd()) {
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{500, 500});
        }

        nm.notify(1, builder.build()); // Using different ID than 0
    }

    private void createNotificationChannel() {

    }
}