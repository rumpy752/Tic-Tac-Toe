/*
    date: 2023-06-25
    author: Jaime Rump
    file: MainActivity.java
    desc: This class provides necessary code for the main activity
 */

package com.example.tictoctoe;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    protected Intent intent;
    protected TextView welcome_message;
    protected String receivedPlayer1Name;
    protected String receivedPlayer2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        }

        // Retrieves string array from strings.xml and loads them into a list view for the main menu
        String[] options = getResources().getStringArray(R.array.main_menu);
        ListView listView = findViewById(R.id.listview_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, EnterNameActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, PlayActivity.class);
                        intent.putExtra("player1", receivedPlayer1Name);
                        intent.putExtra("player2", receivedPlayer2Name);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, StandingsActivity.class);
                        intent.putExtra("player1", receivedPlayer1Name);
                        intent.putExtra("player2", receivedPlayer2Name);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        welcome_message = findViewById(R.id.textview_welcome);
    }

    // Retrieves the names entered in a different activity and displays them to the user
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        receivedPlayer1Name = intent.getStringExtra("player1_name");
        receivedPlayer2Name = intent.getStringExtra("player2_name");

        if (receivedPlayer1Name != null && receivedPlayer2Name != null) {
            welcome_message.setText("Welcome, " + receivedPlayer1Name + " and " + receivedPlayer2Name + "!");
        } else if (receivedPlayer1Name != null) {
            welcome_message.setText("Welcome, " + receivedPlayer1Name +"!");
        }
    }
}
