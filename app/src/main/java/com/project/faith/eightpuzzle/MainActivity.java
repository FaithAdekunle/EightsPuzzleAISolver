package com.project.faith.eightpuzzle;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SearchFragment.onSpaceTileReadyToSearch{

    public RelativeLayout gridContainer;
    public TextView movesText;
    public int moves = 0;
    public Grid grid;
    public boolean isSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridContainer = (RelativeLayout) findViewById(R.id.gridContainer);
        movesText = (TextView) findViewById(R.id.scoreContainer);
        gridContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isSet) return;
                onGameStart(false);
                isSet = true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.RESET:
                onGameReset(true);
                return true;
            case R.id.AUTO:
                GridTemplate.selectedTemplate = null;
                onGameStart(true);
                return true;
            case R.id.TEMPLATE1:
                GridTemplate.selectedTemplate = GridTemplate.GridTemplates.TEMPLATE1;
                onGameStart(false);
                return true;
            case R.id.TEMPLATE2:
                GridTemplate.selectedTemplate = GridTemplate.GridTemplates.TEMPLATE2;
                onGameStart(false);
                return true;
            case R.id.TEMPLATE3:
                GridTemplate.selectedTemplate = GridTemplate.GridTemplates.TEMPLATE3;
                onGameStart(false);
                return true;
            case R.id.TEMPLATE4:
                GridTemplate.selectedTemplate = GridTemplate.GridTemplates.TEMPLATE4;
                onGameStart(false);
                return true;
            case R.id.TEMPLATE5:
                GridTemplate.selectedTemplate = GridTemplate.GridTemplates.TEMPLATE5;
                onGameStart(false);
                return true;
            case R.id.astarsearch:
                GridTemplate.selectedMode = GridTemplate.GameMode.ASTARSEARCH;
                setFragment();
                return true;
            case R.id.greedysearch:
                GridTemplate.selectedMode = GridTemplate.GameMode.GREEDYSEARCH;
                setFragment();
                return true;
            case R.id.uniformcostsearch:
                GridTemplate.selectedMode = GridTemplate.GameMode.UNIFORMCOSTSEARCH;
                setFragment();
                return true;
            case R.id.breadthfirstsearch:
                GridTemplate.selectedMode = GridTemplate.GameMode.BREADTHFIRSTSEARCH;
                setFragment();
                return true;
            case R.id.depthfirstsearch:
                GridTemplate.selectedMode = GridTemplate.GameMode.DEPTHFIRSTSEARCH;
                setFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onGameStart(boolean random) {
        GridTemplate.selectedMode = GridTemplate.GameMode.MANUAL;
        grid = new Grid(3);
        if(random) grid.populateMatrix();
        if(!random) grid.populateMatrixDefault(GridTemplate.selectedTemplate);
        grid.setTiles(gridContainer);
        onGameReset(false);
    }

    public void onGameReset(boolean reset){
        if(reset){
            int[][] matrix = grid.matrix;
            grid = new Grid(3);
            grid.matrix = matrix;
            grid.setTiles(gridContainer);
        }
        grid.spaceTile.setGameState();
        GridTemplate.GameStateDone = false;
        moves = 0;
        movesText.setText("Moves: " + moves);
        int[][] matrix = grid.matrix;
        int gridScale = grid.gridScale;
        ChangeSpaceTile changeSpaceTile = new ChangeSpaceTile() {
            @Override
            public void updateSpaceTile(int row, int col) {
                updateMoves();
            }
        };
        for(int i=0; i<gridScale; i++){
            for(int j=0; j<gridScale; j++){
                if(matrix[i][j] == 0){
                    grid.spaceTile.setPosition(i, j, false);
                    grid.spaceTile.createView(grid);
                }
                else{
                    Tile tile = new Tile(matrix[i][j], i, j, changeSpaceTile);
                    tile.createView(grid);
                    grid.spaceTile.gameState.addTile(tile);
                }
            }
        }
    }

    public void setFragment(){
        if(!GridTemplate.GameStateDone){
            movesText.setText("Searching...\nThis might take very long!");
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            SearchFragment fragment = new SearchFragment();
            Fragment oldFragment = manager.findFragmentByTag("searchFragment");
            if(oldFragment == null){
                transaction.add(fragment, "searchFragment");
                transaction.commit();
            }
            else{
                transaction.remove(oldFragment);
                transaction.add(fragment, "searchFragment");
                transaction.commit();
            }
        }
    }

    @Override
    public void searchBegin() {
        if(!GridTemplate.GameStateDone)this.grid.spaceTile.takeActions();
    }

    @Override
    public void searchMemoryError() {
        movesText.setText("Memory space exhausted! Solve with another method.");
        this.onPause();
    }

    private void updateMoves(){
        moves++;
        this.movesText.setText("Moves: "+ moves);
    }

    public interface ChangeSpaceTile{
        void updateSpaceTile(int row, int col);
    }


}