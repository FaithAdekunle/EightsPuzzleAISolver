package com.project.faith.eightpuzzle;


import android.app.Activity;
import android.app.Fragment;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class SearchFragment extends Fragment{

    onSpaceTileReadyToSearch readyToSearch;
    Date startTimer;
    Date endTimer;
    int numberOfMoves = 0;

    public SearchFragment() {}

    public interface onSpaceTileReadyToSearch{
        void searchBegin(GameAI.GameAIProperties gameAIProperties) throws ExecutionException;
        void searchOnGoing(int progress);
        void searchEnd(double duration, int numberOfMoves);
        void searchMemoryError();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            readyToSearch = (onSpaceTileReadyToSearch) activity;
            readyToSearch.searchBegin(gameAIProperties);
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "MainActivity must implement onSpaceTileReadyToSearch interface");
        }
        catch(ExecutionException e){
            readyToSearch.searchMemoryError();
        }
    }

    private GameAI.GameAIProperties gameAIProperties = new GameAI.GameAIProperties() {
        @Override
        public void startSearchTimer() {
            startTimer = new Date();
        }

        @Override
        public void stopSearchTimer() {
            endTimer = new Date();
            double timeDiffSeconds = (endTimer.getTime() - startTimer.getTime())/1000;
            readyToSearch.searchEnd(timeDiffSeconds, numberOfMoves);
        }

        @Override
        public void numberOfMovesFound(int num) {
            numberOfMoves = num;
        }

        @Override
        public void updateSearchProgress(int progress) {
            readyToSearch.searchOnGoing(progress);
        }
    };
}
