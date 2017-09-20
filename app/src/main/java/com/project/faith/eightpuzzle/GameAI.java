package com.project.faith.eightpuzzle;


import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Faith on 8/16/2017.
 */

public class GameAI extends AsyncTask<SpaceTile, Integer, ArrayList<SpaceTile.Direction>>{

    private GameAIProperties gameAIProperties;
    private Grid grid;

    public GameAI(GameAIProperties gameAIProperties, Grid grid){
        this.gameAIProperties = gameAIProperties;
        this.grid= grid;
    }

    public interface GameAIProperties{
        void startSearchTimer();
        void stopSearchTimer();
        void numberOfMovesFound(int num);
        void updateSearchProgress(int progress);
    }

    public ArrayList<SpaceTile.Direction> DepthFirstSearch(SpaceTile spaceTile){
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        ArrayList<SpaceTile> spaceTiles = new ArrayList<>();
        ArrayList<GameState> gameStates = new ArrayList<>();
        boolean goalFound = false;
        int count = 0;
        int total = spaceTile.gridScale * spaceTile.gridScale;
        while(!goalFound && !this.isCancelled()){
            count++;
            int currentHeuristic = spaceTile.computeHeuristic();
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(count % 100 == 0){
                    int progress =  (currentHeuristic) * 100 / total;
                    if(progress != 100) onProgressUpdate(100 - progress);
                }
                if(tempSpaceTile.atGridEdge() && tempSpaceTile.gameState.isGoalState(tempSpaceTile.gridScale)) {
                    goalFound = true;
                    searchActions = tempSpaceTile.actions;
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

    public ArrayList<SpaceTile.Direction> BreadthFirstSearch(SpaceTile spaceTile){
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        ArrayList<SpaceTile> spaceTiles = new ArrayList<>();
        ArrayList<GameState> gameStates = new ArrayList<>();
        boolean goalFound = false;
        int count = 0;
        int total = spaceTile.gridScale * spaceTile.gridScale;
        while(!goalFound && !this.isCancelled()){
            count++;
            int currentHeuristic = spaceTile.computeHeuristic();
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(count % 100 == 0){
                    int progress =  (currentHeuristic) * 100 / total;
                    if(progress != 100) onProgressUpdate(100 - progress);
                }
                if(tempSpaceTile.atGridEdge() && tempSpaceTile.gameState.isGoalState(tempSpaceTile.gridScale)) {
                    goalFound = true;
                    searchActions = tempSpaceTile.actions;
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

    public ArrayList<SpaceTile.Direction> UniformCostSearch(SpaceTile spaceTile){
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.cost - o2.cost;
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        boolean goalFound = false;
        int count = 0;
        int total = spaceTile.gridScale * spaceTile.gridScale;
        while(!goalFound && !this.isCancelled()){
            count++;
            int currentHeuristic = spaceTile.computeHeuristic();
            if(spaceTile.atGridEdge() && spaceTile.gameState.isGoalState(spaceTile.gridScale)) {
                onProgressUpdate(100);
                searchActions = spaceTile.actions;
                goalFound = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(count % 100 == 0){
                    int progress =  (currentHeuristic) * 100 / total;
                    if(progress != 100) onProgressUpdate(100 - progress);
                }
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            spaceTile = spaceTileQueue.poll();
        }
        return searchActions;
    }

    public ArrayList<SpaceTile.Direction> GreedySearch(SpaceTile spaceTile){
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.computeHeuristic() - o2.computeHeuristic();
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        boolean goalFound = false;
        int count = 0;
        int total = spaceTile.gridScale * spaceTile.gridScale;
        while(!goalFound && !this.isCancelled()){
            count++;
            int currentHeuristic = spaceTile.computeHeuristic();
            if(spaceTile.atGridEdge() && spaceTile.gameState.isGoalState(spaceTile.gridScale)) {
                onProgressUpdate(100);
                searchActions = spaceTile.actions;
                goalFound = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(count % 100 == 0){
                    int progress =  (currentHeuristic) * 100 / total;
                    if(progress != 100) onProgressUpdate(100 - progress);
                }
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            spaceTile = spaceTileQueue.poll();
        }
        return searchActions;
    }

    public ArrayList<SpaceTile.Direction> AStarSearch(SpaceTile spaceTile){
        ArrayList<SpaceTile.Direction> searchActions = new ArrayList<>();
        Queue<SpaceTile> spaceTileQueue = new PriorityQueue<>(10, new Comparator<SpaceTile>() {
            @Override
            public int compare(SpaceTile o1, SpaceTile o2) {
                return o1.computeAStarCost() - o2.computeAStarCost();
            }
        });
        ArrayList<GameState> gameStates = new ArrayList<>();
        boolean goalFound = false;
        int count = 0;
        int total = spaceTile.gridScale * spaceTile.gridScale;
        while(!goalFound && !this.isCancelled()){
            int currentHeuristic = spaceTile.computeHeuristic();
            count++;
            if(spaceTile.atGridEdge() && spaceTile.gameState.isGoalState(spaceTile.gridScale)) {
                onProgressUpdate(100);
                searchActions = spaceTile.actions;
                goalFound = true;
            }
            ArrayList<SpaceTile.Direction> directions = spaceTile.availableMoves();
            for(SpaceTile.Direction direction : directions){
                SpaceTile tempSpaceTile = spaceTile.clone();
                tempSpaceTile.actions.addAll(spaceTile.actions);
                tempSpaceTile.moveSpaceTile(direction, false);
                if(gameStates.contains(tempSpaceTile.gameState)) continue;
                if(count % 100 == 0){
                    int progress =  (currentHeuristic) * 100 / total;
                    if(progress != 100) onProgressUpdate(100 - progress);
                }
                spaceTileQueue.add(tempSpaceTile);
                gameStates.add(tempSpaceTile.gameState);
            }
            if(spaceTileQueue.size() == 0) goalFound = true;
            else{spaceTile = spaceTileQueue.poll();}
        }
        return searchActions;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        GridTemplate.GameStateSearchOngoing = true;
        this.gameAIProperties.startSearchTimer();
    }

    @Override
    protected ArrayList<SpaceTile.Direction> doInBackground(SpaceTile... params) {
        switch (GridTemplate.selectedMode){
            case DEPTHFIRSTSEARCH: return this.DepthFirstSearch(params[0]);
            case BREADTHFIRSTSEARCH: return this.BreadthFirstSearch(params[0]);
            case UNIFORMCOSTSEARCH: return this.UniformCostSearch(params[0]);
            case GREEDYSEARCH: return this.GreedySearch(params[0]);
            case ASTARSEARCH: return this.AStarSearch(params[0]);
            default: return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<SpaceTile.Direction> directions) {
        super.onPostExecute(directions);
        final ArrayList<SpaceTile.Direction> directionArrayList = directions;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameAIProperties.numberOfMovesFound(directionArrayList.size());
                gameAIProperties.stopSearchTimer();
                grid.spaceTile.takeActions(directionArrayList);
            }
        }, 1000);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        gameAIProperties.updateSearchProgress(values[0]);
    }
}