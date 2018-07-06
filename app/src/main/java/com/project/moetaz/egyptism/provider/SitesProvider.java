package com.project.moetaz.egyptism.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.project.moetaz.egyptism.dataStroage.DBAdpater;

public class SitesProvider  extends ContentProvider {

    public static final String AUTHORITY = "com.project.moetaz.egyptism";

    public static final String PATH_SITES_LIST = "SITES_LIST";
    public static final String PATH_SITES_ID = "SITE_ID";

    public static final int SITES_LIST = 1;
    public static final int SITE_ID = 2;

    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+"vnd.com.moetaz.sites";
    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+"vnd.com.moetaz.siteid";

    public static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        MATCHER.addURI(AUTHORITY, PATH_SITES_LIST, SITES_LIST);
        MATCHER.addURI(AUTHORITY, PATH_SITES_ID, SITE_ID);
    }
    private DBAdpater dbAdadpter;


    @Override
    public boolean onCreate() {
        dbAdadpter = DBAdpater.getDbAdpaterInstance(getContext());
        return true ;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)throws UnsupportedOperationException {
        Cursor cursor = null;
        switch (MATCHER.match(uri)){
            case SITES_LIST:  cursor = dbAdadpter.getAllData(); break;
            case SITE_ID: cursor = dbAdadpter.check(selectionArgs[0]); break;

            default: cursor = null; break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)){
            case SITES_LIST:return MIME_TYPE_1;
            case SITE_ID:return MIME_TYPE_2;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) throws UnsupportedOperationException{
        Uri Returnuri = null;
        switch (MATCHER.match(uri)){

            case SITES_LIST: Returnuri = insertMovie(uri,values);break;
            default: new UnsupportedOperationException("Error") ;break;
        }
        return Returnuri;
    }

    private Uri insertMovie(Uri uri, ContentValues values) {
        long id = dbAdadpter.insert(values);
        getContext().getContentResolver().notifyChange(uri,null);

        return  Uri.parse("content://"+AUTHORITY+"/"+ PATH_SITES_LIST +"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws UnsupportedOperationException {
        int deleteCount = -1;
        switch (MATCHER.match(uri)){

            case SITES_LIST: deleteCount = delete(selection,selectionArgs);break;
            default: new UnsupportedOperationException("Error") ;break;
        }
        return deleteCount;
    }

    private int delete(String selection, String[] selectionArgs) {
        return dbAdadpter.delete(selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}