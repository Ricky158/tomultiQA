package com.example.android.tomultiqa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

//"final class" means it can`t be extended or be implemented(abstract it), it only provide stable and unchanged logic, so there is no necessary to change it.
//provide basic SQLite database support for store Quest, refer: https://www.jianshu.com/p/5c33be6ce89d !
//2021.2.9: Most of code were replaced with https://developer.android.google.cn/training/data-storage/sqlite#java(with some modification)
//2021.3.14: support: https://stackoverflow.com/questions/513084/ship-an-application-with-a-database?answertab=votes#tab-top !
public final class QuestDataBaseBasic extends SQLiteOpenHelper {
    //define basic information for database.
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Quest.db";
    public static final String TABLE_NAME = "Quest";
    //these values set for using column names in code easily.
    //using in Quest table.
    public static final String COLUMN_NAME_QuestTitle = "QuestTitle";
    public static final String COLUMN_NAME_QuestAnswer = "QuestAnswer";
    public static final String COLUMN_NAME_QuestHint = "QuestHint";
    public static final String COLUMN_NAME_QuestLevel = "QuestLevel";
    public static final String COLUMN_NAME_QuestType = "QuestType";

    //the"constructor"of whole database.
    public QuestDataBaseBasic(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //sub part of database constructing methods.
    public static class DataBaseEntry implements BaseColumns {
        //basic elements of table in database.
        //using in Quest table.
        public static final String TABLE_NAME = "Quest";
        public static final String COLUMN_NAME_QuestTitle = "QuestTitle";
        public static final String COLUMN_NAME_QuestAnswer = "QuestAnswer";
        public static final String COLUMN_NAME_QuestHint = "QuestHint";
        public static final String COLUMN_NAME_QuestLevel = "QuestLevel";
        public static final String COLUMN_NAME_QuestType = "QuestType";
    }

    //sub part of database constructing, support SQLite keywords in CREATE method.it is used in Quest table creation.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseEntry.TABLE_NAME + " (" +
                    DataBaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseEntry.COLUMN_NAME_QuestTitle + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_QuestAnswer + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_QuestHint + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_QuestLevel + " INTEGER," +
                    DataBaseEntry.COLUMN_NAME_QuestType + " TEXT)";
    //sub part of database constructing,support SQLite keywords in DELETE method.it is used in Quest table creation.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataBaseEntry.TABLE_NAME;

    //Using methods to transform the SQLite command to java code.
    //Defying Strings to let API know that`s SQLite command.And reform it to be easy to use.(Just call the method with arguments.)
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    //these methods will not be used in reality, because we just update database by update entire APP.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    //these methods will not be used in reality, because we just update database by update entire APP.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void DeleteEntire (SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
