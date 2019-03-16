package com.example.tp1chenilrescue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.ChenilDataAccess;
import com.example.tp1chenilrescue.models.ChenilTable;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.ChienTable;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.example.tp1chenilrescue.models.Poids;
import com.example.tp1chenilrescue.models.PoidsDataAccess;
import com.example.tp1chenilrescue.models.PoidsTable;
import com.example.tp1chenilrescue.models.Race;
import com.example.tp1chenilrescue.models.RaceDataAccess;
import com.example.tp1chenilrescue.models.RaceTable;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private RaceDataAccess raceDataAccess;
    private ChienDataAccess chienDataAccess;
    private ChenilDataAccess chenilDataAccess;
    private PoidsDataAccess poidsDataAccess;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private Context context;


    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        String dbName = "maDB.db";
        context.deleteDatabase( dbName );
        helper.DBNAME = dbName;
        helper = new DatabaseHelper( context );
        raceDataAccess = new RaceDataAccess( helper );
        chienDataAccess = new ChienDataAccess( helper );
        chenilDataAccess = new ChenilDataAccess( helper );
        poidsDataAccess = new PoidsDataAccess( helper );
        db = helper.getWritableDatabase();

    }


    // TEST SUR LES INSERTIONS DE DONNÉES ----------------------------------------

    @Test
    public void testDogInsert(){

        Chien chien = new Chien(  );
        chien.setSexe( "F" );
        chien.setNom( "Maurice" );
        chien.setDate_naissance( "12/12/2012" );
        chien.setIdPere( 0 );
        chien.setMereId( 0 );
        chien.setRaceId( 1 );

        long inserted = chienDataAccess.insertDog( chien );

        String[] columns = {ChienTable.ID,ChienTable.SEXE,ChienTable.PERE,ChienTable.MERE, ChienTable.NAME,
        ChienTable.RACE,ChienTable.DATE};
        String where = "id =?";

        Cursor c = db.query( ChienTable.TABLE_NAME,columns,where,new String[] {String.valueOf( (int)inserted )},
                null,null,null);

        c.moveToFirst();
        assertThat( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ), is( (int)inserted ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ), is( chien.getNom() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) ), is( chien.getRaceId() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( ChienTable.PERE ) ), is( chien.getIdPere() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( ChienTable.MERE ) ), is( chien.getMereId() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( ChienTable.DATE ) ), is( chien.getDate_naissance() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( ChienTable.SEXE ) ), is( chien.getSexe() ) );


    }

    @Test
    public void testKennelInsert(){
        Chenil chenil = new Chenil(  );
        chenil.setName( "Chenil Patates Douces" );
        chenil.setAddress( "2134 rue du Navet" );
        chenil.setVille( "Shawinigan" );
        chenil.setCode_postal( "G9Y3T7" );
        chenil.setLatitude( -12.32423f );
        chenil.setLongitude( 23.45234f );

        long id = chenilDataAccess.insertKennel( chenil );

        String where = "id =?";
        String[] columns = {ChenilTable.NAME,ChenilTable.ADDRESS, ChenilTable.VILLE, ChenilTable.CODE_POSTAL,
        ChenilTable.LATITUDE,ChenilTable.LONGITUDE, ChenilTable.ID};

        Cursor c = db.query( ChenilTable.TABLE_NAME, columns,where, new String[] {String.valueOf( (int)id )},null,
                null,null);

        c.moveToFirst();
        assertThat( c.getString( c.getColumnIndexOrThrow( ChenilTable.NAME ) ), is( chenil.getName() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( ChenilTable.ID ) ), is( (int)id ));
        assertThat( c.getString( c.getColumnIndexOrThrow( ChenilTable.ADDRESS ) ), is( chenil.getAddress() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( ChenilTable.VILLE ) ), is( chenil.getVille() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( ChenilTable.CODE_POSTAL ) ), is( chenil.getCode_postal() ) );
        assertThat( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LATITUDE ) ), is( chenil.getLatitude() ) );
        assertThat( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LONGITUDE ) ), is( chenil.getLongitude() ) );

    }


    @Test
    public void testBreedInsert(){
        Race race = new Race(  );
        race.setName( "Labrador" );
        race.setCountry( "Zimbabwe" );
        race.setNote( "Chien trop parfait, doit être un extra-terrestre" );

        long id = raceDataAccess.insertBreed( race );

        String where = "id =?";
        String[] columns = {RaceTable.NAME,RaceTable.ID,RaceTable.COUNTRY, RaceTable.NOTE};

        Cursor c = db.query( RaceTable.TABLE_NAME, columns, where, new String[] {String.valueOf( (int)id )},
                null, null,null);

        c.moveToFirst();
        assertThat( c.getInt( c.getColumnIndexOrThrow( RaceTable.ID ) ), is( (int)id ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( RaceTable.NAME ) ), is( race.getName() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( RaceTable.COUNTRY ) ), is( race.getCountry() ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( RaceTable.NOTE ) ), is( race.getNote() ) );
    }

    @Test
    public void testWeightInsert(){
        Poids poids = new Poids(  );
        poids.setDate( "24/12/2012" );
        poids.setPoids( "123.43" );
        poids.setDog_id( 1 );

        long id = poidsDataAccess.insertMedicalExam( poids, poids.getDog_id() );

        String where = "id =?";
        String[] columns = {PoidsTable.WEIGHT, PoidsTable.ID_DOG, PoidsTable.ID, PoidsTable.DATE};

        Cursor c = db.query( PoidsTable.TABLE_NAME,columns,where,new String[] {String.valueOf( (int)id )},
                null,null,null);

        c.moveToFirst();
        assertThat( c.getString( c.getColumnIndexOrThrow( PoidsTable.DATE ) ), is( poids.getDate() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( PoidsTable.ID ) ), is( (int)id ) );
        assertThat( c.getString( c.getColumnIndexOrThrow( PoidsTable.WEIGHT ) ), is( poids.getPoids() ) );
        assertThat( c.getInt( c.getColumnIndexOrThrow( PoidsTable.ID_DOG ) ), is( poids.getDog_id() ) );
    }

    // A TERMINER ---------------------------------------
    @Test
    public void testKennelDogInsert(){
        Chenil chenil = new Chenil(  );
        chenil.setName( "Chenil Patates Douces" );
        chenil.setAddress( "2134 rue du Navet" );
        chenil.setVille( "Shawinigan" );
        chenil.setCode_postal( "G9Y3T7" );
        chenil.setLatitude( -12.32423f );
        chenil.setLongitude( 23.45234f );

        Chien chien = new Chien(  );
        chien.setSexe( "F" );
        chien.setNom( "Maurice" );
        chien.setDate_naissance( "12/12/2012" );
        chien.setIdPere( 0 );
        chien.setMereId( 0 );
        chien.setRaceId( 1 );

        long dogId = chienDataAccess.insertDog( chien );

        long kennelId = chenilDataAccess.insertKennel( chenil );

        long kennelRow = chenilDataAccess.setKennelIdToDog( (int)dogId, (int)kennelId );

    }



    // TEST SUR LES SUPPRESSIONS DE DONNÉES --------------------------------------

    @Test
    public void testDogDelete(){
        Chien chien = new Chien(  );
        chien.setSexe( "F" );
        chien.setNom( "Maurice" );
        chien.setDate_naissance( "12/12/2012" );
        chien.setIdPere( 0 );
        chien.setMereId( 0 );
        chien.setRaceId( 1 );

        long id = chienDataAccess.insertDog( chien );

        int rowId = chienDataAccess.deleteDogById( (int)id );

        // test si le nombre de row effacé est 1, puisque efface seulement 1 row.
        assertThat( rowId, is( (int) id) );
    }

    @Test
    public void testKennelDelete(){
        Chenil chenil = new Chenil(  );
        chenil.setName( "Chenil Patates Douces" );
        chenil.setAddress( "2134 rue du Navet" );
        chenil.setVille( "Shawinigan" );
        chenil.setCode_postal( "G9Y3T7" );
        chenil.setLatitude( -12.32423f );
        chenil.setLongitude( 23.45234f );

        long id = chenilDataAccess.insertKennel( chenil );

        int rowId = chenilDataAccess.deleteKennelById( (int)id );

        assertThat( rowId, is( 1 ) );

    }

    @Test
    public void testBreedDelete(){
        Race race = new Race(  );
        race.setName( "Labrador" );
        race.setCountry( "Zimbabwe" );
        race.setNote( "Chien trop parfait, doit être un extra-terrestre" );

        long id = raceDataAccess.insertBreed( race );

        int rowId = raceDataAccess.deleteBreedById( (int)id );

        assertThat( rowId, is( 1 ) );
    }

    @Test
    public void testWeightDelete(){
        Poids poids = new Poids(  );
        poids.setDate( "24/12/2012" );
        poids.setPoids( "123.43" );
        poids.setDog_id( 1 );

        long id = poidsDataAccess.insertMedicalExam( poids, poids.getDog_id() );

        int rowId = poidsDataAccess.deleteMedicalById( (int)id );

        assertThat( rowId, is( 1 ) );
    }

    @Test
    public void testKennelDogDelete(){

    }



    // TEST SUR LES UPDATE DES DONNÉES ---------------------------------------------



}
