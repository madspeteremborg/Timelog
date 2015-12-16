package dk.embo.madspeter.timelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadsPeter on 08-10-2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Timelog.db";
    private static final int DATABASE_VERSION  = 2;
    public static final String TIMELOG_TABLE_NAME = "timelogTable";
    public static final String TIMELOG_COLUMN_KEY_ID = "_id";
    public static final String TIMELOG_COLUMN_DATO = "dato";
    public static final String TIMELOG_COLUMN_PROJECT = "project";
    public static final String TIMELOG_COLUMN_TIDSRUM = "tidsrum";
    public static final String TIMELOG_COLUMN_NOTE = "note";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "create table " +
                        TIMELOG_TABLE_NAME +
                        " (" +
                        TIMELOG_COLUMN_KEY_ID  + " integer primary key, " +
                        TIMELOG_COLUMN_DATO + " text, " +
                        TIMELOG_COLUMN_PROJECT + " text, " +
                        TIMELOG_COLUMN_TIDSRUM + " text, " +
                        TIMELOG_COLUMN_NOTE + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(DBHelper.class.getName(), "Upgrading database from version: " + oldVersion + " to: " + newVersion + ". Data destroyed.");
        db.execSQL("DROP TABLE IF EXISTS " + TIMELOG_TABLE_NAME);
        onCreate(db);
    }

    protected void addProject(String dato, String project, String tidsrum, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TIMELOG_COLUMN_DATO, dato);
        contentValues.put(TIMELOG_COLUMN_PROJECT, project);
        contentValues.put(TIMELOG_COLUMN_TIDSRUM, tidsrum);
        contentValues.put(TIMELOG_COLUMN_NOTE, note);

        db.insert(TIMELOG_TABLE_NAME, null, contentValues);
        db.close();
    }

    protected String selectProject(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TIMELOG_TABLE_NAME +
                " where " + TIMELOG_COLUMN_KEY_ID + " = " + id, null);
        cursor.moveToFirst();
        String project = cursor.getString(cursor.getColumnIndex(DBHelper.TIMELOG_COLUMN_PROJECT));
        if(!cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return project;
    }

    protected List<String> selectAllProjects(){
        List<String> projectList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TIMELOG_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                projectList.add(cursor.getString(cursor.getColumnIndex(DBHelper.TIMELOG_COLUMN_PROJECT)));
            } while(cursor.moveToNext());
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return projectList;
    }

    protected void deleteProject(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMELOG_TABLE_NAME, TIMELOG_COLUMN_KEY_ID + " = ?",new String[] {String.valueOf(id)});
        db.close();
    }
}
