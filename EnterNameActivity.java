/*
    date: 2023-06-25
    author: Jaime Rump
    file: EnterNameActivity.java
    desc: This class provides necessary code for the enter name activity
 */

package com.example.tictoctoe;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class EnterNameActivity extends AppCompatActivity {
    protected Button buReturn;
    protected Button buSave;
    protected EditText player1_name, player2_name;
    protected boolean flag;
    protected LinearLayout linearLayout;
    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.enter_names_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.enter_names);
        }

        buReturn = findViewById(R.id.buReturn);
        buReturn.setOnClickListener(view -> {
            intent = new Intent(EnterNameActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Passes the saved names back to the main activity
        buSave = findViewById(R.id.buSave);
        buSave.setOnClickListener(view -> {
            String player1Name = player1_name.getText().toString().trim();
            intent = new Intent(EnterNameActivity.this, MainActivity.class);
            if (!player1Name.isEmpty()) {
                if (!flag) {
                    intent.putExtra("player1_name", player1Name);
                    startActivity(intent);
                    player2_name.setText("");
                } else {
                    String player2Name = player2_name.getText().toString().trim();
                    intent.putExtra("player1_name", player1Name);
                    intent.putExtra("player2_name", player2Name);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Player 1 name cannot be blank! ", Toast.LENGTH_SHORT).show();
            }
        });

        // Determines the layout depending on number of players
        linearLayout = findViewById(R.id.player2_layout);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {
                    String checkedText = checkedRadioButton.getText().toString();
                    if (checkedText.equals("One Player")) {
                        flag = false;
                        linearLayout.setVisibility(View.INVISIBLE);
                    } else {
                        flag = true;
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        player1_name = findViewById(R.id.input_player1);
        player2_name = findViewById(R.id.input_player2);
    }
}
