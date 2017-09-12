package com.project.faith.eightpuzzle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.Random;


/**
 * Created by Faith on 8/15/2017.
 */

public class Grid {
    public int gridScale;
    public int cellWidth;
    public int cellHeight;
    public int[][] matrix;
    public ViewGroup gridContainer;
    public SpaceTile spaceTile = new SpaceTile();

    public Grid(int gridScale){this.gridScale = gridScale;}

    public void populateMatrix(){
        matrix = new int[gridScale][gridScale];
        String[] tileList = generateTileList();
        for(int i=0; i<gridScale; i++){
            String[] tile = tileList[i].split(",");
            for(int j=0; j<gridScale; j++){
                int ij = Integer.valueOf(tile[j]+"");
                matrix[i][j] = ij;
                Log.v("pos", ij+"");
            }
        }
    }

    public void populateMatrixDefault(GridTemplate.GridTemplates gridTemplate){
        if(gridTemplate.equals(GridTemplate.GridTemplates.TEMPLATE1)){
            this.matrix = GridTemplate.template1;
        }
        else if(gridTemplate.equals(GridTemplate.GridTemplates.TEMPLATE2)){
            this.matrix = GridTemplate.template2;
        }
        else if(gridTemplate.equals(GridTemplate.GridTemplates.TEMPLATE3)){
            this.matrix = GridTemplate.template3;
        }
        else if(gridTemplate.equals(GridTemplate.GridTemplates.TEMPLATE4)){
            this.matrix = GridTemplate.template4;
        }
        else if(gridTemplate.equals(GridTemplate.GridTemplates.TEMPLATE5)){
            this.matrix = GridTemplate.template5;
        }
    }

    public void updateSpaceTile(int row, int col, boolean real){
        this.spaceTile.setPosition(row, col, real);
    }

    private String[] generateTileList(){
        int listLength = this.gridScale*this.gridScale;
        int[] tileList = new int[listLength];
        for(int i=0; i<listLength; i++){
           tileList[i] = i;
        }
        tileList = shuffleTileList(tileList);
        while(!isTileListValid(tileList)){
            shuffleTileList(tileList);
        }
        return groupTileList(tileList);
    }

    private boolean isTileListValid(int[] tileList){
        int count = 0;
        for(int i=0; i<tileList.length-1; i++){
            if(tileList[i] == 0) continue;
            for(int j=i+1; j<tileList.length; j++){
                if(tileList[j] == 0) continue;
                if(tileList[j] > tileList[i]) count++;
            }
        }
        if(count%2 > 0) return false;
        return true;
    }

    private int[] shuffleTileList(int[] tileList){
        Random random = new Random();
        int listLength = tileList.length - 1;
        for(int i=0; i<listLength; i++){
            int randomPos =  i + 1 + random.nextInt(listLength-i);
            int temp = tileList[randomPos];
            tileList[randomPos] = tileList[i];
            tileList[i] = temp;
        }
        return tileList;
    }

    private String[] groupTileList(int[] tileList){
        String[] tileString = new String[tileList.length / gridScale];
        int count = 0;
        int start = 0;
        int end = gridScale;
        while(end <= tileList.length){
            String temp = "";
            for(int i=start; i<end; i++){
                temp += tileList[i];
                if(i != end-1) temp += ",";
            }
            tileString[count] = temp;
            count++;
            start += gridScale;
            end += gridScale;
        }
        return tileString;
    }

    public void setTiles(RelativeLayout parentView){
        this.gridContainer = parentView;
        this.cellHeight = parentView.getHeight()/this.gridScale;
        this.cellWidth = parentView.getWidth()/this.gridScale;
        for(int i=0; i<this.gridScale; i++){
            for(int j=0; j<this.gridScale; j++){
                LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
                View aview = inflater.inflate(R.layout.tiles, null, false);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.cellWidth, this.cellHeight);
                aview.setX(j*this.cellWidth);
                aview.setY(i*this.cellHeight);
                parentView.addView(aview, params);
            }
        }
    }

    public int getX(int col){
        return this.cellWidth*col;
    }

    public int getY(int row){
        return this.cellHeight*row;
    }

    public Grid clone(){
        Grid newGrid = new Grid(this.gridScale);
        newGrid.cellHeight = this.cellHeight;
        newGrid.cellWidth = this.cellWidth;
        return newGrid;
    }
}