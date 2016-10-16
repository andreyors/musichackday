package com.mio.musicitout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TrainingDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "training_id";

    private ExerciseListAdapter mAdapter;
    private ListView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);

        mAdapter = new ExerciseListAdapter(this);

        mAdapter.addSectionHeaderItem("Round #1 of 5");

        mAdapter.addItem("Jumping jacks");
        mAdapter.addItem("Wall sit");
        mAdapter.addItem("Push-ups");
        mAdapter.addItem("Abdominal crunch");
        mAdapter.addItem("Step-up onto chair");

        mAdapter.addSectionHeaderItem("Round #2 of 5");

        mAdapter.addItem("Squat");
        mAdapter.addItem("Triceps dip on chair");
        mAdapter.addItem("Plank");
        mAdapter.addItem("High knees running in place");
        mAdapter.addItem("Lunge");

        mRecyclerView = (ListView) findViewById(R.id.exercises);
        mRecyclerView.setAdapter(mAdapter);
    }
}
