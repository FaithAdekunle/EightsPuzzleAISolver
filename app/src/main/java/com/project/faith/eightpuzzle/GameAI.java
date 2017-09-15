package com.project.faith.eightpuzzle;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Faith on 8/16/2017.
 */

public class GameAI {

    public GameAIProperties gameAIProperties;

    public GameAI(GameAIProperties gameAIProperties){
        this.gameAIProperties = gameAIProperties;
    }

    public interface GameAIProperties{
        void startSearchTimer();
        void stopSearchTimer();
        void numberOfMovesFound(int num);
    }

    public ArrayList<SpaceTile.Direction> DepthFirstSearch(Grid grid){
        SpaceTile spaceTile = grid.spaceTile;
        ArrayList<SpaceTile> spaceTiles = new ArrayList<>();
        ArrayList<GameState> gameStates = new ArrayList<>();
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        boolean goalFound = false;
        this.gameAIProperties.startSearchTimer();
        while(!goalFound){
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(((tempSpaceTile.row == 0 && tempSpaceTile.col == 0) || (tempSpaceTile.row ==  grid.gridScale-1 && tempSpaceTile.col == grid.gridScale-1)) && tempSpaceTile.gameState.isGoalState(grid.gridScale)) {
                    goalFound = true;
                    GridTemplate.GameStateDone = true;
                    searchActions.addAll(tempSpaceTile.actions);
                    this.gameAIProperties.numberOfMovesFound(searchActions.size());
                    this.gameAIProperties.stopSearchTimer();
                }
                spaceTiles.add(0, tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTiles.size() == 0) goalFound = true;
            else{
                spaceTile = spaceTiles.get(0);
                spaceTiles.remove(0);
            }
        }
        return  searchActions;
    }

    public ArrayList<SpaceTile.Direction> BreadthFirstSearch(Grid grid){
        SpaceTile spaceTile = grid.spaceTile;
        ArrayList<SpaceTile> spaceTiles = new ArrayList<>();
        ArrayList<GameState> gameStates = new ArrayList<>();
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        boolean goalFound = false;
        this.gameAIProperties.startSearchTimer();
        while(!goalFound){
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(((tempSpaceTile.row == 0 && tempSpaceTile.col == 0) || (tempSpaceTile.row ==  grid.gridScale-1 && tempSpaceTile.col == grid.gridScale-1)) && tempSpaceTile.gameState.isGoalState(grid.gridScale)) {
                    goalFound = true;
                    GridTemplate.GameStateDone = true;
                    searchActions.addAll(tempSpaceTile.actions);
                    this.gameAIProperties.numberOfMovesFound(searchActions.size());
                    this.gameAIProperties.stopSearchTimer();
                }
                spaceTiles.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTiles.size() == 0) goalFound = true;
            spaceTile = spaceTiles.get(0);
            spaceTiles.remove(0);
        }
        return  searchActions;
    }

    public ArrayList<SpaceTile.Direction> UniformCostSearch(Grid grid){
        SpaceTile spaceTile = grid.spaceTile;
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.cost - o2.cost;
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        boolean goalFound = false;
        this.gameAIProperties.startSearchTimer();
        while(!goalFound){
            if(((spaceTile.row == 0 && spaceTile.col == 0) || (spaceTile.row ==  grid.gridScale-1 && spaceTile.col == grid.gridScale-1)) && spaceTile.gameState.isGoalState(grid.gridScale)) {
                searchActions.addAll(spaceTile.actions);
                this.gameAIProperties.numberOfMovesFound(searchActions.size());
                this.gameAIProperties.stopSearchTimer();
                goalFound = true;
                GridTemplate.GameStateDone = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            spaceTile = spaceTileQueue.poll();
        }
        return searchActions;
    }

    public ArrayList<SpaceTile.Direction> GreedySearch(Grid grid){
        SpaceTile spaceTile = grid.spaceTile;
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.computeHeuristic() - o2.computeHeuristic();
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        boolean goalFound = false;
        this.gameAIProperties.startSearchTimer();
        while(!goalFound){
            if(((spaceTile.row == 0 && spaceTile.col == 0) || (spaceTile.row ==  grid.gridScale-1 && spaceTile.col == grid.gridScale-1)) && spaceTile.gameState.isGoalState(grid.gridScale)) {
                searchActions.addAll(spaceTile.actions);
                this.gameAIProperties.numberOfMovesFound(searchActions.size());
                this.gameAIProperties.stopSearchTimer();
                goalFound = true;
                GridTemplate.GameStateDone = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            spaceTile = spaceTileQueue.poll();
        }
        return searchActions;
    }

    public ArrayList<SpaceTile.Direction> AStarSearch(Grid grid){
        SpaceTile spaceTile = grid.spaceTile;
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.computeAStarCost() - o2.computeAStarCost();
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        boolean goalFound = false;
        this.gameAIProperties.startSearchTimer();
        while(!goalFound){
            if(((spaceTile.row == 0 && spaceTile.col == 0) || (spaceTile.row ==  grid.gridScale-1 && spaceTile.col == grid.gridScale-1)) && spaceTile.gameState.isGoalState(grid.gridScale)) {
                searchActions.addAll(spaceTile.actions);
                this.gameAIProperties.numberOfMovesFound(searchActions.size());
                this.gameAIProperties.stopSearchTimer();
                goalFound = true;
                GridTemplate.GameStateDone = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            else{spaceTile = spaceTileQueue.poll();}
        }
        return searchActions;
    }
}