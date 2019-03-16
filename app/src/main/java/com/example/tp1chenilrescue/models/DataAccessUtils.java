package com.example.tp1chenilrescue.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 *
 * Classe qui servira à faire toutes les requêtes à la Database pour simplifier
 * les manipulations.
 */
public class DataAccessUtils {

    protected SQLiteDatabase database;


    public DataAccessUtils() {
    }

    protected void setDatabase(SQLiteDatabase db) {
        database = db;
    }



    public int setChenilIdToNull(int chienId , int chenilId){
        String where = ChienTable.ID + " = ?";
        ContentValues values = new ContentValues(  );
        values.putNull( ChienTable.CHENIL_ID);

        return database.update( ChienTable.TABLE_NAME, values, where ,new String[] {String.valueOf( chienId ) });
    }


    public int deleteDogById(int id) {
        ContentValues values = new ContentValues(  );
        values.put( ChienTable.STATE , ChienTable.STATE_OUT );
        String where = ChienTable.ID + " = ?";

      return database.update( ChienTable.TABLE_NAME, values ,where, new String[] {String.valueOf( id )});
    }

    public int deleteMedicalById(int id) {
        String where = PoidsTable.ID + " = ?";

        return database.delete( PoidsTable.TABLE_NAME , where, new String[] {String.valueOf( id )});
    }

    public boolean updateKennel(Chenil mChenil) {
        String whereClause = ChenilTable.ID + " = ?";

        ContentValues contentValues = new ContentValues();
        contentValues.put( ChenilTable.ID, mChenil.getId() );
        contentValues.put( ChenilTable.NAME, mChenil.getName() );
        contentValues.put( ChenilTable.ADDRESS, mChenil.getAddress() );
        contentValues.put( ChenilTable.VILLE, mChenil.getVille() );
        contentValues.put( ChenilTable.CODE_POSTAL, mChenil.getCode_postal() );
        contentValues.put( ChenilTable.LATITUDE, mChenil.getLatitude() );
        contentValues.put( ChenilTable.LONGITUDE, mChenil.getLongitude() );

        int row = database.update( ChenilTable.TABLE_NAME, contentValues, whereClause,
                new String[]{String.valueOf( mChenil.getId() )} );

        return row == 1;
    }

    public boolean updateMedical(Poids monPoids) {
        String where = PoidsTable.ID + " = ?";

        ContentValues values = new ContentValues(  );
        values.put( PoidsTable.WEIGHT, monPoids.getPoids() );
        values.put( PoidsTable.DATE, monPoids.getDate() );
        values.put( PoidsTable.ID_DOG, monPoids.getDog_id() );

        int row = database.update( PoidsTable.TABLE_NAME, values, where,
                new String[] {String.valueOf( monPoids.getId() )} );

        return row == 1;
    }

