package com.arhsota.workout;


import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentTransaction;


public class MainActivity extends Activity
        implements WorkoutListFragment.WorkoutListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    public void itemClicked(long id) {
        //Здесь размещается код отображения подробной информации    }
        WorkDetailFragment details = new WorkDetailFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        details.setWorkout(id);
        ft.replace(R.id.fragment_container, details);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
