package com.example.tp1chenilrescue.models;

import android.database.sqlite.SQLiteDatabase;

public class ChenilChienDataAccess extends DataAccessUtils {


    public ChenilChienDataAccess(DatabaseHelper helper) {
        database = helper.getWritableDatabase();
        DataAccessUtils  dataAccessUtils = new DataAccessUtils();
        dataAccessUtils.setDatabase(database);
    }
}