    public Chenil selectKennellById(int id) {
        String whereClause = ChenilTable.ID + " = ?";

        Cursor c = database.query( ChenilTable.TABLE_NAME, null, whereClause,
                new String[]{String.valueOf( id )}, null, null, null );

        Chenil chenil = new Chenil();

        while (c.moveToNext()) {
            chenil.setId( c.getInt( c.getColumnIndexOrThrow( ChenilTable.ID ) ) );
            chenil.setName( c.getString( c.getColumnIndexOrThrow( ChenilTable.NAME ) ) );
            chenil.setAddress( c.getString( c.getColumnIndexOrThrow( ChenilTable.ADDRESS ) ) );
            chenil.setVille( c.getString( c.getColumnIndexOrThrow( ChenilTable.VILLE ) ) );
            chenil.setCode_postal( c.getString( c.getColumnIndexOrThrow( ChenilTable.CODE_POSTAL ) ) );
            chenil.setLatitude( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LATITUDE ) ) );
            chenil.setLongitude( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LONGITUDE ) ) );
        }
        c.close();
        return chenil;
    }

    public Chien selectDogById(int id) {
        String selection = ChienTable.ID + " = ?" + " AND " + ChienTable.TABLE_NAME + "." + ChienTable.STATE + " =  ?" ;

        Cursor c = database.query( ChienTable.TABLE_NAME, null, selection, new String[]{String.valueOf( id ),ChienTable.STATE_IN},
                null, null, null );

        Chien chien = new Chien();

        while (c.moveToNext()) {
            chien.setId( id );
            chien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );
            chien.setDate_naissance( c.getString( c.getColumnIndexOrThrow( ChienTable.DATE ) ) );
            chien.setSexe( c.getString( c.getColumnIndexOrThrow( ChienTable.SEXE ) ) );
            chien.setRace( selectOneBreed( c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) ) ) );
            int pereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.PERE ) );
            int mereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.MERE ) );
            int raceId = c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) );
            chien.setPere( pereId <= 0 ? "Inconnu" : selectDogName( pereId ) );
            chien.setMere( mereId <= 0 ? "Inconnue" : selectDogName( mereId ) );
            chien.setRace( raceId <= 0 ? null : selectOneBreed( raceId ) );
            chien.setIdPere( pereId );
            chien.setMereId( mereId );
            chien.setRaceId( raceId );
        }

        c.close();
        return chien;
    }


    public ArrayList<Chien> selectDogNotInThisKennel(Integer id) {




        String bigSelect = "SELECT * FROM chien WHERE chenil_id is null AND chien.state = ?";


        ArrayList<Chien> chienArrayList = new ArrayList<>();
        Cursor c = database.rawQuery( bigSelect,new String[] {ChienTable.STATE_IN } );

        while (c.moveToNext()) {
            Chien chien = new Chien();

            chien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );
            chien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );
            int race = c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) );
            chien.setRace( selectOneBreed( race ) );
            chien.setSexe( c.getString( c.getColumnIndexOrThrow( ChienTable.SEXE ) ) );

            chienArrayList.add( chien );

        }
        c.close();
        return chienArrayList;
    }

    /**
     * Méthode récursive qui construit un arbre généalogie pour contrer la consanguinité et renvoie
     * une Liste des chiens compatible a un accouplement.
     *
     * @param monChien Chien a vérifier l'arbre généalogique.
     * @return une liste de Chien qui ne font pas parti de la famille du chien.
     */
    public ArrayList<Chien> selectDogNotInFamily(Chien monChien){
        ArrayList<Chien> notMyFamily = new ArrayList<>(  );
        String otherSexToFind = monChien.getSexe().equals( "F" )? ChienTable.PERE :ChienTable.MERE;
        String sameSexToFind = monChien.getSexe().equals( "M" )? ChienTable.PERE :ChienTable.MERE;


        String recursiveQuery = "SELECT " + ChienTable.ID +", " + ChienTable.NAME +" FROM "
                + ChienTable.TABLE_NAME +" WHERE " + ChienTable.TABLE_NAME + "."+ ChienTable.ID + " NOT IN (" +
                "WITH tempTable as ( SELECT " + ChienTable.TABLE_NAME + "."+ ChienTable.ID + ", " +
                ChienTable.TABLE_NAME + "."+ otherSexToFind + " FROM " + ChienTable.TABLE_NAME + " WHERE " + ChienTable.ID + " = ?" + " UNION" +
                " SELECT " + ChienTable.TABLE_NAME + "."+ ChienTable.ID + ", " + ChienTable.TABLE_NAME + "." + otherSexToFind +
                " FROM " + ChienTable.TABLE_NAME +" INNER JOIN tempTable ON tempTable." + otherSexToFind+ " = "
                + ChienTable.TABLE_NAME + "." + ChienTable.ID + " OR " + ChienTable.TABLE_NAME + "." + otherSexToFind + " = tempTable.id)" +
                "SELECT tempTable.id FROM tempTable ) AND " + ChienTable.TABLE_NAME + "."+ ChienTable.ID
                + " NOT IN (WITH tempTable2 as ( SELECT " + ChienTable.TABLE_NAME + "."+ ChienTable.ID + ", " +
                 ChienTable.TABLE_NAME + "." + sameSexToFind + " FROM " + ChienTable.TABLE_NAME + " WHERE " + ChienTable.ID + " = ?" + " UNION " +
                " SELECT " + ChienTable.TABLE_NAME + "."+ ChienTable.ID + ", " + ChienTable.TABLE_NAME + "." + sameSexToFind +
                " FROM " + ChienTable.TABLE_NAME +" INNER JOIN tempTable2 ON tempTable2." + sameSexToFind+ " = "
                + ChienTable.TABLE_NAME + "." + ChienTable.ID + " OR " + ChienTable.TABLE_NAME + "." + sameSexToFind + " = tempTable2.id)" +
                " SELECT tempTable2.id FROM tempTable2 ) AND  "
                + ChienTable.TABLE_NAME + "." + ChienTable.RACE + " = ? AND " + ChienTable.TABLE_NAME + "." + ChienTable.SEXE +" <> ?;";

        Cursor c = database.rawQuery( recursiveQuery, new String[] {String.valueOf(monChien.getId()),
                String.valueOf(monChien.getId()),  String.valueOf( monChien.getRaceId() ), monChien.getSexe()} );

        while (c.moveToNext()){
            Chien chien = new Chien(  );

            chien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );
            chien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );
            notMyFamily.add( chien );
        }

        c.close();
        return notMyFamily;
    }


    public ArrayList<Chien> getParentFromDB(String sexe, int raceId) {
        ArrayList<Chien> chiens = new ArrayList<>();

        Cursor c = selectDogByBreedAndSex( sexe, raceId );
        if (c != null) {
            while (c.moveToNext()) {
                Chien mChien = new Chien();
                mChien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );
                mChien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );

                chiens.add( mChien );
            }
            c.close();
        }
        return chiens;
    }


    /**
     * Méthode qui fait une Query à la Database pour avoir la liste des chiens.
     *
     * @return un ArrayList de chiens pour afficher à l'utilisateur la liste de ses chiens.
     */
    public ArrayList<Chien> selectAllDog( boolean isFamilyFragment) {
        ArrayList<Chien> chiens = new ArrayList<>();

            String selection = ChienTable.TABLE_NAME + "." + ChienTable.STATE + " = ?";
            Cursor c = database.query( ChienTable.TABLE_NAME,
                    null, selection, new String[]{ChienTable.STATE_IN},
                    null, null, null );

        while (c.moveToNext()) {
            Chien chien = new Chien();

            chien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );
            chien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );
            chien.setDate_naissance( c.getString( c.getColumnIndexOrThrow( ChienTable.DATE ) ) );
            chien.setAge( c.getString( c.getColumnIndexOrThrow( ChienTable.DATE ) ) );
            int pereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.PERE ) );
            int mereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.MERE ) );
            int raceId = c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) );
            chien.setIdPere( pereId );
            chien.setMereId( mereId );
            chien.setRaceId( raceId );
            chien.setPere( pereId <= 0 ? "Inconnu" : selectDogName( pereId ) );
            chien.setMere( mereId <= 0 ? "Inconnue" : selectDogName( mereId ) );
            chien.setRace( getNameById( String.valueOf( raceId ), RaceTable.TABLE_NAME, RaceTable.ID, RaceTable.NAME ) );
            chien.setSexe( c.getString( c.getColumnIndexOrThrow( ChienTable.SEXE ) ) );

            chiens.add( chien );
        }
        c.close();

        return chiens;

    }

    private String selectDogName(int id) {
        String[] column = {ChienTable.NAME};
        String selection = ChienTable.ID + " = ?";

        Cursor c = database.query( ChienTable.TABLE_NAME, column, selection, new String[]{String.valueOf( id )},
                null, null, null );

        if (c.moveToNext()) {
            return c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) );
        }
        c.close();
        return null;
    }


    public ArrayList<Race> selectAllBreed() {
        Cursor c = database.query( RaceTable.TABLE_NAME,
                null, null, null,
                null, null, null );

        ArrayList<Race> races = new ArrayList<>();

        while (c.moveToNext()) {
            int id = c.getInt( c.getColumnIndexOrThrow( RaceTable.ID ) );
            String nom = c.getString( c.getColumnIndexOrThrow( RaceTable.NAME ) );
            String country = c.getString( c.getColumnIndexOrThrow( RaceTable.COUNTRY ) );
            String note = c.getString( c.getColumnIndexOrThrow( RaceTable.NOTE ) );

            races.add( new Race( id, nom, country, note ) );
        }
        c.close();
        return races;
    }


    public int deleteKennelById(int id) {
        String whereClause = ChenilTable.ID + " = ? ";

        return database.delete( ChenilTable.TABLE_NAME, whereClause, new String[]{String.valueOf( id )} );

    }

    public ArrayList<Chenil> selectKennelForRV() {
        ArrayList<Chenil> chenils = new ArrayList<>();
        String[] columnToShow = {ChenilTable.ID, ChenilTable.NAME, ChenilTable.VILLE, ChenilTable.ADDRESS};

        Cursor c = database.query( ChenilTable.TABLE_NAME, columnToShow, null,
                null, null, null, null );

        while (c.moveToNext()) {
            Chenil chenil = new Chenil();

            chenil.setId( c.getInt( c.getColumnIndexOrThrow( ChenilTable.ID ) ) );
            chenil.setName( c.getString( c.getColumnIndexOrThrow( ChenilTable.NAME ) ) );
            chenil.setVille( c.getString( c.getColumnIndexOrThrow( ChenilTable.VILLE ) ) );
            chenil.setAddress( c.getString( c.getColumnIndexOrThrow( ChenilTable.ADDRESS ) ) );

            chenils.add( chenil );
        }
        c.close();
        return chenils;
    }


    public ArrayList<Chien> selectDogByKennelToShowInRV(String chenilId) {
        ArrayList<Chien> chiens = new ArrayList<>();
        String select = "SELECT * FROM chien WHERE chenil_id = ? AND " + ChienTable.STATE + " = ?";



        Cursor c = database.rawQuery( select, new String[]{chenilId, ChienTable.STATE_IN} );

        while (c.moveToNext()) {
            Chien chien = new Chien();

            chien.setId( c.getInt( c.getColumnIndexOrThrow( ChienTable.ID ) ) );
            chien.setNom( c.getString( c.getColumnIndexOrThrow( ChienTable.NAME ) ) );
            chien.setDate_naissance( c.getString( c.getColumnIndexOrThrow( ChienTable.DATE ) ) );
            int pereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.PERE ) );
            int mereId = c.getInt( c.getColumnIndexOrThrow( ChienTable.MERE ) );
            int raceId = c.getInt( c.getColumnIndexOrThrow( ChienTable.RACE ) );
            chien.setIdPere( pereId );
            chien.setMereId( mereId );
            chien.setRaceId( raceId );
            chien.setPere( pereId <= 0 ? null : selectDogName( pereId ) );
            chien.setMere( mereId <= 0 ? null : selectDogName( mereId ) );
            chien.setRace( selectOneBreed( raceId ) );
            chien.setSexe( c.getString( c.getColumnIndexOrThrow( ChienTable.SEXE ) ) );


            chiens.add( chien );
        }
        c.close();
        return chiens;
    }

    public long insertMedicalExam(Poids poids, int dogId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( PoidsTable.WEIGHT, poids.getPoids() );
        contentValues.put( PoidsTable.DATE, poids.getDate() );
        contentValues.put( PoidsTable.ID_DOG, dogId );

        return database.insert( PoidsTable.TABLE_NAME, null, contentValues );

    }


    public ArrayList<Poids> selectMEdicalByDogId(int id) {
        String selection = PoidsTable.ID_DOG + " = ?";

        Cursor c = database.query( PoidsTable.TABLE_NAME, null, selection,
                new String[]{String.valueOf( id )}, null, null, null );
        ArrayList<Poids> poidsArrayList = new ArrayList<>();
        while (c.moveToNext()) {
            Poids poids = new Poids();

            poids.setId( c.getInt( c.getColumnIndexOrThrow( PoidsTable.ID ) ) );
            poids.setDate( c.getString( c.getColumnIndexOrThrow( PoidsTable.DATE ) ) );
            poids.setPoids( c.getString( c.getColumnIndexOrThrow( PoidsTable.WEIGHT ) ) );
            poids.setDog_id( c.getInt( c.getColumnIndexOrThrow( PoidsTable.ID_DOG ) ) );
            poids.setChien_nom( selectDogName( poids.getDog_id() ) );

            poidsArrayList.add( poids );
        }
        c.close();
        return poidsArrayList;
    }


    public long insertBreed(Race race) {
        ContentValues content = new ContentValues();
        content.put( RaceTable.NAME, race.getName() );
        content.put( RaceTable.COUNTRY, race.getCountry() );
        content.put( RaceTable.NOTE, race.getNote() );

        return database.insert( RaceTable.TABLE_NAME, null, content );

    }


    // TEST FAIT --------------------------------------------------------------------------
    public long insertDog(Chien chien) {
        ContentValues values = new ContentValues();
        values.put( ChienTable.NAME, chien.getNom() );
        values.put( ChienTable.SEXE, chien.getSexe() );
        values.put( ChienTable.RACE, chien.getRaceId() );
        values.put( ChienTable.DATE, chien.getDate_naissance() );
        values.put( ChienTable.PERE, chien.getIdPere() );
        values.put( ChienTable.MERE, chien.getMereId() );
        values.put( ChienTable.STATE, ChienTable.STATE_IN );

        return database.insert( ChienTable.TABLE_NAME, null, values );

    }

    public long setKennelIdToDog(int chienId, int chenilId) {
        String where = ChienTable.ID + " = ?";
        ContentValues values = new ContentValues();
        values.put( ChienTable.CHENIL_ID, chenilId );

        return database.update( ChienTable.TABLE_NAME, values,where,new String[] {String.valueOf( chienId )} );

    }

    public long insertKennel(Chenil chenil) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( ChenilTable.NAME, chenil.getName() );
        contentValues.put( ChenilTable.ADDRESS, chenil.getAddress() );
        contentValues.put( ChenilTable.CODE_POSTAL, chenil.getCode_postal() );
        contentValues.put( ChenilTable.VILLE, chenil.getVille() );
        contentValues.put( ChenilTable.LATITUDE, chenil.getLatitude() );
        contentValues.put( ChenilTable.LONGITUDE, chenil.getLongitude() );

        return  database.insert( ChenilTable.TABLE_NAME, null, contentValues );

    }


    public boolean updateById(Chien chien) {
        String where = ChienTable.ID + " = ?";
        ContentValues values = new ContentValues();

        values.put( ChienTable.NAME, chien.getNom() );
        values.put( ChienTable.SEXE, chien.getSexe() );
        values.put( ChienTable.RACE, chien.getRaceId() );
        values.put( ChienTable.DATE, chien.getDate_naissance() );
        values.put( ChienTable.PERE, chien.getIdPere() );
        values.put( ChienTable.MERE, chien.getMereId() );

        int row = database.update( ChienTable.TABLE_NAME, values, where, new String[]{String.valueOf( chien.getId() )} );

        return row == 1;
    }

    // Méthode concernant les requêtes sur les chiens -------------------------------



    private String getNameById(String id, String table_name, String col_id, String col_name) {
        String selection = col_id + " = " + "\'" + id + "\'";
        Cursor c = database.query( table_name, new String[]{table_name + "." + col_name}, selection, null,
                null, null, null );

        c.moveToPosition( 0 );
        String name = c.getString( c.getColumnIndexOrThrow( col_name ) );

        c.close();
        return name;
    }


    public Cursor selectDogByBreedAndSex(String sexe, int raceId) {
        String selection = ChienTable.SEXE + " = ?" + " AND " + ChienTable.RACE + " = ?";

        return database.query( ChienTable.TABLE_NAME, new String[]{ChienTable.ID, ChienTable.NAME}
                , selection, new String[]{sexe, String.valueOf( raceId )}, null,
                null, null );
    }


    public ArrayList<Chenil> getKennelsLocalisation() {
        ArrayList<Chenil> list = new ArrayList<>();
        String[] column = {ChenilTable.NAME, ChenilTable.LONGITUDE, ChenilTable.LATITUDE};
        Cursor c = database.query( ChenilTable.TABLE_NAME, column, null, null,
                null, null, null );

        while (c.moveToNext()) {
            Chenil chenil = new Chenil();

            chenil.setName( c.getString( c.getColumnIndexOrThrow( ChenilTable.NAME ) ) );
            chenil.setLongitude( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LONGITUDE ) ) );
            chenil.setLatitude( c.getFloat( c.getColumnIndexOrThrow( ChenilTable.LATITUDE ) ) );

            list.add( chenil );
        }
        c.close();
        return list;
    }


    // Méthodes concernant les requêtes sur les races -------------------------------

    public String selectOneBreed(int raceId) {
        String[] column = {RaceTable.NAME};
        String where = " id = ?";

        String nom = "";
        Cursor c = database.query( RaceTable.TABLE_NAME, column, where, new String[]{String.valueOf( raceId )},
                null, null, null );
        while (c.moveToNext()) {

            nom = c.getString( c.getColumnIndexOrThrow( RaceTable.NAME ) );
        }

        c.close();
        return nom;
    }

    public boolean updateBreed(Race mRace) {
        String where = RaceTable.ID + " = ?";
        ContentValues content = new ContentValues();

        content.put( RaceTable.NAME, mRace.getName() );
        content.put( RaceTable.COUNTRY, mRace.getCountry() );
        content.put( RaceTable.NOTE, mRace.getNote() );

        int row = database.update( RaceTable.TABLE_NAME, content, where, new String[]{String.valueOf( mRace.getId() )} );

        return row == 1;
    }

    public int deleteBreedById(int id) {
        /*String selection = RaceTable.NAME + " = ?";

        database.query( RaceTable.TABLE_NAME, new String[] {RaceTable.ID}, )*/
        String where = RaceTable.ID + " = ?";

        return database.delete( RaceTable.TABLE_NAME,where, new String[] {String.valueOf( id )});

    }
}

