package com.example.lines;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScoreLabel scoreLabel = new ScoreLabel(this);

        GameBoard gameBoard = new GameBoard(scoreLabel);
        GameBoardView gameBoardView = new GameBoardView(this, gameBoard);

        Button button = setRestartButton(gameBoard, gameBoardView);

        LinearLayout viewGroup = new LinearLayout(this);
        viewGroup.setOrientation(LinearLayout.VERTICAL);

        viewGroup.addView(button);
        viewGroup.addView(scoreLabel.getViewGroup());
        viewGroup.addView(gameBoardView);

        setContentView(viewGroup);
    }

    public Button setRestartButton(final GameBoard gameBoard, final GameBoardView gameBoardView) {
        Button button = new Button(this);
        button.setText("Restart");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameBoard.clear();
                gameBoardView.invalidate();
            }
        });
        return button;
    }
}

