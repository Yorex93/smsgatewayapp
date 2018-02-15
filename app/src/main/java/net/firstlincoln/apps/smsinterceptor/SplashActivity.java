package net.firstlincoln.apps.smsinterceptor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView tv;

    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv = findViewById(R.id.welcome_header);

        im = findViewById(R.id.welcome_image);

        TextView tvVersion = findViewById(R.id.welcome_version);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        tv.startAnimation(animation);

        im.startAnimation(animation);

        tvVersion.startAnimation(animation);

        final Intent intent = new Intent(this, MainActivity.class);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();
    }
}
