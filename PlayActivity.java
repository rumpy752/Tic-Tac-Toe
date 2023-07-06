/*
    date: 2023-06-25
    author: Jaime Rump
    file: PlayActivity.java
    desc: This class provides necessary code for the play activity
 */
package com.example.tictoctoe;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
public class PlayActivity extends AppCompatActivity {
    protected Button buReturn;
    protected Intent intent;
    protected String receivedPlayer1Name;
    protected String receivedPlayer2Name;
    protected TextView players_turn;
    protected boolean flag = true;
    protected TextView title;
    protected TextView textViewplayer1;
    protected TextView textViewplayer2;
    protected int player1Points = 0;
    protected int player2Points = 0;
    protected int roundCount = 0;
    protected Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.gameboard_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.gameboard);
        }

        textViewplayer1 = findViewById(R.id.text_view_p1);
        textViewplayer2 =  findViewById(R.id.text_view_p2);
        title = findViewById(R.id.gameboard_title);
        players_turn = findViewById(R.id.players_turn);

        buReturn = findViewById(R.id.buReturn);
        buReturn.setOnClickListener(view -> {
            intent = new Intent(PlayActivity.this, MainActivity.class);
            startActivity(intent);
        });

        intent = getIntent();
        receivedPlayer1Name = intent.getStringExtra("player1");
        receivedPlayer2Name = intent.getStringExtra("player2");
        players_turn.setText(receivedPlayer1Name + "'s turn: ");

        buttons = new Button[3][3];
        buttons[0][0] = findViewById(R.id.button00);
        buttons[0][1] = findViewById(R.id.button01);
        buttons[0][2] = findViewById(R.id.button02);
        buttons[1][0] = findViewById(R.id.button10);
        buttons[1][1] = findViewById(R.id.button11);
        buttons[1][2] = findViewById(R.id.button12);
        buttons[2][0] = findViewById(R.id.button20);
        buttons[2][1] = findViewById(R.id.button21);
        buttons[2][2] = findViewById(R.id.button22);
    }

    public void onStart() {
        super.onStart();
            if (receivedPlayer2Name == null) {
                receivedPlayer2Name = "Android";
            }
            updatePointsText();
    }

    public void buttonEvent(View view) {
        Button button = (Button) view;

        if (flag) {
            roundCount++;
            if (!isGameOver()) {
                if (button.getText().toString().equals("")) {
                    button.setText("X");
                    flag = false;
                    players_turn.setText(receivedPlayer2Name + "'s turn: ");
                    isGameOver();
                }
            } else {
                return;
            }
        } else if (!flag && !receivedPlayer2Name.equals("Android")) {
            roundCount++;
            if (!isGameOver()) {
                if (button.getText().toString().equals("")) {
                    button.setText("O");
                    flag = true;
                    players_turn.setText(receivedPlayer1Name + "'s turn: ");
                    isGameOver();
                }
            } else {
                return;
            }
        }

        if (!flag && receivedPlayer2Name.equals("Android")) {
            ComputerMoveTask task = new ComputerMoveTask();
            if (!isGameOver()) {
                task.execute();

                flag = true;
                players_turn.setText(receivedPlayer1Name + "'s turn: ");
            } else {
                return;
            }
        }
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = buttons[row][col];
                button.setText("");
            }
        }

        roundCount = 0;
        players_turn.setText(receivedPlayer1Name + "'s turn: ");
    }

    // AsyncTask to perform computers move in the background
    class ComputerMoveTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            makeComputerMove();
            return null;
        }

        private void makeComputerMove() {
            int row, col;
            do {
                    row = (int) (Math.random() * 3);
                    col = (int) (Math.random() * 3);
                } while (!buttons[row][col].getText().toString().equals(""));

                buttons[row][col].setText("O");
                roundCount++;
                isGameOver();
        }
    }

    private boolean hasWinner() {
        String[][] field = new String[3][3];
        boolean result = flag;

        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i=0; i<3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                if (result) {
                    player1Wins();
                } else {
                    player2Wins();
                }
                return true;
            }
        }
        for (int i=0; i<3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                if (result) {
                    player1Wins();
                } else {
                    player2Wins();
                }
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            if (result) {
                player1Wins();
            } else {
                player2Wins();
            }
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            if (result) {
                player1Wins();
            } else {
                player2Wins();
            }
            return true;
        }

        if (roundCount > 8) {
            draw();
            return true;
        }
        return false;
    }

    private void player1Wins() {
        player2Points++;
        title.setText("Player 2 WINS!");
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        player1Points++;
        title.setText("Player 1 WINS!");
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        title.setText("It's a draw!");
        resetBoard();
    }

    private boolean isGameOver() {
        if (hasWinner()) {
            flag = true;
            return true;
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().toString().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updatePointsText() {
        textViewplayer1.setText(receivedPlayer1Name + ": " + player1Points);
        textViewplayer2.setText(receivedPlayer2Name + ": " + player2Points);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String fileName = "player_data.txt";
        String data;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        boolean nameExists = checkIfNameExists(fileName, receivedPlayer1Name);

        if (!nameExists) {
            try {
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_APPEND);
                data = receivedPlayer1Name + "," + player1Points + "," + timestamp + "\n";
                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    String[] playerData = line.split(",");

                    String playerName = playerData[0];

                    if (playerName.equals(receivedPlayer1Name)) {
                        int playerScore = Integer.parseInt(playerData[1]);
                        playerScore += player1Points;
                        line = playerName + "," + playerScore + "," + timestamp;;
                    }
                    stringBuilder.append(line).append("\n");
                }

                br.close();
                isr.close();
                fis.close();

                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(stringBuilder.toString().getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Repeat the same logic for receivedPlayer2Name
        nameExists = checkIfNameExists(fileName, receivedPlayer2Name);

        if (!nameExists && flag) {
            try {
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_APPEND);
                data = receivedPlayer2Name + "," + player2Points + "," + timestamp + "\n";

                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    String[] playerData = line.split(",");

                    String playerName = playerData[0];

                    if (playerName.equals(receivedPlayer2Name)) {
                        int playerScore = Integer.parseInt(playerData[1]);
                        playerScore += player2Points;
                        line = playerName + "," + playerScore + "," + timestamp;
                    }

                    stringBuilder.append(line).append("\n");
                }

                br.close();
                isr.close();
                fis.close();

                // Write the modified data back to the file
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(stringBuilder.toString().getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIfNameExists(String fileName, String name) {
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String firstWord = parts[0].trim();

                if (firstWord.equalsIgnoreCase(name.trim())) {
                    br.close();
                    isr.close();
                    fis.close();
                    return true;
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
