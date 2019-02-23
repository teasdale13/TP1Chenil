package com.example.tp1chenilrescue.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoidsDataAccess extends DataAccessUtils {


    public PoidsDataAccess(SQLiteOpenHelper helper){
        database = helper.getWritableDatabase();
        DataAccessUtils  dataAccessUtils = new DataAccessUtils();
        dataAccessUtils.setDatabase(database);
    }

}
