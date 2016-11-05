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

public class HabitContentProvider extends ContentProvider {


    private static final int ALL_HABIT = 1;
    private static final int SINGLE_habit = 2;
    private static final int MY_HABIT = 3;

    private static final int MY_SINGLE_habit = 4;

    private static final int MOTIVE_ALL = 5;
    private static final int MOTIVE_SIGNLE = 6;

    private static final int DAILY_ALL = 7;
    private static final int DAILY_SIGNLE = 8;

    private static final String AUTHORITY = "com.android.habitapp.data.HabitContentProvider";
    // create content URIs from the authority by appending path to database table
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/habits");
    public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/myhabits");
    public static final Uri CONTENT_URI3 = Uri.parse("content://" + AUTHORITY + "/motive");
    public static final Uri CONTENT_URI4 = Uri.parse("content://" + AUTHORITY + "/daily");
    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "habits", ALL_HABIT);
        uriMatcher.addURI(AUTHORITY, "habits/#", SINGLE_habit);

        uriMatcher.addURI(AUTHORITY, "myhabits", MY_HABIT);
        uriMatcher.addURI(AUTHORITY, "myhabits/#", MY_SINGLE_habit);

        uriMatcher.addURI(AUTHORITY, "motive", MOTIVE_ALL);
        uriMatcher.addURI(AUTHORITY, "motive/#", MOTIVE_SIGNLE);

        uriMatcher.addURI(AUTHORITY, "daily", DAILY_ALL);
        uriMatcher.addURI(AUTHORITY, "daily/#", DAILY_SIGNLE);
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

        String id;
        switch (uriMatcher.match(uri)) {

            case ALL_HABIT:
                queryBuilder.setTables(HabitDb.TABLE_HABIT_All);
                break;
            case SINGLE_habit:
                queryBuilder.setTables(HabitDb.TABLE_HABIT_All);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(HabitDb.HABIT_SR_NO + "=" + id);
                break;
            case MY_HABIT:
                queryBuilder.setTables(HabitDb.TABLE_HABIT_MY);
                break;
            case MY_SINGLE_habit:
                queryBuilder.setTables(HabitDb.TABLE_HABIT_MY);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(HabitDb.MY_HABIT_SR_NO + "=" + id);
                break;

            case MOTIVE_ALL:
                queryBuilder.setTables(HabitDb.TABLE_MOTIVE);
                break;
            case MOTIVE_SIGNLE:
                queryBuilder.setTables(HabitDb.TABLE_MOTIVE);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(HabitDb.MOTIVE_SR_NO + "=" + id);
                break;

            case DAILY_ALL:
                queryBuilder.setTables(HabitDb.TABLE_DAILY_ENREY);
                break;
            case DAILY_SIGNLE:
                queryBuilder.setTables(HabitDb.TABLE_DAILY_ENREY);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(HabitDb.MY_DAILY_HABIT_ID + "=" + id);
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
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                id = db.insert(HabitDb.TABLE_HABIT_All, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_habit:
                id = db.insert(HabitDb.TABLE_HABIT_All, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MY_HABIT:
                id = db.insert(HabitDb.TABLE_HABIT_MY, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MY_SINGLE_habit:
                id = db.insert(HabitDb.TABLE_HABIT_MY, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case MOTIVE_ALL:
                id = db.insert(HabitDb.TABLE_MOTIVE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MOTIVE_SIGNLE:
                id = db.insert(HabitDb.TABLE_MOTIVE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case DAILY_ALL:
                id = db.insert(HabitDb.TABLE_DAILY_ENREY, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DAILY_SIGNLE:
                id = db.insert(HabitDb.TABLE_DAILY_ENREY, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return Uri.parse(uri.toString() + "/" + id);
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
        String id;
        int updateCount = -5;
        switch (uriMatcher.match(uri)) {
            case ALL_HABIT:
                //do nothing
                break;
            case SINGLE_habit:
                id = uri.getPathSegments().get(1);
                selection = HabitDb.HABIT_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;

            case MY_HABIT:

                updateCount = db.update(HabitDb.TABLE_HABIT_MY, values, null, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case MY_SINGLE_habit:
                id = uri.getPathSegments().get(1);
                selection = HabitDb.MY_HABIT_SR_NO + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(HabitDb.TABLE_HABIT_MY, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return updateCount;
    }
}
