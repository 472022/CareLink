package com.example.carelink;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.carelink.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private boolean isFabMenuOpen = false;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final String AMBULANCE_NUMBER = "tel:8857845418fv";
    private static final String police = "tel:8857845418fv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new fragment_home());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new fragment_home());
                    break;
                case R.id.shorts:
                    replaceFragment(new fragment_shorts());
                    break;
                case R.id.subscriptions:
                    replaceFragment(new fragment_subscription());
                    break;
                case R.id.library:
                    replaceFragment(new fragment_library());
                    break;
            }
            return true;
        });


        // FloatingActionButtons
        FloatingActionButton fabMain = findViewById(R.id.fab_main);
        FloatingActionButton fabOption1 = findViewById(R.id.fab_option1);
        FloatingActionButton fabOption2 = findViewById(R.id.fab_option2);
        fabMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isFabMenuOpen) {
                    closeFabMenu(fabOption1, fabOption2);
                } else {
                    openFabMenu(fabOption1, fabOption2);
                }
                isFabMenuOpen = !isFabMenuOpen;
                return false;
            }
        });

        fabOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialAmbulance();
            }
        });
        fabOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void openFabMenu(FloatingActionButton fabOption1, FloatingActionButton fabOption2) {
        fabOption1.setVisibility(View.VISIBLE);
        fabOption2.setVisibility(View.VISIBLE);
        fabOption1.animate().translationX(25).translationY(-150).alpha(1.0f).setDuration(300).start();
        fabOption2.animate().translationX(25).translationY(-90).alpha(1.0f).setDuration(300).start();


    }

    private void closeFabMenu(FloatingActionButton fabOption1, FloatingActionButton fabOption2) {
        fabOption1.animate().translationX(0).translationY(0).alpha(0.0f).setDuration(300).withEndAction(() -> fabOption1.setVisibility(View.GONE)).start();
        fabOption2.animate().translationX(0).translationY(0).alpha(0.0f).setDuration(300).withEndAction(() -> fabOption2.setVisibility(View.GONE)).start();
    }


    private void dialAmbulance() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCalll();
        }
    }
    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }
    }

    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(AMBULANCE_NUMBER));
        startActivity(callIntent);
    }
    private void startCalll() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(police));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}