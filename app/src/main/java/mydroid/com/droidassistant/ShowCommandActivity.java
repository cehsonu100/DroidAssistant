package mydroid.com.droidassistant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowCommandActivity extends AppCompatActivity {
    String[] command_list=new String[]{"turn on wifi","turn off wifi","wifi बंद कर दो","wifi स्टार्ट कर दो","call xyz" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_command);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view, command_list);
        ListView listView = (ListView)findViewById(R.id.command_list_view);
        listView.setAdapter(adapter);
    }

}
