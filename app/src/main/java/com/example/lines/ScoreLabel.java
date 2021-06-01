package com.example.lines;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class ScoreLabel {
    private ViewGroup viewGroup;
    private TextView label;
    private float textSize;
    private int score;

    public ScoreLabel(Context context) {
        score = 0;
        textSize = 50f;
        TextView scoreTextLabel = new TextView(context);
        scoreTextLabel.setText("Score: ");
        scoreTextLabel.setTextSize(textSize);

        label = new TextView(context);
        label.setText(Integer.toString(score));
        label.setTextSize(textSize);

        viewGroup = new LinearLayout(context);
        viewGroup.addView(scoreTextLabel);
        viewGroup.addView(label);
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public void updateScore(int n) {
        score += n;
        label.setText(Integer.toString(score));
    }

    public void reset() {
        score = 0;
        label.setText(Integer.toString(score));
    }

}
