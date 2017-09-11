package com.project.faith.eightpuzzle;

import java.util.ArrayList;

/**
 * Created by Faith on 8/16/2017.
 */

public class GameState {
    public ArrayList<Tile> tileStates = new ArrayList<>();

    public GameState() {
    }

    public void addTile(Tile tile) {
        this.tileStates.add(tile);
    }

    public Tile findTile(int row, int col) {
        Tile nullTile = null;
        for(Tile tile : tileStates){
            if(tile.row == row && tile.col == col) return tile;
        }
        return  nullTile;
    }

    public boolean isGoalState(int gridScale){
        for(int i=0; i<tileStates.size(); i++){
            Tile tile = tileStates.get(i);
            if(tile.row == gridScale-1 && tile.col == gridScale-1){
                continue;
            }
            else if(tile.col == gridScale-1){
                Tile nextTile = this.findTile(tile.row+1, 0);
                if(nextTile != null){
                    if(nextTile.value != tile.value+1) return false;
                }
            }
            else if(tile.col<gridScale-1){
                Tile nextTile = this.findTile(tile.row, tile.col+1);
                if(nextTile != null){
                    if(nextTile.value != tile.value+1) return false;
                }
            }
        }
        return true;
    }

    public GameState clone(){
        GameState newGameState = new GameState();
        for(Tile tile : tileStates){
            newGameState.addTile(tile.clone());
        }
        return newGameState;
    }

    public  boolean equals(Object obj){
        if (obj == null) return false;
        if (!GameState.class.isAssignableFrom(obj.getClass())) return false;
        GameState gameState = (GameState) obj;
        if(gameState.tileStates.size() != this.tileStates.size()) return false;
        for(int i=0; i<gameState.tileStates.size(); i++){
            if(gameState.tileStates.get(i).row != this.tileStates.get(i).row || gameState.tileStates.get(i).col != this.tileStates.get(i).col) return false;
        }
        return true;
    }
}