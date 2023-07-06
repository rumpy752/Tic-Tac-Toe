/*
    date: 2023-06-25
    author: Jaime Rump
    file: StandingsActivity.java
    desc: This class provides necessary code for the standings (scoreboard) activity
 */

package com.example.tictoctoe;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.text.Html;

public class StandingsActivity extends AppCompatActivity {
    protected Intent intent;
    protected Button buReturn;
    protected TextView player1, player2, player3, player4, player5;
    protected TextView player1_score, player2_score, player3_score, player4_score, player5_score;
    protected List<String> lines;
    protected String receivedPlayer1Name;
    protected String receivedPlayer2Name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.standings_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.standings);
        }

        buReturn = findViewById(R.id.buReturn);
        buReturn.setOnClickListener(view -> {
            intent = new Intent(StandingsActivity.this, MainActivity.class);
            intent.putExtra("player1_name", receivedPlayer1Name);
            intent.putExtra("player2_name", receivedPlayer2Name);
            startActivity(intent);
        });

        player1 = findViewById(R.id.player_1);
        player2 = findViewById(R.id.player_2);
        player3 = findViewById(R.id.player_3);
        player4 = findViewById(R.id.player_4);
        player5 = findViewById(R.id.player_5);

        player1_score = findViewById(R.id.player_1_score);
        player2_score = findViewById(R.id.player_2_score);
        player3_score = findViewById(R.id.player_3_score);
        player4_score = findViewById(R.id.player_4_score);
        player5_score = findViewById(R.id.player_5_score);

        intent = getIntent();
        receivedPlayer1Name = intent.getStringExtra("player1");
        receivedPlayer2Name = intent.getStringExtra("player2");
    }

    public void onResume() {
        super.onResume();
        String fileName = "player_data.txt";
        StringBuilder stringBuilder = new StringBuilder();
        lines = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                stringBuilder.append(line).append("\n");
            }
            br.close();
            isr.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int linesToRetrieve = Math.min(lines.size(), 5);
        int startIndex = lines.size() - linesToRetrieve;
        List<String> sortedLines = new ArrayList<>(lines.subList(startIndex, lines.size()));

        // Sort the lines based on the timestamp in descending order
        Collections.sort(sortedLines, (line1, line2) -> {
            String timestamp1 = line1.split(",")[2];
            String timestamp2 = line2.split(",")[2];

            LocalDateTime dateTime1 = LocalDateTime.parse(timestamp1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime dateTime2 = LocalDateTime.parse(timestamp2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return dateTime2.compareTo(dateTime1);
        });

        int count = 0;
        for (String line : sortedLines) {
            String[] playerData = line.split(",");
            String playerName = playerData[0];
            String playerScore = (playerData.length > 1) ? playerData[1] : "";
            String timestamp = (playerData.length > 2) ? playerData[2] : "";

            if (!playerName.equals("Android")) {
                String formattedText = "<b>" + playerName + "</b>" + ": <br>" + timestamp;
                if (count == 0) {
                    player1.setText(Html.fromHtml(formattedText));
                    player1_score.setText(playerScore);
                } else if (count == 1) {
                    player2.setText(Html.fromHtml(formattedText));
                    player2_score.setText(playerScore);
                } else if (count == 2) {
                    player3.setText(Html.fromHtml(formattedText));
                    player3_score.setText(playerScore);
                } else if (count == 3) {
                    player4.setText(Html.fromHtml(formattedText));
                    player4_score.setText(playerScore);
                } else if (count == 4) {
                    player5.setText(Html.fromHtml(formattedText));
                    player5_score.setText(playerScore);
                }
                count++;
            }
        }
    }

    public void restoreEvent(View view) {
        String fileName = "player_data.txt";

        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        for (String line : lines) {
                if (count == 0) {
                    player1.setText("Player 1");
                    player1_score.setText("");
                } else if (count == 1) {
                    player2.setText("Player 2");
                    player2_score.setText("");
                } else if (count == 2) {
                    player3.setText("Player 3");
                    player3_score.setText("");
                } else if (count == 3) {
                    player4.setText("Player 4");
                    player4_score.setText("");
                } else if (count == 4) {
                    player5.setText("Player 5");
                    player5_score.setText("");
                }
                count++;
            }
    }
}
