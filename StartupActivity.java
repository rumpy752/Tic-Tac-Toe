/*
    date: 2023-06-25
    author: Jaime Rump
    file: StartupActivity.java
    desc: This class provides necessary code for startup screen (launch activity)
 */
package com.example.tictoctoe;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class StartupActivity extends AppCompatActivity {
    private static final long DELAY_TIME = 5000; // Delay time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        // Sets the current date and time
        TextView date = findViewById(R.id.textView2);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        date.setText(timestamp);

        // Handles delayed transition to main activity
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Start the second activity
            Intent intent = new Intent(StartupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_TIME);
    }
}