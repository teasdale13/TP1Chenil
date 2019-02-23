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
 * <p>
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

        recherche = binding.searchBar.toString();


        binding.searchBar.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference = database.getReference( "race" );

                reference.orderByKey().addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Race> races = new ArrayList<>();
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Race race = snapshot.getValue( Race.class );
                                races.add( race );
                            }
                        }

                        ArrayList<Race> raceArrayList = new ArrayList<>(  );
                        for (Race mRace : races){
                            if (mRace.getName().toUpperCase().contains( query.toUpperCase() )){
                                raceArrayList.add( mRace );
                            }
                        }
                        if (raceArrayList.size() > 0){
                            binding.firebaseSearchRV.setVisibility( View.VISIBLE );
                        }
                        recyclerView = binding.firebaseSearchRV;
                        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
                        adapter = new FirebaseRVAdapter(raceArrayList );
                        adapter.setListener( new FirebaseRVAdapter.BreedListener() {
                            @Override
                            public void onBreedListener(Race race) {
                                binding.setBreed( race );
                            }
                        } );
                        recyclerView.setAdapter( adapter );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

    public void onButtonPressed(Race race, int index) {
        mListener.onFragmentInteraction( race, index );
        this.dismiss();
    }

    public void setRaceToFragment(Race raceToFragment){
        binding.setBreed( raceToFragment );
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
