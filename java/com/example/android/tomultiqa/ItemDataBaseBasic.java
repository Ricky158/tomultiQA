package com.example.android.tomultiqa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ItemDataBaseBasic extends SQLiteOpenHelper {

    //define basic information for database.
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Item.db";

    //the"constructor"of whole database.
    public ItemDataBaseBasic(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //sub part of database constructing methods.
    public static class DataBaseEntry implements BaseColumns {
        //basic elements of table in database.
        //using in table.
        public static final String TABLE_NAME = "Item";
        //these values set for using column names in code easily.
        //using in table.
        public static final String COLUMN_NAME_ItemName = "ItemName";
        public static final String COLUMN_NAME_ItemType = "ItemType";
        public static final String COLUMN_NAME_ItemText = "ItemText";
        public static final String COLUMN_NAME_ItemNumber = "ItemNumber";
    }

    //sub part of database constructing, support SQLite keywords in CREATE method.it is used in Quest table creation.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemDataBaseBasic.DataBaseEntry.TABLE_NAME + " (" +
                    ItemDataBaseBasic.DataBaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseEntry.COLUMN_NAME_ItemName + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_ItemType + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_ItemText + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_ItemNumber + " INTEGER)";
    //sub part of database constructing,support SQLite keywords in DELETE method.it is used in Quest table creation.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemDataBaseBasic.DataBaseEntry.TABLE_NAME;

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
