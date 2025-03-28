package tyler.meira.n01432291.TM;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MeiraActivity10 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Fragments

        Ty1ler tyler = new Ty1ler();
        Me2ira meira = new Me2ira();

        setCurrentFragment(tyler);

        BottomNavigationView bottomNavigationView = findViewById(R.id.tylBottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.Tyler){
                setCurrentFragment(tyler);
            }
            if(item.getItemId() == R.id.Meira){
                setCurrentFragment(meira);
            }
            if(item.getItemId() == R.id.N01432291){
                //setCurrentFragment(personFrag);
            }
            if(item.getItemId() == R.id.TM){
                //setCurrentFragment(settingsFrag);

            }


            return true;
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }

        return false;
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tylFragment, fragment)
                .commit();
    }

}