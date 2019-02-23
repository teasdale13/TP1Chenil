package com.example.tp1chenilrescue.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChenilDataAccess extends DataAccessUtils {


    public ChenilDataAccess(SQLiteOpenHelper helper){
        database = helper.getWritableDatabase();
        DataAccessUtils  dataAccessUtils = new DataAccessUtils();
        dataAccessUtils.setDatabase(database);
    }

}
