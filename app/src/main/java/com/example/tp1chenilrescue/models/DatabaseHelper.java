package com.example.tp1chenilrescue.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DBNAME = "chenilandreinc.db";
    private static int VERSION = 2;

    public DatabaseHelper(@Nullable Context context){
        super( context, DBNAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "CREATE TABLE chenil ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "nom TEXT NOT NULL, adresse TEXT, ville TEXT, code_postal TEXT(6), longitude REAL," +
                "latitude REAL );");

        db.execSQL( "CREATE TABLE race (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nom TEXT NOT NULL," +
                "pays TEXT NOT NULL, note TEXT);" );

        db.execSQL( "CREATE TABLE chien (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,nom TEXT," +
                " date_naissance TEXT,mere INTEGER, pere INTEGER, race_id INTEGER NOT NULL, sexe TEXT NOT NULL," +
                " state TEXT NOT NULL," +
                "FOREIGN KEY(mere) REFERENCES chien(id), " +
                "FOREIGN KEY(pere) REFERENCES chien(id)," +
                "FOREIGN KEY(race_id) REFERENCES race(id));" );

        db.execSQL( "CREATE TABLE chenil_chien(chenil_id INTEGER NOT NULL," +
                " chien_id INTEGER NOT NULL, PRIMARY KEY(chenil_id, chien_id), " +
                "FOREIGN KEY(chenil_id) REFERENCES chenil(id), " +
                "FOREIGN KEY(chien_id) REFERENCES chien(id));" );


        db.execSQL( "CREATE TABLE poids (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, date TEXT NOT NULL," +
                "poids DOUBLE NOT NULL, chien_id INTEGER, " +
                "FOREIGN KEY(chien_id) REFERENCES chien(id));" );

        insertBreed( db,"Teckel","Allemagne", "À l’aise en appartement");
        insertBreed( db,"Caniche","France", "Aime les enfants");
        insertBreed( db,"Berger Allemand","Allemagne", "Sportif");
        insertBreed( db,"Labrador","Grande-Bretagne", "Affectueux");

    }

    private void insertBreed(SQLiteDatabase db,String name, String country, String note){
        ContentValues values = new ContentValues(  );
        values.put( RaceTable.NAME, name);
        values.put( RaceTable.COUNTRY, country );
        values.put( RaceTable.NOTE, note );

        db.insert( RaceTable.TABLE_NAME, null,values );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL( "ALTER TABLE " + ChienTable.TABLE_NAME + " ADD COLUMN " + ChienTable.STATE + " TEXT" );
            ArrayList<Chien> maListe = updateDogInDB(db);

            for (Chien chien: maListe) {
                String where = "id = ?";
                ContentValues values = new ContentValues(  );
                values.put( ChienTable.STATE, ChienTable.STATE_IN );
                db.update( ChienTable.TABLE_NAME, values,where,new String[]{String.valueOf( chien.getId() )} );

            }
        }

    }


    private ArrayList<Chien> updateDogInDB(SQLiteDatabase db) {

        ArrayList<Chien> chiens = new ArrayList<>();

        Cursor c = db.query( ChienTable.TABLE_NAME,
                    null, null, null,
                    null, null, null );
        while (c.moveToNext()) {
            Chien chien = new Chien();

            chien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );

            chiens.add( chien );
        }
        c.close();

        return chiens;

    }
}
