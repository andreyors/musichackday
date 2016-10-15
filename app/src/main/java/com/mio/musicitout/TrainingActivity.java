package com.mio.musicitout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity {

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
