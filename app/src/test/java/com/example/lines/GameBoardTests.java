package com.example.lines;

import android.graphics.Color;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameBoardTests {
    private int numberOfRows = 9;
    private int numberOfColumns = 9;
    private int numberOfTests = 100;

    @Test
    public void checkSwapCells() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        List<List<GameCell>> board = game.getBoard();

        {
            board.get(0).get(0).upgrade();
            board.get(0).get(0).upgrade();
            board.get(0).get(0).setColor(Color.RED);

            game.swapCells(0, 0, 1, 1);
            game.getBoard();
            assertEquals(GameCell.CellType.EMPTY, board.get(0).get(0).getCellType());
            assertEquals(GameCell.CellType.BIG_CIRCLE, board.get(1).get(1).getCellType());
        }
    }

    @Test
    public void checkConvertSmallToBigCircle() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));

        int n = 5;
        game.randomFillWithSmallCircles(n);
        List<List<GameCell>>  board = game.getBoard();
        int smallCellCircleCount = 0;

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() == GameCell.CellType.SMALL_CIRCLE) {
                    ++smallCellCircleCount;
                }
            }
        }
        assertEquals(n, smallCellCircleCount);

        game.convertSmallCirclesToBig();
        game.getBoard();

        smallCellCircleCount = 0;

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() == GameCell.CellType.SMALL_CIRCLE) {
                    ++smallCellCircleCount;
                }
            }
        }
        assertEquals(0, smallCellCircleCount);
    }

    @Test
    public void checkRandomFillWithSmallCircles() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        List<List<GameCell>> board = game.getBoard();

        int noEmptyCells = 0;

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() != GameCell.CellType.EMPTY) {
                    ++noEmptyCells;
                }
            }
        }
        assertEquals(0, noEmptyCells);

        int n = 2;

        game.randomFillWithSmallCircles(n);
        board = game.getBoard();

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() != GameCell.CellType.EMPTY) {
                    ++noEmptyCells;
                }
            }
        }
        assertEquals(n, noEmptyCells);
    }

    @Test
    public void checkNextTurn() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        List<List<GameCell>> board = game.getBoard();

        int noEmptyCells = 0;

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() != GameCell.CellType.EMPTY) {
                    ++noEmptyCells;
                }
            }
        }
        assertEquals(0, noEmptyCells);

        int n = 2;
        game.nextTurn(2);
        board = game.getBoard();

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() != GameCell.CellType.EMPTY) {
                    ++noEmptyCells;
                }
            }
        }
        assertEquals(n, noEmptyCells);
    }

    @Test
    public void checkRemoveDisappearedCells() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        List<List<GameCell>> board = game.getBoard();

        {
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(0).get(i).upgrade();
                board.get(0).get(i).upgrade();
                board.get(0).get(i).setColor(Color.RED);
            }

            game.removeDisappearedCells();
            board = game.getBoard();
            for (int i = 0; i < board.size(); ++i) {
                for (int j = 0; j < board.get(i).size(); ++j) {
                    assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
                }
            }
        }
        {
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(0).upgrade();
                board.get(i).get(0).upgrade();
                board.get(i).get(0).setColor(Color.RED);
            }

            game.removeDisappearedCells();
            board = game.getBoard();
            for (int i = 0; i < board.size(); ++i) {
                for (int j = 0; j < board.get(i).size(); ++j) {
                    assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
                }
            }
        }
        {
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(i).upgrade();
                board.get(i).get(i).upgrade();
                board.get(i).get(i).setColor(Color.RED);
            }

            game.removeDisappearedCells();
            board = game.getBoard();
            for (int i = 0; i < board.size(); ++i) {
                for (int j = 0; j < board.get(i).size(); ++j) {
                    assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
                }
            }
        }
        {
            int n = 5;
            for (int i = 0; i < n; ++i) {
                board.get(i).get(5-i).upgrade();
                board.get(i).get(5-i).upgrade();
                board.get(i).get(5-i).setColor(Color.RED);
            }

            game.removeDisappearedCells();
            board = game.getBoard();
            for (int i = 0; i < board.size(); ++i) {
                for (int j = 0; j < board.get(i).size(); ++j) {
                    assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
                }
            }
        }
    }

    @Test
    public void checkBoardCreation() {
        for (int k = 0; k < numberOfTests; ++k) {
            int currentNumberOfRows = new Random().nextInt(numberOfTests);
            int currentNumberOfColumns = new Random().nextInt(numberOfTests);

            GameBoard game = new GameBoard();
            List<List<GameCell>> board = game.createEmptyBoard(currentNumberOfRows, currentNumberOfColumns);

            assertEquals(currentNumberOfRows, board.size());

            for (int i = 0; i < currentNumberOfRows; ++i) {
                assertEquals(currentNumberOfColumns, board.get(i).size());

                for (int j = 0; j < currentNumberOfColumns; ++j) {
                    assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
                }
            }
        }
    }

    @Test
    public void checkClearBoard() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.setBoard(gameBoard.createEmptyBoard(numberOfRows, numberOfColumns));
        gameBoard.clearBoard();

        List<List<GameCell>> board = gameBoard.getBoard();

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                assertEquals(GameCell.CellType.EMPTY, board.get(i).get(j).getCellType());
            }
        }
    }

    @Test
    public void checkCanMove() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));

        assertTrue(game.canMove(0, 0, 8, 8));
        assertFalse(game.canMove(0, 0, 0, 0));
    }

    @Test
    public void checkEndOfGame() {
        GameBoard game = new GameBoard();
        game.setBoard(game.createEmptyBoard(numberOfRows, numberOfColumns));
        assertTrue(game.isEnd());

        game.nextTurn(1);
        assertTrue(game.isEnd());

        game.nextTurn(2);
        assertFalse(game.isEnd());
    }

}
