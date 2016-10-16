package com.mio.musicitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TrainingListAdapter mAdapter;
    private boolean isListView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
        mAdapter = new TrainingListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        isListView = true;
    }


    TrainingListAdapter.OnItemClickListener onItemClickListener = new TrainingListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(TrainingActivity.this, TrainingDetailActivity.class);
            intent.putExtra(TrainingDetailActivity.EXTRA_PARAM_ID, position);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.addSubMenu(1, Menu.FIRST, 0, "Settings");
        menu.addSubMenu(2, Menu.NONE, 0, "About");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case -1:
                break;

            case Menu.FIRST:
                Intent SettingsIntent = new Intent(TrainingActivity.this, SettingsActivity.class);
                TrainingActivity.this.startActivity(SettingsIntent);
                break;

            case Menu.NONE:
                Toast.makeText(getApplicationContext(), "Stay tuned. Sportify team", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
