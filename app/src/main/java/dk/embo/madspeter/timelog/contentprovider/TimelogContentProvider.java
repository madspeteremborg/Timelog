package dk.embo.madspeter.timelog.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import dk.embo.madspeter.timelog.database.DBHelper;

/**
 * Created by MadsPeter on 29-10-2014.
 */
public class TimelogContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "dk.embo.madspeter.timelog";
    static final String URL = "content://" + PROVIDER_NAME + "/timelogs";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/timelog";


    static final int TIMELOGS = 1;
    static final int TIMELOG_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "timelogs", TIMELOGS);
        uriMatcher.addURI(PROVIDER_NAME, "timelogs/#", TIMELOG_ID);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate(){
        Context context = getContext();
        dbHelper = new DBHelper(context);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = db.insert(DBHelper.TIMELOG_TABLE_NAME, "", values);

        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHelper.TIMELOG_TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch(uriType){
            case TIMELOGS:
                break;
            case TIMELOG_ID:
                queryBuilder.appendWhere(DBHelper.TIMELOG_COLUMN_KEY_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri){
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        int uriType = uriMatcher.match(uri);

        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch(uriType){
            case TIMELOGS:
                rowsDeleted = sqlDB.delete(DBHelper.TIMELOG_TABLE_NAME, selection, selectionArgs);
                break;
            case TIMELOG_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    rowsDeleted = sqlDB.delete(DBHelper.TIMELOG_TABLE_NAME, DBHelper.TIMELOG_COLUMN_KEY_ID + "=" + id, null);
                }else{
                    rowsDeleted = sqlDB.delete(DBHelper.TIMELOG_TABLE_NAME, DBHelper.TIMELOG_COLUMN_KEY_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        int uriType = uriMatcher.match(uri);

        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType){
            case TIMELOGS:
                rowsUpdated = sqlDB.update(DBHelper.TIMELOG_TABLE_NAME, values, selection, selectionArgs);
                break;
            case TIMELOG_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHelper.TIMELOG_TABLE_NAME, values, DBHelper.TIMELOG_COLUMN_KEY_ID + "=" + id, null);
                }else {
                    rowsUpdated = sqlDB.update(DBHelper.TIMELOG_TABLE_NAME, values, DBHelper.TIMELOG_COLUMN_KEY_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return  rowsUpdated;
    }
}
