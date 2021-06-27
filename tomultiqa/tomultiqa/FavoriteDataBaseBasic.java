package com.example.android.tomultiqa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FavoriteDataBaseBasic extends SQLiteOpenHelper {

    //define basic information for database.
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Favorite.db";

    //the"constructor"of whole database.
    public FavoriteDataBaseBasic(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //sub part of database constructing methods.
    public static class DataBaseEntry implements BaseColumns {
        //basic elements of table in database.
        //using in table.
        public static final String TABLE_NAME = "Favorite";
        //these values set for using column names in code easily.
        //using in table.
        public static final String COLUMN_NAME_FavoriteTitle = "FavoriteTitle";
        public static final String COLUMN_NAME_FavoriteAnswer = "FavoriteAnswer";
        public static final String COLUMN_NAME_FavoriteHint = "FavoriteHint";
        public static final String COLUMN_NAME_FavoriteType = "FavoriteType";
    }

    //sub part of database constructing, support SQLite keywords in CREATE method.it is used in Quest table creation.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseEntry.TABLE_NAME + " (" +
                    ItemDataBaseBasic.DataBaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseEntry.COLUMN_NAME_FavoriteTitle + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_FavoriteAnswer + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_FavoriteHint + " TEXT," +
                    DataBaseEntry.COLUMN_NAME_FavoriteType + " INTEGER)";
    //sub part of database constructing,support SQLite keywords in DELETE method.it is used in Quest table creation.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteDataBaseBasic.DataBaseEntry.TABLE_NAME;

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
}
