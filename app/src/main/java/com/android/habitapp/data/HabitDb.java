package com.android.habitapp.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Hitesh on 10/20/2016.
 */

public class HabitDb {


    // region Variable_and_Keys
    public static final String TABLE_HABIT_All = "habits_all";
    public static final String TABLE_HABIT_MY = "habits_my";
    public static final String TABLE_MOTIVE = "motive";
    public static final String TABLE_DAILY_ENREY = "table_daily_enrey";
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

    //endregion

    // region AllHabit Column
    public static final String HABIT_SR_NO = "HABIT_SR_NO";
    public static final String HABIT_ID = "HABIT_ID";
    public static final String HABIT_NAME = "HABIT_NAME";
    public static final String HABIT_DESCIPTION = "HABIT_DESCIPTION";
    public static final String HABIT_USERS = "HABIT_USERS";

    private static final String CREATE_TABLE_HABIT_ALL =
            CREATE_TABLE + TABLE_HABIT_All + " ( " +
                    HABIT_SR_NO + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    HABIT_ID + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_NAME + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_DESCIPTION + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    HABIT_USERS + TYPE_TEXT + NOT_NULL +
                    ")";

    //endregion

    // region MyHabit Column
    public static final String MY_HABIT_SR_NO = "MY_HABIT_SR_NO";
    public static final String MY_HABIT_ID = "MY_HABIT_ID";
    public static final String MY_HABIT_NAME = "MY_HABIT_NAME";
    public static final String MY_HABIT_REASON = "MY_HABIT_REASON";
    public static final String MY_HABIT_DAYS_COMPLETED = "MY_HABIT_DAYS_COMPLETED";
    public static final String MY_HABIT_START_DATE = "MY_HABIT_START_DATE";
    public static final String MY_HABIT_REMINDER_TIME = "MY_HABIT_REMINDER_TIME";
    public static final String MY_HABIT_REMINDER_STATUS = "MY_HABIT_REMINDER_STATUS";
    public static final String MY_HABIT_TODAY_STATUS = "my_habit_today_status";


    private static final String CREATE_TABLE_HABIT_MY =
            CREATE_TABLE + TABLE_HABIT_MY + " ( " +
                    MY_HABIT_SR_NO + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    MY_HABIT_ID + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_NAME + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_REASON + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_DAYS_COMPLETED + TYPE_INT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_START_DATE + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_REMINDER_TIME + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_REMINDER_STATUS + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_HABIT_TODAY_STATUS + TYPE_INT + NOT_NULL +
                    ")";

    //endregion

    // region MyHabit Daily Data Column
    public static final String MY_DAILY_SR_NO = "MY_DAILY_SR_NO";
    public static final String MY_DAILY_HABIT_ID = "MY_DAILY_HABIT_ID";
    public static final String MY_DAILY__DATE = "MY_DAILY__DATE";
    public static final String MY_DAILY_TIME = "MY_DAILY_TIME";


    private static final String CREATE_TABLE_DAILY_ENREY =
            CREATE_TABLE + TABLE_DAILY_ENREY + " ( " +
                    MY_DAILY_SR_NO + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    MY_DAILY_HABIT_ID + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_DAILY__DATE + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MY_DAILY_TIME + TYPE_TEXT + NOT_NULL +
                    ")";

    //endregion

    // region Motive Column
    public static final String MOTIVE_SR_NO = "MOTIVE_SR_NO";
    public static final String MOTIVE_ID = "MOTIVE_ID";
    public static final String MOTIVE_MSG = "MOTIVE_MSG";
    public static final String MOTIVE_AUTHOR = "MOTIVE_AUTHOR";
    public static final String MOTIVE_DATE = "MOTIVE_DATE";


    private static final String CREATE_TABLE_MOTIVE =
            CREATE_TABLE + TABLE_MOTIVE + " ( " +
                    MOTIVE_SR_NO + TYPE_INT + NOT_NULL + PRIMARY_KEY + AUTOINCREMENT + COMMA_SPACE +
                    MOTIVE_ID + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MOTIVE_MSG + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MOTIVE_AUTHOR + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                    MOTIVE_DATE + TYPE_INT + NOT_NULL +
                    ")";

    //endregion

    //region LocalMethods
    public static void onCreate(SQLiteDatabase db) {
        Log.w(DB_NAME, CREATE_TABLE_HABIT_ALL);
        db.execSQL(CREATE_TABLE_HABIT_ALL);

        Log.w(DB_NAME, CREATE_TABLE_HABIT_MY);
        db.execSQL(CREATE_TABLE_HABIT_MY);

        Log.w(DB_NAME, CREATE_TABLE_MOTIVE);
        db.execSQL(CREATE_TABLE_MOTIVE);

        Log.w(DB_NAME, CREATE_TABLE_DAILY_ENREY);
        db.execSQL(CREATE_TABLE_DAILY_ENREY);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DB_NAME, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABIT_All);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABIT_MY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTIVE);
        onCreate(db);
    }

    //endregion
}
