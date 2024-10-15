package ensa.ma.starsapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);

        // Ajouter des animations
        //logo.animate().rotation(360f).setDuration(2000);
        //logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000);
        //logo.animate().translationYBy(1000f).setDuration(2000);
        //logo.animate().alpha(0f).setDuration(6000);

        logo = findViewById(R.id.logo);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        rotation.setDuration(2000);
        ObjectAnimator scale = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f);
        scale.setDuration(3000);
        scaleY.setDuration(3000);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(logo, "translationY", 1000f);
        translationY.setDuration(2000);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(logo, "alpha", 0f);
        fadeOut.setDuration(6000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(rotation, scale, scaleY, translationY, fadeOut);
        animatorSet.start();


        // Redirection après 5 secondes
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(SplashActivity.this, ListActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
