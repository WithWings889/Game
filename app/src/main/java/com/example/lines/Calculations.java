package com.example.lines;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Calculations {
    private Calculations(){}

    private static int horizontalScore(List<List<GameCell>> board) {
        int score = 0;

        for (int i = 0; i < board.size(); ++i) {
            int cellsCounter = 0;
            int color = 0;
            for (int j = 0; j < board.get(i).size(); ++j) {
                GameCell cell = board.get(i).get(j);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            score += cellsCounter;
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        score += cellsCounter;
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                score += cellsCounter;
            }
        }

        return score;
    }

    private static int verticalScore(List<List<GameCell>> board) {
        int score = 0;

        for (int i = 0; i < board.get(0).size(); ++i) {
            int cellsCounter = 0;
            int color = 0;
            for (int j = 0; j < board.size(); ++j) {
                GameCell cell = board.get(j).get(i);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            score += cellsCounter;
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        score += cellsCounter;
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                score += cellsCounter;
            }
        }

        return score;
    }

    private static int mainDiagonalScore(List<List<GameCell>> board) {
        int score = 0;

        for (int i = -board.get(0).size() + 5; i <= board.size() - 5; ++i) {
            int cellsCounter = 0;
            int color = 0;
            int i1 = Math.max(i, 0);
            int j = 0;
            if (i < 0)
                j = -i;
            for (; i1 < board.size() && j < board.get(i1).size(); ++j, ++i1) {
                GameCell cell = board.get(i1).get(j);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            score += cellsCounter;
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        score += cellsCounter;
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                score += cellsCounter;
            }
        }

        return score;
    }

    private static int sideDiagonalScore(List<List<GameCell>> board) {
        int score = 0;

        for (int i = -board.get(0).size() + 5; i <= board.size() - 5; ++i) {
            int cellsCounter = 0;
            int color = 0;
            int i1 = Math.max(i, 0);
            int j = board.get(0).size() - 1;
            if (i < 0)
                j = board.get(0).size() + i - 1;
            for (; i1 < board.size() && j >= 0; --j, ++i1) {
                GameCell cell = board.get(i1).get(j);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            score += cellsCounter;
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        score += cellsCounter;
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                score += cellsCounter;
            }
        }

        return score;
    }

    public static int getNewScore(List<List<GameCell>> board) {
        int score = 0;

        score += horizontalScore(board);
        score += verticalScore(board);
        score += mainDiagonalScore(board);
        score += sideDiagonalScore(board);

        return score;
    }
}
