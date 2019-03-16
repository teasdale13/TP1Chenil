package com.example.tp1chenilrescue.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.ChenilDataAccess;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.example.tp1chenilrescue.models.RaceDataAccess;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 * 
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private DatabaseHelper helper;
    private ChenilDataAccess chenilDataAccess;
    private ChienDataAccess chienDataAccess;
    private RaceDataAccess raceDataAccess;
    private int container;
    private Toolbar toolbar;
    private SupportMapFragment supportMapFragment;
    private boolean connected;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        helper = new DatabaseHelper( getApplicationContext() );
        chenilDataAccess = new ChenilDataAccess( helper );
        raceDataAccess = new RaceDataAccess( helper );
        chienDataAccess = new ChienDataAccess( helper );
        container = R.id.fragment_container;
        toolbar = findViewById( R.id.toolbar );

        setSupportActionBar( toolbar );

        // Permet de trapper les clicks events dans le menu.
        NavigationView navigationView = findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener(this);
        checkLocalisationPermission();
        drawerLayout = findViewById( R.id.drawer );
        toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close );

        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE );

        connected = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;



        // Affiche un Fragment prédéterminé lorsque
        // l'application est démarré pour la première fois.
        if (savedInstanceState == null) {
            ChenilFragmentRV fragment = new ChenilFragmentRV();
            fragment.setOption( false,false, getSupportFragmentManager(),
                    getApplicationContext(), true, false);
            fragment.setList( chenilDataAccess.selectKennelForRV());
            getSupportFragmentManager().beginTransaction().replace( container,
                    fragment ).commit();
            navigationView.setCheckedItem( R.id.chenil_menu );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSetting:
                settingsFragment();
                break;
            case R.id.menuHelp:
                help();
                break;
            case R.id.genealogie:
                familyFragment();
                break;
            case R.id.stat:
                statsFragment();
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Méthode qui affiche le fragment qui sert a déterminer les possibilités d'accouplements.
     */
    private void familyFragment() {
        FamilyFragment familyFragment = new FamilyFragment();
        familyFragment.setDogList( chienDataAccess.selectAllDog(true) );
        familyFragment.setDataAccess(chienDataAccess);
        getSupportFragmentManager().beginTransaction().replace( container, familyFragment ).commit();
    }

    /**
     * Méthode qui affiche le fragment qui sert a afficher l'aide Online et Offline.
     */
    private void help(){
        HelpFragment fragment = new HelpFragment();
        getSupportFragmentManager().beginTransaction().replace( container,fragment ).commit();

    }

    /**
     * Méthode qui affiche un diagramme selon l'age des chiens.
     */
    private void statsFragment() {
        StatsFragment fragment = new StatsFragment();
        fragment.setContext( getApplicationContext() );
        fragment.setList( chienDataAccess.selectAllDog(false) );
        getSupportFragmentManager().beginTransaction().replace( container,fragment ).commit();
    }

    /**
     * Méthode qui affiche le fragment des reglages.
     */
    private void settingsFragment() {
        SettingFragment fragment = new SettingFragment();
        fragment.setContext( getApplicationContext() );
        getSupportFragmentManager().beginTransaction().replace( container,fragment ).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.toolbar_menu, menu );

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.chenil_menu:
                showFragmentChenil(false, false,
                        true, false);
                break;
            case R.id.chien_menu:
                showFragmentChenil( true, false,
                        false, true );
                break;
            case R.id.chien_list:
                showDogList();
                break;
            case R.id.scan:
                showNFCReader();
                break;
            case R.id.map:

                showMap();
                break;
            case R.id.poids:
                showFragmentChenil( true, true,
                        false, false);
                break;
            case R.id.breed:
                showBreed();
                break;
            default:
                Toast.makeText( this, "Choix non supporté", Toast.LENGTH_SHORT ).show();
        }

        drawerLayout.closeDrawer( GravityCompat.START );
        return true;
    }

    /**
     * Méthode qui affiche tous les chiens et qui permet d'en ajouter de nouveaux.
     */
    private void showDogList() {
        DogFragmentRV fragmentRV = new DogFragmentRV();
        fragmentRV.setList( chienDataAccess.selectAllDog(false) );
        fragmentRV.setShowMedical( false, false , false);
        fragmentRV.setFragmentManager( getSupportFragmentManager() );
        fragmentRV.setContext( getApplicationContext() );
        getSupportFragmentManager().beginTransaction().replace( container,fragmentRV ).commit();

    }


    /**
     * Fait appel au fragment qui contient le RecyclerView qui affiche les races.
     */
    private void showBreed() {
        RVBreedFragment fragment = new RVBreedFragment();
        fragment.setOption(getApplicationContext());
        fragment.setList( raceDataAccess.selectAllBreed() );
        fragment.setDatabase(raceDataAccess);
        fragment.setFManagement( getSupportFragmentManager());
        getSupportFragmentManager().beginTransaction().replace( container, fragment ).commit();

    }

    /**
     * Fait appel au fragment qui contient la GoogleMap et fait apparaitre les chenils sur une
     * map.
     */
    private void showMap() {
        if (connected){
            ArrayList<Chenil> list = chenilDataAccess.getKennelsLocalisation();
            if (!list.isEmpty()) {
                MapFragment fragment = new MapFragment();
                fragment.setSupportMapFragment( supportMapFragment );
                fragment.setList( list );
                getSupportFragmentManager().beginTransaction().replace( container, fragment ).commit();
            }else {
                Toast.makeText( getApplicationContext(), "Aucun chenil enregistré", Toast.LENGTH_LONG ).show();
            }
        }else {
            Toast.makeText( getApplicationContext(), "Aucune connection internet", Toast.LENGTH_LONG ).show();
        }


    }

    /**
     * Vérifie si l'application détient l'autorisation de l'utilisateur pour utiliser le GPS et
     * la demande si elle ne la détient pas.
     */
    private void checkLocalisationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){

        } else {
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    1 );
        }
    }


    /**
     * Fait appel au FragmentActivity qui permettera de mettre en marche la lecture NFC.
     */
    private void showNFCReader() {
        Intent intent = new Intent( MainActivity.this, NFCReader.class );
        Bundle bundle = new Bundle(  );
        bundle.putSerializable("database", chienDataAccess );
        intent.putExtra( "dataAccess", bundle );
        NFCReader reader = new NFCReader();
        reader.setListener( new NFCReader.NFCFragmentListener() {
            @Override
            public void NFCReaderListener(Uri uri) {

            }
        } );
                startActivity( intent );
    }

    /**
     * Fonction qui affiche la liste de tous les chiens dans un RecyclerView dans un Fragment. Selon
     * les paramêtres entrés, certains "features" sont activés ou désactivés.
     */
    private void showFragmentChenil(boolean needToShowDog, boolean needToShowMedical, boolean showDialogFragment, boolean rvKennelDog) {
        ChenilFragmentRV fragment = new ChenilFragmentRV();
        fragment.setOption( needToShowDog, needToShowMedical, getSupportFragmentManager(),
                getApplicationContext(),showDialogFragment, rvKennelDog );
        ArrayList<Chenil> arrayList = chenilDataAccess.selectKennelForRV();
        fragment.setList( arrayList );
        getSupportFragmentManager().beginTransaction().replace( container,
                fragment ).commit();
    }






}

