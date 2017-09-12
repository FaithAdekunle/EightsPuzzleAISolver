package com.project.faith.eightpuzzle;


import android.app.Activity;
import android.app.Fragment;

public class SearchFragment extends Fragment {

    onSpaceTileReadyToSearch readyToSearch;

    public SearchFragment() {}

    public interface onSpaceTileReadyToSearch{
        void searchBegin();
        void searchMemoryError();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            readyToSearch = (onSpaceTileReadyToSearch) activity;
            readyToSearch.searchBegin();
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "MainActivity must implement onSpaceTileReadyToSearch interface");
        }
        catch(OutOfMemoryError e){
            readyToSearch.searchMemoryError();
        }
    }


}
