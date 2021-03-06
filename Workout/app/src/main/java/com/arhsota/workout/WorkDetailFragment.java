package com.arhsota.workout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDetailFragment extends Fragment {

    private long workoutId;



    public WorkDetailFragment() {
        // Required empty public constructor
//        см страницу 310 выделено отдельно
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            workoutId = savedInstanceState.getLong("workoutId");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_detail, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.textTitle);
            Workout workout = Workout.workouts[(int) workoutId];
            title.setText(workout.getName());
            TextView description = (TextView) view.findViewById(R.id.textDescription);
            description.setText(workout.getDescription());
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("workoutId", workoutId);
    }
    public void setWorkout(long id) {
            this.workoutId = id;

        }

    }
