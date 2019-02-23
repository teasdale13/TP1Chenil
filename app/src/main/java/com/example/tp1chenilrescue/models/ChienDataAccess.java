package com.example.tp1chenilrescue.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class ChienDataAccess extends DataAccessUtils implements Serializable {


    public ChienDataAccess(SQLiteOpenHelper helper) {
        database = helper.getWritableDatabase();
        DataAccessUtils  dataAccessUtils = new DataAccessUtils();
        dataAccessUtils.setDatabase(database);
    }
}