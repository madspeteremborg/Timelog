package dk.embo.madspeter.timelog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dk.embo.madspeter.timelog.contentprovider.TimelogContentProvider;
import dk.embo.madspeter.timelog.database.DBHelper;


public class DisplayTimelog extends Activity {

    /* comment */
    private TextView mProject;
    private TextView mDate;
    private TextView mTime;
    private TextView mNote;

    private Uri timelogUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_timelog);

        mProject = (TextView)findViewById(R.id.textProject);
        mDate = (TextView)findViewById(R.id.textDate);
        mTime = (TextView)findViewById(R.id.textTime);
        mNote = (TextView)findViewById(R.id.textNote);

        Bundle extras = getIntent().getExtras();

        if( savedInstanceState == null){
            timelogUri = null;
        }else{
            timelogUri = (Uri)savedInstanceState.getParcelable(TimelogContentProvider.CONTENT_ITEM_TYPE);
        }
        if(extras != null){
            timelogUri = extras.getParcelable(TimelogContentProvider.CONTENT_ITEM_TYPE);
            fillData(timelogUri);
        }

    }

    private void fillData(Uri uri){
        String[] projection = {DBHelper.TIMELOG_COLUMN_DATO,
                DBHelper.TIMELOG_COLUMN_PROJECT,
                DBHelper.TIMELOG_COLUMN_TIDSRUM,
                DBHelper.TIMELOG_COLUMN_NOTE};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();

            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMELOG_COLUMN_DATO));
            mDate.setText(date);

            String project = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMELOG_COLUMN_PROJECT));
            mProject.setText(project);

            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMELOG_COLUMN_TIDSRUM));
            mTime.setText(time);

            String note = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMELOG_COLUMN_NOTE));
            mNote.setText(note);

            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(TimelogContentProvider.CONTENT_ITEM_TYPE, timelogUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_timelog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.edit_timelog:
                Intent intent = new Intent(getApplicationContext(),dk.embo.madspeter.timelog.EditTimelog.class);
                startActivityForResult(intent,1);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
