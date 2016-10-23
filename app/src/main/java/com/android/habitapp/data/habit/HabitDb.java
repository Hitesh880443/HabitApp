package com.android.habitapp.data.habit;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Hitesh on 10/20/2016.
 */

public class HabitDb {

    public static final String TABLE_HABIT = "habits_all";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "habitApp.db";
    private static final String COMMA_SPACE = ", ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = "PRIMARY KEY ";
    private static final String UNIQUE = "UNIQUE ";
    private static final String TYPE_TEXT = " TEXT ";
    private static final String TYPE_DATE = " DATETIME ";
    private static final String TYPE_INT = " INTEGER ";
    private static final String DEFAULT = "DEFAULT ";
    private static final String AUTOINCREMENT = "AUTOINCREMENT ";
    private static final String NOT_NULL = "NOT NULL ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";


    public static final String HABIT_SR_NO = "HABIT_SR_NO";
    public static final String HABIT_ID = "HABIT_ID";
    public static final String HABIT_NAME = "HABIT_NAME";
    public static final String HABIT_DESCIPTION = "HABIT_DESCIPTION";
    public static final String HABIT_USERS = "HABIT_USERS";


    private static final String CREATE_TABLE_HABIT =
            CREATE_TABLE + TABLE_HABIT + " ( " +
                    HABIT_SR_NO + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    HABIT_ID + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_NAME + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_DESCIPTION + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_USERS + TYPE_TEXT + NOT_NULL +
                    ")";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(DB_NAME, CREATE_TABLE_HABIT);
        db.execSQL(CREATE_TABLE_HABIT);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DB_NAME, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABIT);
        onCreate(db);
    }
}
