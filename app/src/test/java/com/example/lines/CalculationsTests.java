package com.example.lines;

import android.graphics.Color;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalculationsTests {
    private int numberOfRows = 9;
    private int numberOfColumns = 9;

    @Test
    public void checkScore() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        List<List<GameCell>> board = game.getBoard();
        assertEquals(0, Calculations.getNewScore(board));

        {
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(0).get(i).upgrade();
                board.get(0).get(i).upgrade();
                board.get(0).get(i).setColor(Color.RED);
            }
            assertEquals(n, Calculations.getNewScore(board));
        }
        {
            game.clearBoard();
            board = game.getBoard();
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(0).upgrade();
                board.get(i).get(0).upgrade();
                board.get(i).get(0).setColor(Color.RED);
            }
            assertEquals(n, Calculations.getNewScore(board));
        }
        {
            game.clearBoard();
            board = game.getBoard();
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(i).upgrade();
                board.get(i).get(i).upgrade();
                board.get(i).get(i).setColor(Color.RED);
            }
            assertEquals(n, Calculations.getNewScore(board));
        }
        {
            game.clearBoard();
            board = game.getBoard();
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(5-i).upgrade();
                board.get(i).get(5-i).upgrade();
                board.get(i).get(5-i).setColor(Color.RED);
            }
            assertEquals(n, Calculations.getNewScore(board));
        }
    }
}
