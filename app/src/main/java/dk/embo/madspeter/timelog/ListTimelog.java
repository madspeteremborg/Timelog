package dk.embo.madspeter.timelog;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import dk.embo.madspeter.timelog.contentprovider.TimelogContentProvider;
import dk.embo.madspeter.timelog.database.DBHelper;


public class ListTimelog extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private SimpleCursorAdapter mSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timelog);

        String[] dataColumns = {DBHelper.TIMELOG_COLUMN_PROJECT, DBHelper.TIMELOG_COLUMN_DATO};
        int[] viewIds = {android.R.id.text1, android.R.id.text2};
        mSimpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, dataColumns, viewIds, 0);
        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(mSimpleCursorAdapter);
        getLoaderManager().initLoader(1, null, this);

/*
        ContentValues values = new ContentValues();
        values.put(DBHelper.TIMELOG_COLUMN_DATO, "23.09.1978");
        values.put(DBHelper.TIMELOG_COLUMN_PROJECT, "Åbne gaver");
        values.put(DBHelper.TIMELOG_COLUMN_NOTE, "Det er min fødselsdag");
        values.put(DBHelper.TIMELOG_COLUMN_TIDSRUM, "08-09");

        getContentResolver().insert(TimelogContentProvider.CONTENT_URI, values);
*/





        /*
        DBHelper db = new DBHelper(this);


        db.addProject("23.09.1978", "Åbne gaver", "11-12", "Det er min fødselsdag");
        db.addProject("05.10.2014", "Vande blomster", "20-21", "Husk gødning");
        db.addProject("06.10.2014", "Vaske op", "18-19", "Brug opvasker");
        db.addProject("07.10.2014", "Fitness", "19-20", "Ryk træning");
        */

        /*
        listView = (ListView)findViewById(R.id.listView);
        List<String> list = db.selectAllProjects();
*/
        /*
        ArrayList<String> array_list = new ArrayList<String>();
        array_list.add("A");
        array_list.add("B");
        array_list.add("C");
        array_list.add("D");
        */
/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1, list);

        mListView.setAdapter(adapter);
*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*                String project = (String) mListView.getItemAtPosition(position);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("project", project);
                */
                Intent intent = new Intent(getApplicationContext(), dk.embo.madspeter.timelog.DisplayTimelog.class);
                Uri uri = Uri.parse(TimelogContentProvider.CONTENT_URI + "/" + id );

                intent.putExtra(TimelogContentProvider.CONTENT_ITEM_TYPE, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {DBHelper.TIMELOG_COLUMN_KEY_ID, DBHelper.TIMELOG_COLUMN_PROJECT, DBHelper.TIMELOG_COLUMN_DATO};
        CursorLoader cursorLoader = new CursorLoader(this, TimelogContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor>  cursorLoader, Cursor cursor){
        mSimpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){
        mSimpleCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_timelog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.action_add_timelog:
                Intent intent = new Intent(getApplicationContext(),dk.embo.madspeter.timelog.EditTimelog.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
