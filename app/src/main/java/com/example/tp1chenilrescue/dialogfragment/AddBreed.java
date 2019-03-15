package com.example.tp1chenilrescue.dialogfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.adapter.AdapterDog;
import com.example.tp1chenilrescue.adapter.FirebaseRVAdapter;
import com.example.tp1chenilrescue.databinding.AddBreedDiagfragBinding;
import com.example.tp1chenilrescue.models.Race;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kevin Teasdale-Dubé
 *
 * DialogFragment qui affiche un formulaire pour ajouter une race à la base de données.
 */
public class AddBreed extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private Race maRace;
    private AddBreedDiagfragBinding binding;
    private int mIndex;
    private DatabaseReference reference;
    private String recherche;
    private RecyclerView recyclerView;
    private FirebaseRVAdapter adapter;

    public AddBreed() {
        // Required empty public constructor
    }

    public static AddBreed newInstance() {
        AddBreed fragment = new AddBreed();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    public void setAddBreed(Race race) {
        maRace = race;
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    public void setIndex(int position) {
        mIndex = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.add_breed_diagfrag, container, false );

        binding.searchBar.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference = database.getReference( "race" );

                reference.orderByKey().addValueEventListener( new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Race> races = getDataFromFirebase( dataSnapshot, query );
                        if (races.size() > 0){
                            binding.firebaseSearchRV.setVisibility( View.VISIBLE );
                            setUpRecyclerView( races );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.firebaseSearchRV.setVisibility( View.GONE );
                binding.setBreed( new Race(  ) );
                return false;
            }
        } );

        binding.setBreed( maRace );
        binding.btnBreedConfirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( maRace, mIndex );
            }
        } );
        return binding.getRoot();
    }

    /**
     * Méthode qui paramètre le RecyclerView qui se retrouve sous le SearchView.
     *
     * @param list liste de Races à afficher.
     */
    private void setUpRecyclerView(ArrayList<Race> list) {

        recyclerView = binding.firebaseSearchRV;
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        adapter = new FirebaseRVAdapter(list );
        adapter.setListener( new FirebaseRVAdapter.BreedListener() {
            @Override
            public void onBreedListener(Race race) {
                /* Bind la race qui a été sélectionnée dans le RecyclerView avec les EditText */
                binding.setBreed( race );
            }
        } );
        recyclerView.setAdapter( adapter );
    }

    /**
     * Méthode qui fait le tour de tous les Races qui sont dans Firebase et qui en fait un ArrayListe
     * temporaire et qui procède à la comparaison avec la "query" entrée par l'utilisateur et creer
     * la Liste finale à afficher dans un RecyclerView.
     *
     * @param dataSnapshot DataSnapshot qui contient tout les informations de la Query fait à Firebase.
     * @param query Requête (String) entrée par l'utilisateur qui sert à faire le tri des données.
     * @return La liste de races à afficher.
     */
    private ArrayList<Race> getDataFromFirebase(DataSnapshot dataSnapshot, String query) {
        ArrayList<Race> races = new ArrayList<>();
        ArrayList<Race> raceArrayList = new ArrayList<>(  );

        if (dataSnapshot.getValue() != null) {
            /* Fait une ArrayList avec toutes les races de Firebase. */
            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                Race race = snapshot.getValue( Race.class );
                races.add( race );
            }
        }
        for (Race mRace : races){
            /* Compare le nom de la race avec la saisie de l'utilisateur. */
            if (mRace.getName().toUpperCase().contains( query.toUpperCase() )){
                raceArrayList.add( mRace );
            }
        }
        return raceArrayList;
    }

    public void onButtonPressed(Race race, int index) {
        mListener.onFragmentInteraction( race, index );
        this.dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Race mRace, int index);
    }
}
