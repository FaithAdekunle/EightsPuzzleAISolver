package com.project.faith.eightpuzzle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Faith on 8/15/2017.
 */

public class Tile {
    public  int row;
    public int col;
    public int value;
    public int cost;
    public Grid grid;
    public View tileView;
    private MainActivity.ChangeSpaceTile changeSpaceTile = null;

    public Tile(int value, int row, int col, MainActivity.ChangeSpaceTile changeSpaceTile){
        this.value = value;
        this.row = row;
        this.col = col;
        this.cost = 1;
        this.changeSpaceTile = changeSpaceTile;
    }

    public Tile(int value, int row, int col){
        this.value = value;
        this.row = row;
        this.col = col;
        this.cost = 1;
    }

    public void createView(Grid grid){
        this.grid = grid;
        LayoutInflater inflater = LayoutInflater.from(this.grid.gridContainer.getContext());
        tileView = inflater.inflate(R.layout.boxes, null, false);
        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GridTemplate.selectedMode.equals(GridTemplate.GameMode.MANUAL))swapTileWithSpaceTile(true);
            }
        });
        TextView tileText = (TextView) tileView.findViewById(R.id.text);
        tileText.setText(value+"");
        tileText.setTextSize(30);
        if (this.grid.gridScale > 5) tileText.setTextSize(15);
        if (this.grid.gridScale > 10) tileText.setTextSize(8);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.grid.cellWidth, this.grid.cellHeight);
        tileView.setX(0);
        tileView.setY(0);
        this.grid.gridContainer.addView(tileView, params);
        this.setPosition(this.row, this.col, true);
    }

    public void setPosition(int row, int col, boolean real){
        if(real){
            int x = this.grid.getX(col);
            int y = this.grid.getY(row);
            tileView.setX(x);
            tileView.setY(y);
        }
        this.row = row;
        this.col = col;
    }

    public void swapTileWithSpaceTile(boolean real){
        int spaceRow = this.grid.spaceTile.row;
        int spaceCol = this.grid.spaceTile.col;
        if(real){
            if((this.row == spaceRow && Math.abs(this.col - spaceCol) == 1) || (this.col == spaceCol && Math.abs(this.row - spaceRow) == 1)) {
                this.grid.updateSpaceTile(row, col, true);
                this.setPosition(spaceRow, spaceCol, true);
                changeSpaceTile.updateSpaceTile(row, col);
            }
        }
        else{
            this.grid.updateSpaceTile(row, col, false);
            this.setPosition(spaceRow, spaceCol, false);
        }
    }

    public void swapTileWithSpaceTile(SpaceTile spaceTile){
        int spaceRow = spaceTile.row;
        int spaceCol = spaceTile.col;
        spaceTile.setPosition(row, col, false);
        this.setPosition(spaceRow, spaceCol, false);
    }

    public boolean isSameAS(Tile tile){
        return this.row == tile.row && this.col == tile.col && this.value == tile.value;
    }

    public Tile clone(){
        Tile newTile = new Tile(this.value, this.row, this.col);
        return newTile;
    }
}