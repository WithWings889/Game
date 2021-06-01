package com.example.lines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public final class GameBoardView extends View {
    private float heightPercent;
    private float widthPercent;
    private float gameBoardHeightPercent;

    private GameBoard gameBoard;

    private Paint background = new Paint();
    private Rect backgroundRect;

    private Paint cellCircle = new Paint();
    private Paint cellBackground = new Paint();

    public GameBoardView(Context context, GameBoard gameBoard) {
        this(context, gameBoard, 1f, 1f, 1f);
    }

    public GameBoardView(Context context, GameBoard board, float heightPercent, float widthPercent, float gameBoardHeightPercent) {
        super(context);

        this.heightPercent = heightPercent;
        this.widthPercent = widthPercent;
        this.gameBoardHeightPercent = gameBoardHeightPercent;

        this.gameBoard = board;

        this.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP
                        && backgroundRect.contains((int) event.getX(), (int) event.getY())) {
                    int i = getIndex(event.getX(), gameBoard.getRowsNumber(), backgroundRect.left, backgroundRect.width());
                    int j = getIndex(event.getY(), gameBoard.getColumnsNumber(), backgroundRect.top, backgroundRect.height());

                    gameBoard.setClick(j, i);
                    GameBoardView.super.invalidate();
                }
                return true;
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        background.setColor(0xBB2222BB);
        background.setStrokeWidth(8);
        background.setStyle(Paint.Style.STROKE);

        int height = (int) (gameBoardHeightPercent * (float) (GameBoardView.super.getHeight()));

        Rect backgroundBorderRect = getBackgroundBorderRect(height, GameBoardView.super.getWidth(), heightPercent, widthPercent, gameBoard.getRowsNumber(), gameBoard.getColumnsNumber());
        backgroundBorderRect.offset(0, GameBoardView.super.getHeight() - height);

        backgroundRect = getBackgroundRect(height, GameBoardView.super.getWidth(), heightPercent, widthPercent, gameBoard.getRowsNumber(), gameBoard.getColumnsNumber());
        backgroundRect.offset(0, GameBoardView.super.getHeight() - height);


        canvas.drawRect(backgroundBorderRect, background);

        background.setColor(0xFFdde4fc);
        background.setStyle(Paint.Style.FILL);

        canvas.drawRect(backgroundRect, background);

        float[] pts = getLinesGrid(gameBoard.getRowsNumber(), gameBoard.getColumnsNumber());

        background.setColor(0xFF8794f5);
        background.setStrokeWidth(8);
        background.setStyle(Paint.Style.STROKE);

        canvas.drawLines(pts, background);

        List<List<GameCell>> gameCellArray = gameBoard.getBoard();

        cellCircle.setStyle(Paint.Style.FILL);

        cellBackground.setStyle(Paint.Style.FILL);

        for (int i = 0; i < gameCellArray.size(); ++i) {
            for (int j = 0; j < gameCellArray.get(i).size(); ++j) {
                if (gameCellArray.get(i).get(j).getCellType() != GameCell.CellType.EMPTY && !gameCellArray.get(i).get(j).getWasClicked()) {
                    cellBackground.setColor(gameCellArray.get(i).get(j).getBackgroundColor());
                    canvas.drawRect(getCellBackgroundRect(gameBoard.getColumnsNumber(), i, j), cellBackground);

                    float[] circleParameter = getCircle(gameBoard.getColumnsNumber(), i, j, gameCellArray.get(i).get(j));
                    cellCircle.setColor(gameCellArray.get(i).get(j).getColor());
                    canvas.drawCircle(circleParameter[0], circleParameter[1], circleParameter[2], cellCircle);
                 }
            }
        }
        for (int i = 0; i < gameCellArray.size(); ++i) {
            for (int j = 0; j < gameCellArray.get(i).size(); ++j) {
                if (gameCellArray.get(i).get(j).getCellType() != GameCell.CellType.EMPTY && gameCellArray.get(i).get(j).getWasClicked()) {
                    cellBackground.setColor(gameCellArray.get(i).get(j).getBackgroundColor());
                    canvas.drawRect(getCellBackgroundRect(gameBoard.getColumnsNumber(), i, j), cellBackground);

                    float[] circleParameter = getCircle(gameBoard.getColumnsNumber(), i, j, gameCellArray.get(i).get(j));
                    cellCircle.setColor(gameCellArray.get(i).get(j).getColor());
                    canvas.drawCircle(circleParameter[0], circleParameter[1], circleParameter[2], cellCircle);
                }
            }
        }
    }

    private Rect getBackgroundBorderRect(int height, int width, float heightPercent, float widthPercent, int rowsCount, int columnsCount) {
        Rect rect = getBackgroundRect(height, width, heightPercent, widthPercent, rowsCount, columnsCount);
        rect.left -= 3;
        rect.right += 3;
        rect.top -= 3;
        rect.bottom += 3;
        return rect;
    }

    private Rect getBackgroundRect(int height, int width, float heightPercent, float widthPercent, int rowsCount, int columnsCount) {
        float areaHeight = heightPercent * (float) height;
        float areaWidth = widthPercent * (float) width;

        float blockHeight = areaHeight / (float) rowsCount;
        float blockWidth = areaWidth / (float) columnsCount;
        float blockSize = Math.min(blockHeight, blockWidth);

        float rectHeight = blockSize * (float) columnsCount;
        float rectWidth = blockSize * (float) rowsCount;

        Rect rect = new Rect();
        rect.top = (int) (((float) height * (1f - heightPercent) + (areaHeight - rectHeight)) * 0.5f);
        rect.bottom = rect.top + (int) (rectHeight);

        rect.left = (int) (((float) width * (1f - widthPercent) + (areaWidth - rectWidth)) * 0.5f);
        rect.right = rect.left + (int) (rectWidth);

        return rect;
    }

    private float[] getLinesGrid(int rowsCount, int columnsCount) {

        float[] grid = new float[4 * (rowsCount + columnsCount - 2)];

        float n = (float) rowsCount;
        float m = (float) columnsCount;

        //стовпчики
        for (int  i = 0; i < rowsCount - 1f; ++i) {
            float x = ((float) (i) + 1f) * ((backgroundRect.right - backgroundRect.left) / (n)) + backgroundRect.left;
            grid[4 * i] = x;
            grid[4 * i + 2] = x;
            grid[4 * i + 1] = backgroundRect.top;
            grid[4 * i + 3] = backgroundRect.bottom;
        }

        int offset = 4 * (rowsCount - 1);

        //рядки
        for (int i = 0; i < columnsCount - 1; ++i) {
            float y = ((float) (i) + 1f) * ((backgroundRect.bottom - backgroundRect.top) / (m)) + backgroundRect.top;
            grid[4 * i + offset] = backgroundRect.left;
            grid[4 * i + 2 + offset] = backgroundRect.right;
            grid[4 * i + 1 + offset] = y;
            grid[4 * i + 3 + offset] = y;
        }

        return grid;
    }

    private float[] getCircle(int columnsCount, int i, int j, GameCell gameCell) {
        float[] result = new float[3];

        float blockSize = (backgroundRect.right - backgroundRect.left) / (float) columnsCount;

        result[0] = backgroundRect.left +  blockSize * 0.5f + blockSize * (float) j;
        result[1] = backgroundRect.top + blockSize * 0.5f + blockSize * (float) i;
        if (gameCell.getCellType() == GameCell.CellType.BIG_CIRCLE) {
            result[2] = blockSize * (0.45f + (gameCell.getWasClicked() ? 0.25f : 0f));
        }
        else {
            result[2] = blockSize * 0.2f;
        }
        return result;
    }

    private Rect getCellBackgroundRect(int columnsCount, int i, int j) {
        Rect result = new Rect();

        float blockSize = (backgroundRect.right - backgroundRect.left) / (float) columnsCount;

        result.left = backgroundRect.left + (int) (blockSize * (float) j);
        result.top = backgroundRect.top + (int) (blockSize * (float) i);
        result.right = result.left + (int) blockSize;
        result.bottom = result.top + (int) blockSize;

        return result;
    }

    private int getIndex(float x, int rowsCount, float superX, float superHeight) {
        float xPosition = (x - superX) / superHeight;
        return (int) (xPosition * (float) rowsCount);
    }


}
