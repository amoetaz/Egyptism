package com.project.moetaz.egyptism.dataStroage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.moetaz.egyptism.models.Site;

import java.util.ArrayList;
import java.util.List;

public class DBAdpater {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    @SuppressLint("StaticFieldLeak")
    private static DBAdpater dbAdpater;
    public static final String DB_NAME = "placecs.db";
    public static final int DB_VERSION = 3;
    public static final String TABLE_NAME = "favsites";

    private static final String DESC = "desc";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String LATLONG = "latlong";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + NAME + " TEXT PRIMARY KEY , "
            + DESC + " TEXT ,"
            + IMAGE + " TEXT,"
            + LATLONG + " TEXT )";

    private DBAdpater(Context context) {
        this.context = context;
        sqLiteDatabase = new SqlHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();

    }

    public static DBAdpater getDbAdpaterInstance(Context context) {
        if (dbAdpater == null) {
            dbAdpater = new DBAdpater(context);
        }
        return dbAdpater;
    }

    public long Insert(String name, String dec, String imageUrl,String latlong) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(DESC, dec);
        cv.put(IMAGE, imageUrl);
        cv.put(LATLONG,latlong);
        return sqLiteDatabase.insert(TABLE_NAME, null, cv);
    }

    public List<Site> getData() {

        List<Site> sites = new ArrayList<>();
        String[] cols = new String[]{NAME, DESC, IMAGE,LATLONG};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, cols, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Site site = new Site();
                site.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                site.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
                site.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                site.setLatLong(cursor.getString(cursor.getColumnIndex(LATLONG)));
                sites.add(site);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return sites;
    }

    public long deleteSite(String name) {
        String whereClause = "name=?";
        String[] whereArgs = new String[] {name };
        return  sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public boolean checkSite(String name){
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME,new String []{NAME},NAME+" LIKE '%"+name+"%'",
                null,null,null,null,null);
        if (  cursor.getCount() > 0){
            return true;
        }

        cursor.close();
        return false;
    }

    ///////////////////////////////////////////////
    public long insert(ContentValues values) {
        return sqLiteDatabase.insert(TABLE_NAME,null,values);
    }

    public Cursor getAllData() {
        return sqLiteDatabase.query(TABLE_NAME
                ,new String []{DESC,NAME,IMAGE,LATLONG}
                ,null,null,null,null,null,null);
    }

    public int delete(String selection, String[] selectionArgs) {
        return sqLiteDatabase.delete(TABLE_NAME,selection,selectionArgs);
    }

    public Cursor check(String name) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,new String []{NAME
        },NAME+" LIKE '%"+name+"%'",null,null,null,null,null );
        return cursor;
    }




    private static class SqlHelper extends SQLiteOpenHelper {

        public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
