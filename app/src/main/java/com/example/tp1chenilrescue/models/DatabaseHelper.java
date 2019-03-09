package com.example.tp1chenilrescue.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DBNAME = "chenilandreinc.db";
    private static int VERSION = 1;

    public DatabaseHelper(@Nullable Context context){
        super( context, DBNAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "CREATE TABLE chenil ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "nom TEXT NOT NULL, adresse TEXT, ville TEXT, code_postal TEXT(6), longitude REAL," +
                "latitude REAL );");

        db.execSQL( "CREATE TABLE chien (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,nom TEXT," +
                " date_naissance TEXT,mere INTEGER, pere INTEGER, race_id INTEGER NOT NULL, sexe TEXT NOT NULL," +
                " state TEXT NOT NULL DEFAULT \"OWN\"," +
                "FOREIGN KEY(mere) REFERENCES chien(id), " +
                "FOREIGN KEY(pere) REFERENCES chien(id)," +
                "FOREIGN KEY(race_id) REFERENCES race(id));" );

        db.execSQL( "CREATE TABLE chenil_chien(chenil_id INTEGER NOT NULL," +
                " chien_id INTEGER NOT NULL, PRIMARY KEY(chenil_id, chien_id), " +
                "FOREIGN KEY(chenil_id) REFERENCES chenil(id), " +
                "FOREIGN KEY(chien_id) REFERENCES chien(id));" );

        db.execSQL( "CREATE TABLE race (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nom TEXT NOT NULL," +
                "pays TEXT NOT NULL, note TEXT);" );

        db.execSQL( "CREATE TABLE poids (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, date TEXT NOT NULL," +
                "poids DOUBLE NOT NULL, chien_id INTEGER, " +
                "FOREIGN KEY(chien_id) REFERENCES chien(id));" );

        insertLabradorIntoRace(db);
        insertBergerAllemandIntoRace(db);
        insertCanicheIntoRace(db);
        insertTeckelIntoRace(db);


    }

    private void insertTeckelIntoRace(SQLiteDatabase db) {
        ContentValues values = new ContentValues(  );
        values.put( RaceTable.NAME, "Teckel" );
        values.put( RaceTable.COUNTRY, "Allemagne" );
        values.put( RaceTable.NOTE, "À l’aise en appartement" );

        db.insert( RaceTable.TABLE_NAME, null,values );
    }

    private void insertCanicheIntoRace(SQLiteDatabase db) {
        ContentValues values = new ContentValues(  );
        values.put( RaceTable.NAME, "Caniche" );
        values.put( RaceTable.COUNTRY, "France" );
        values.put( RaceTable.NOTE, "Aime les enfants" );

        db.insert( RaceTable.TABLE_NAME, null,values );
    }

    private void insertBergerAllemandIntoRace(SQLiteDatabase db) {
        ContentValues values = new ContentValues(  );
        values.put( RaceTable.NAME, "Berger Allemand" );
        values.put( RaceTable.COUNTRY, "Allemagne" );
        values.put( RaceTable.NOTE, "Sportif" );

        db.insert( RaceTable.TABLE_NAME, null,values );
    }

    private void insertLabradorIntoRace(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( RaceTable.NAME, "Labrador" );
        contentValues.put( RaceTable.COUNTRY, "Grande-Bretagne" );
        contentValues.put( RaceTable.NOTE, "Affectueux" );

        database.insert( RaceTable.TABLE_NAME, null, contentValues );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
