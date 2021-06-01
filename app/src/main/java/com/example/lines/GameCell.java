package com.example.lines;

import android.graphics.Color;

import java.util.Random;

public class GameCell {
    enum CellType {
        EMPTY,
        SMALL_CIRCLE,
        BIG_CIRCLE
    }

    private int color;
    private boolean wasClicked;
    private static Random random = new Random(System.currentTimeMillis());
    private CellType cellType;

    public GameCell()
    {
        cellType = CellType.EMPTY;
        wasClicked = false;
    }

    public GameCell copy() {
        GameCell newCell = new GameCell();
        newCell.color = color;
        newCell.wasClicked = wasClicked;
        newCell.cellType = cellType;
        return newCell;
    }

    private void setRandomColor()
    {
        int colorsCount = 7;
        int newColor = Math.abs(random.nextInt()) % colorsCount;

        if (newColor == 0) {
            color = 0xFF000000;
        }
        else if (newColor == 1) {
            color = 0xFF3b4def;
        }
        else if (newColor == 2) {
            color = 0xFF10c45c;
        }
        else if (newColor == 3){
            color = 0xFFf0aa47;
        }
        else if (newColor == 4) {
            color = 0xFFf98f8e;
        }
        else if (newColor == 5) {
            color = 0xFFa5be22;
        }
        else {
            color = 0xFF25c2d7;
        }
    }

    public void upgrade()
    {
        if (cellType == CellType.EMPTY) {
            cellType = CellType.SMALL_CIRCLE;
            setRandomColor();
        }
        else
            cellType = CellType.BIG_CIRCLE;
    }

    public void setEmpty()
    {
        cellType = CellType.EMPTY;
        wasClicked = false;
    }

    public CellType getCellType()
    {
        return cellType;
    }

    public void setColor(int color) { this.color = color; }
    int getColor()
    {
        return color;
    }

    int getBackgroundColor()
    {
        if (wasClicked) {
            return 0xFF74ccf6;
        }
        return Color.TRANSPARENT;
    }

    public void setWasClicked(boolean wasClicked) {
        this.wasClicked = wasClicked;
    }
    public boolean getWasClicked() { return wasClicked; }
}
