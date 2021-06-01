package com.example.lines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class GameBoard {
    private List<List<GameCell>> board;
    private int rowsNumber;
    private int columnsNumber;
    private int newCellsCount;
    private boolean wasClicked;
    private int fromRow;
    private int fromColumn;

    private ScoreLabel scoreLabel;

    public GameBoard(){}

    public GameBoard(ScoreLabel scoreLabel) {
        this(9, 9, 3, scoreLabel);
    }

    public GameBoard(int rowsNumber, int columnsNumber, int newCellsCount, ScoreLabel scoreLabel) {
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
        this.newCellsCount = newCellsCount;
        this.scoreLabel = scoreLabel;

        board = createEmptyBoard(rowsNumber, columnsNumber);
        start();
    }

    public void clear() {
        clearBoard();
        start();
    }

    public void setClick(int i, int j) {
        if (board.get(i).get(j).getCellType() == GameCell.CellType.BIG_CIRCLE) {
            if (wasClicked) {
                (board.get(fromRow).get(fromColumn)).setWasClicked(false);
            }
            (board.get(i).get(j)).setWasClicked(true);
            wasClicked = true;
            fromRow = i;
            fromColumn = j;
        }
        else if (wasClicked) {
            boolean move = canMove(fromRow, fromColumn, i, j);
            if (move) {
                swapCells(fromRow, fromColumn, i, j);
                (board.get(i).get(j)).setWasClicked(false);
                wasClicked = false;

                int tempScore = Calculations.getNewScore(board);
                scoreLabel.updateScore(tempScore);
                removeDisappearedCells();
                if (tempScore > 0)
                    return;
                nextTurn(newCellsCount);

                tempScore = Calculations.getNewScore(board);
                scoreLabel.updateScore(tempScore);
                removeDisappearedCells();

                boolean end = isEnd();
                if (end) {
                    nextTurn(newCellsCount);

                    tempScore = Calculations.getNewScore(board);
                    scoreLabel.updateScore(tempScore);
                    removeDisappearedCells();
                }
            }
        }
        else{
            (board.get(i).get(j)).setWasClicked(false);
            wasClicked = false;
        }
    }

    public List<List<GameCell>> getBoard() {
        return board;
    }

    public void setBoard(List<List<GameCell>> board) { this.board = board; }

    public final int getRowsNumber()  {
        return rowsNumber;
    }

    public final int getColumnsNumber() {
        return columnsNumber;
    }

    private void start() {
        randomFillWithSmallCircles(newCellsCount);
        convertSmallCirclesToBig();
        randomFillWithSmallCircles(newCellsCount);
        wasClicked = false;
        scoreLabel.reset();
    }

    public List<List<GameCell>> createEmptyBoard(int numberOfRows, int numberOfColumns) {
        List<List<GameCell>> newBoard = new ArrayList<>();

        for (int i = 0; i < numberOfRows; ++i) {
            List<GameCell> array = new ArrayList<>();

            for (int j = 0; j < numberOfColumns; ++j) {
                GameCell gameCell = new GameCell();
                array.add(gameCell);
            }
            newBoard.add(array);
        }

        return newBoard;
    }

    public void clearBoard() {
        for (int i = 0; i < board.size(); ++i)
            for (int j = 0; j < board.get(i).size(); ++j)
                board.get(i).get(j).setEmpty();
    }

    public void nextTurn(int n) {
        convertSmallCirclesToBig();
        randomFillWithSmallCircles(n);
    }

    public void convertSmallCirclesToBig() {
        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() == GameCell.CellType.SMALL_CIRCLE) {
                    board.get(i).get(j).upgrade();
                }
            }
        }
    }

    public void randomFillWithSmallCircles(int n) {
        List<List> emptyCells = new ArrayList<>();

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() == GameCell.CellType.EMPTY) {
                    List emptyCell = new ArrayList();
                    emptyCell.add(i);
                    emptyCell.add(j);

                    emptyCells.add(emptyCell);
                }
            }
        }

        Collections.shuffle(emptyCells);
        for (int i = 0; i < n && i < emptyCells.size(); ++i) {
            int rowIndex = (int) emptyCells.get(i).get(0);
            int columnIndex = (int) emptyCells.get(i).get(1);

            board.get(rowIndex).get(columnIndex).upgrade();
        }
    }

    public void swapCells(int fromRow, int fromColumn, int toRow, int toColumn) {
        GameCell cell = board.get(fromRow).get(fromColumn).copy();

        board.get(fromRow).get(fromColumn).setEmpty();
        List<GameCell> array = board.get(toRow);
        array.set(toColumn, cell);
        board.set(toRow, array);
    }


    public boolean canMove(int rowFrom, int columnFrom, int rowTo, int columnTo) {
        int n = board.size();
        int m = board.get(0).size();
        int[] stepRow = new int[] { -1, 0, 1, 0 };
        int[] stepColumn = new int[] { 0, 1, 0, -1 };
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] used = new boolean[n][m];
        used[rowFrom][columnFrom] = true;

        queue.add(new int[]{ rowFrom, columnFrom });
        while (!queue.isEmpty()) {
            int[] curCell = queue.remove();
            for (int i = 0; i < 4; ++i) {
                int nextRow = curCell[0] + stepRow[i];
                int nextColumn = curCell[1] + stepColumn[i];
                if (nextRow < 0 || nextRow >= n || nextColumn >= m || nextColumn < 0) {
                    continue;
                }
                if (board.get(nextRow).get(nextColumn).getCellType() != GameCell.CellType.BIG_CIRCLE && !used[nextRow][nextColumn]) {
                    used[nextRow][nextColumn] = true;
                    queue.add(new int[]{ nextRow, nextColumn });

                    if (nextRow == rowTo && nextColumn == columnTo)
                        return true;
                }
            }
        }
        return false;
    }


    public boolean isEnd() {
        int smallCirclesCounter = 0;

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.get(i).size(); ++j) {
                if (board.get(i).get(j).getCellType() == GameCell.CellType.SMALL_CIRCLE) {
                    ++smallCirclesCounter;
                }
            }
        }
        return smallCirclesCounter <= 1;
    }

    private List<List> getListToRemoveHorizontal() {
        List<List> needToRemove = new ArrayList<>();

        for (int i = 0; i < board.size(); ++i) {
            int cellsCounter = 0;
            int color = 0;
            for (int j = 0; j < board.get(i).size(); ++j) {
                GameCell cell = board.get(i).get(j);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            for (int k = j - 1; k >= j - cellsCounter; --k) {
                                List emptyCell = new ArrayList();
                                emptyCell.add(i);
                                emptyCell.add(k);

                                needToRemove.add(emptyCell);
                            }
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        for (int k = j - 1; k >= j - cellsCounter; --k) {
                            List emptyCell = new ArrayList();
                            emptyCell.add(i);
                            emptyCell.add(k);

                            needToRemove.add(emptyCell);
                        }
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                for (int k = board.get(i).size() - 1; k >= board.get(i).size() - cellsCounter; --k) {
                    List emptyCell = new ArrayList();
                    emptyCell.add(i);
                    emptyCell.add(k);

                    needToRemove.add(emptyCell);
                }
            }
        }

        return needToRemove;
    }

    private List<List> getListToRemoveVertical() {
        List<List> needToRemove = new ArrayList<>();

        for (int i = 0; i < board.get(0).size(); ++i) {
            int cellsCounter = 0;
            int color = 0;
            for (int j = 0; j < board.size(); ++j) {
                GameCell cell = board.get(j).get(i);
                if (cell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
                    if (color != cell.getColor()) {
                        color = cell.getColor();

                        if (cellsCounter >= 5) {
                            for (int k = j - 1; k >= j - cellsCounter; --k) {
                                List emptyCell = new ArrayList();
                                emptyCell.add(k);
                                emptyCell.add(i);

                                needToRemove.add(emptyCell);
                            }
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        for (int k = j - 1; k >= j - cellsCounter; --k) {
                            List emptyCell = new ArrayList();
                            emptyCell.add(k);
                            emptyCell.add(i);

                            needToRemove.add(emptyCell);
                        }
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                for (int k = board.get(i).size() - 1; k >= board.get(i).size() - cellsCounter; --k) {
                    List emptyCell = new ArrayList();
                    emptyCell.add(k);
                    emptyCell.add(i);

                    needToRemove.add(emptyCell);
                }
            }
        }

        return needToRemove;
    }

    private List<List> getListToRemoveMainDiagonal() {
        List<List> needToRemove = new ArrayList<>();

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
                            for (int k = j - 1, l = i1 - 1; k >= j - cellsCounter; --k, --l) {
                                List emptyCell = new ArrayList();
                                emptyCell.add(l);
                                emptyCell.add(k);

                                needToRemove.add(emptyCell);
                            }
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        for (int k = j - 1, l = i1 - 1; k >= j - cellsCounter; --k, --l) {
                            List emptyCell = new ArrayList();
                            emptyCell.add(l);
                            emptyCell.add(k);

                            needToRemove.add(emptyCell);
                        }
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                for (int k = j - 1, l = i1 - 1; k >= j - cellsCounter; --k, --l) {
                    List emptyCell = new ArrayList();
                    emptyCell.add(l);
                    emptyCell.add(k);

                    needToRemove.add(emptyCell);
                }
            }
        }

        return needToRemove;
    }

    private List<List> getListToRemoveSideDiagonal() {
        List<List> needToRemove = new ArrayList<>();

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
                            for (int k = j + 1, l = i1 - 1; k <= j + cellsCounter; ++k, --l) {
                                List emptyCell = new ArrayList();
                                emptyCell.add(l);
                                emptyCell.add(k);

                                needToRemove.add(emptyCell);
                            }
                        }

                        cellsCounter = 1;
                    }
                    else {
                        ++cellsCounter;
                    }
                }
                else {
                    if (cellsCounter >= 5) {
                        for (int k = j + 1, l = i1 - 1; k <= j + cellsCounter; ++k, --l) {
                            List emptyCell = new ArrayList();
                            emptyCell.add(l);
                            emptyCell.add(k);

                            needToRemove.add(emptyCell);
                        }
                    }
                    color = 0;
                    cellsCounter = 0;
                }
            }
            if (cellsCounter >= 5) {
                for (int k = j + 1, l = i1 - 1; k <= j + cellsCounter; ++k, --l) {
                    List emptyCell = new ArrayList();
                    emptyCell.add(l);
                    emptyCell.add(k);

                    needToRemove.add(emptyCell);
                }
            }
        }

        return needToRemove;
    }

    public void removeDisappearedCells() {
        List<List> needToRemove = new ArrayList<>();

        needToRemove.addAll(getListToRemoveHorizontal());
        needToRemove.addAll(getListToRemoveVertical());
        needToRemove.addAll(getListToRemoveMainDiagonal());
        needToRemove.addAll(getListToRemoveSideDiagonal());


        for (int i = 0; i < needToRemove.size(); ++i) {
            int iIndex = (int) needToRemove.get(i).get(0);
            int jIndex = (int) needToRemove.get(i).get(1);

            board.get(iIndex).get(jIndex).setEmpty();
        }
    }
}
