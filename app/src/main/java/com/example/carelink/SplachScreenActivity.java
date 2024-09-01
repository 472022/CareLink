package com.example.carelink;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplachScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // Duration of the splash screen (3 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);

        ImageView logo = findViewById(R.id.logoImageView);
        TextView t = findViewById(R.id.textView26);

        // Create fade-in animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500); // Duration of the animation (1.5 seconds)
        logo.startAnimation(fadeIn);
        t.startAnimation(fadeIn);

        // Handler to transition to the next activity after the splash duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplachScreenActivity.this, login.class);
                startActivity(mainIntent);
                finish(); // Finish the splash screen activity
            }
        }, SPLASH_DISPLAY_LENGTH);
}
}