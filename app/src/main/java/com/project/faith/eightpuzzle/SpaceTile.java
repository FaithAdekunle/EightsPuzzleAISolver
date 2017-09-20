package com.project.faith.eightpuzzle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import java.util.ArrayList;

/**
 * Created by Faith on 8/16/2017.
 */

public class SpaceTile {
    public Grid grid;
    public int gridScale;
    public int row;
    public int col;
    public int cost = 0;
    private View spaceTileView;
    public GameState gameState;
    public enum Direction {UP, DOWN, RIGHT, LEFT}
    public ArrayList<Direction> actions = new ArrayList<>();
    Runnable actionRunnable;
    final static int ACTION_DELAY = 500;
    ArrayList<Direction> directions = new ArrayList<>();
    private int moveIndex = 0;

    public SpaceTile(){
        this.gameState = new GameState();
        actionRunnable = new Runnable() {
            @Override
            public void run() {
                if(GridTemplate.GameStateSearchOngoing){
                    if(moveIndex < directions.size()){
                        moveSpaceTile(directions.get(moveIndex), true);
                        moveIndex++;
                        Handler handler = new Handler();
                        handler.postDelayed(actionRunnable, ACTION_DELAY);
                    }
                    else{
                        directions = new ArrayList<>();
                    }
                }
            }
        };
    }

    public void createView(Grid grid){
        this.grid = grid;
        this.gridScale = grid.gridScale;
        LayoutInflater inflater = LayoutInflater.from(this.grid.gridContainer.getContext());
        this.spaceTileView = inflater.inflate(R.layout.tiles, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.grid.cellWidth, this.grid.cellHeight);
        this.spaceTileView.setX(0);
        this.spaceTileView.setY(0);
        this.grid.gridContainer.addView(spaceTileView, params);
        this.setPosition(this.row, this.col, true);
    }

    public void setGameState(){
        this.gameState = new GameState();
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public void setPosition(int row, int col, boolean real){
        this.row = row;
        this.col = col;
        if(real){
            int x = this.grid.getX(col);
            int y = this.grid.getY(row);
            this.spaceTileView.setX(x);
            this.spaceTileView.setY(y);
        }
    }

    public void moveSpaceTile(Direction direction, boolean real){
        if(direction.equals(Direction.UP)){
           Tile tile = gameState.findTile(row-1, col);
            if(real) tile.swapTileWithSpaceTile(true);
            if(!real) {
                tile.swapTileWithSpaceTile(this);
                this.cost += tile.cost;
            }
           this.actions.add(direction);
        }
        else if(direction.equals(Direction.DOWN)){
            Tile tile = gameState.findTile(row+1, col);
            if(real) tile.swapTileWithSpaceTile(true);
            if(!real) {
                tile.swapTileWithSpaceTile(this);
                this.cost += tile.cost;
            }
            this.actions.add(direction);
        }
        else if(direction.equals(Direction.LEFT)){
            Tile tile = gameState.findTile(row, col-1);
            if(real) tile.swapTileWithSpaceTile(true);
            if(!real) {
                tile.swapTileWithSpaceTile(this);
                this.cost += tile.cost;
            }
            this.actions.add(direction);
        }
        else if(direction.equals(Direction.RIGHT)){
            Tile tile = gameState.findTile(row, col+1);
            if(real) tile.swapTileWithSpaceTile(true);
            if(!real) {
                tile.swapTileWithSpaceTile(this);
                this.cost += tile.cost;
            }
            this.actions.add(direction);
        }
    }

    public int computeHeuristic(){
        int init = 0;
        for(int i=0; i<gameState.tileStates.size(); i++){
            Tile tile = gameState.tileStates.get(i);
            if(tile.row == gridScale-1 && tile.col == gridScale-1){
                continue;
            }
            else if(tile.col == gridScale-1){
                Tile nextTile = gameState.findTile(tile.row+1, 0);
                if(nextTile != null){
                    if(nextTile.value != tile.value+1) init++;
                }
            }
            else if(tile.col<gridScale-1){
                Tile nextTile = gameState.findTile(tile.row, tile.col+1);
                if(nextTile != null){
                    if(nextTile.value != tile.value+1) init++;
                }
            }
        }
        return init;
    }

    public int computeAStarCost(){
        return this.cost + this.computeHeuristic();
    }

    public boolean atGridEdge(){
        return (this.row == 0 && this.col == 0) || (this.row ==  this.gridScale-1 && this.col == this.gridScale-1);
    }

    public ArrayList<Direction> availableMoves(){
        ArrayList<Direction> moves = new ArrayList<>();
        if(row-1 >= 0) moves.add(Direction.UP);
        if(row+1 < gridScale) moves.add(Direction.DOWN);
        if(col-1 >= 0) moves.add(Direction.LEFT);
        if(col+1 < gridScale) moves.add(Direction.RIGHT);
        return moves;
    }

    public void getActions(GameAI.GameAIProperties gameAIProperties){
        GameAI gameAI = new GameAI(gameAIProperties, this.grid);
        MainActivity.gameAI = gameAI;
        gameAI.execute(this.clone());
    }

    public void takeActions(ArrayList<Direction> directions){
        this.directions = directions;
        moveIndex = 0;
        Handler handler = new Handler();
        handler.postDelayed(actionRunnable, ACTION_DELAY);
    }

    public SpaceTile clone(){
        SpaceTile newSpaceTile = new SpaceTile();
        newSpaceTile.setGameState(this.gameState.clone());
        newSpaceTile.gridScale = this.gridScale;
        newSpaceTile.cost = this.cost;
        newSpaceTile.setPosition(this.row, this.col, false);
        return newSpaceTile;
    }
}