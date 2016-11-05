package com.android.habitapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Hitesh on 10/20/2016.
 */

public class HabitContentProviderBackup extends ContentProvider {


    private static final int ALL_HABIT = 1;
    private static final int SINGLE_habit = 2;
    private static final int MY_HABIT = 3;
    private static final int MY_SINGLE_habit = 4;
    private static final String AUTHORITY = "com.android.habitapp.data.HabitContentProvider";
    // create content URIs from the authority by appending path to database table
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/habits");
    public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/myhabits");
    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "habits", ALL_HABIT);
        uriMatcher.addURI(AUTHORITY, "habits/#", SINGLE_habit);
        uriMatcher.addURI(AUTHORITY, "myhabits", ALL_HABIT);
        uriMatcher.addURI(AUTHORITY, "myhabits/#", SINGLE_habit);
    }

    private HabitDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new HabitDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(HabitDb.TABLE_HABIT_All);

        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                //do nothing
                break;
            case SINGLE_habit:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(HabitDb.HABIT_SR_NO + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                return "vnd.android.cursor.dir/vnd.com.android.habitapp.data.HabitContentProvider.habits";
            case SINGLE_habit:
                return "vnd.android.cursor.item/vnd.com.android.habitapp.data.HabitContentProvider.habits";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(HabitDb.TABLE_HABIT_All, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                //do nothing
                break;
            case SINGLE_habit:
                String id = uri.getPathSegments().get(1);
                selection = HabitDb.HABIT_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(HabitDb.TABLE_HABIT_All, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                //do nothing
                break;
            case SINGLE_habit:
                String id = uri.getPathSegments().get(1);
                selection = HabitDb.HABIT_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(HabitDb.TABLE_HABIT_All, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
