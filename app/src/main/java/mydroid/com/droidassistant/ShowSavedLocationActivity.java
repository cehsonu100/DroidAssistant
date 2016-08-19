package mydroid.com.droidassistant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import mydroid.com.droidassistant.database.DBhelper;
import mydroid.com.droidassistant.database.SQLController;

public class ShowSavedLocationActivity extends AppCompatActivity {

    SQLController sqlController;
    DBhelper dbHelper;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location);
        myList = (ListView) findViewById(R.id.listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sqlController = new SQLController(this);
        sqlController.open();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation();
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllLocation();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateListViewFromDB();
    }

    public void addLocation() {
        Intent intent = new Intent(this, PlaceRingerVolumeSetter.class); //going to middle activity DisplayDB.class
        startActivity(intent);
    }

    public void deleteAllLocation(){
        sqlController.deleteAll();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void populateListViewFromDB() {
        String[] fromFieldNames = new String[]{DBhelper.PLACE_NAME, DBhelper.RADIUS, DBhelper.MODE};
        int[] toViewIDs = new int[]{R.id.place_name, R.id.radius, R.id.mode};
        Cursor cursor = sqlController.fetch();
        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,        // Context
                        R.layout.item_layout,    // Row layout template
                        cursor,                    // cursor (set of DB records to map)
                        fromFieldNames,            // DB Column names
                        toViewIDs                // View IDs to put information in
                );

        if (cursor.moveToFirst()) {
            myList.setAdapter(myCursorAdapter);

        } else {
            myList.setEmptyView(findViewById(R.id.empty_view));
        }
    }
}
