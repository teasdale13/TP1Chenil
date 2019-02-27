package com.example.tp1chenilrescue.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.adapter.AdapterBreed;
import com.example.tp1chenilrescue.dialogfragment.AddBreed;
import com.example.tp1chenilrescue.dialogfragment.ConfirmDelete;
import com.example.tp1chenilrescue.models.Race;
import com.example.tp1chenilrescue.models.RaceDataAccess;
import com.example.tp1chenilrescue.models.SwipeToDelete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * @author Kevin Teadale-Dubé
 *
 * Fragment qui affiche un RecyclerView des Races disponible dans le chenil.
 */
public class RVBreedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private AdapterBreed adapter;
    private ArrayList<Race> raceArrayList;
    private FragmentManager manager;
    private Context mContext;
    private RaceDataAccess dataAccess;

    public RVBreedFragment() {
        // Required empty public constructor
    }

    public static RVBreedFragment newInstance(String param1, String param2) {
        RVBreedFragment fragment = new RVBreedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setList(ArrayList<Race> list){
        raceArrayList = list;
    }

    public void setFManagement(FragmentManager supportFragmentManager) {
        manager = supportFragmentManager;
    }

    public void setDatabase(RaceDataAccess raceDataAccess) {
        dataAccess = raceDataAccess;
    }

    public void setOption(Context applicationContext) {
        mContext = applicationContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.rv_breed_fragment, container, false );

        recyclerView = view.findViewById( R.id.recyclerViewBreed );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ));
        adapter = new AdapterBreed(raceArrayList, manager );
        recyclerView.setAdapter( adapter );

        adapter.setDatabaseToAdapter(dataAccess, mContext);

        FloatingActionButton button = view.findViewById( R.id.floatBtnRace );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBreed fragment = new AddBreed();
                fragment.setAddBreed( new Race(  ) );
                fragment.setListener( new AddBreed.OnFragmentInteractionListener() {
                    @Override
                    public void onFragmentInteraction(Race maRace, int index) {
                        // Averti l'Adapter qu'il y a un Item a ajouter au RecyclerView.
                        adapter.addBreedToRV(maRace);
                        addBreedToDb(maRace);

                    }
                } );
                fragment.show( manager , "dialog" );
            }
        } );

        SwipeToDelete();
        return view;
    }

    /**
     * Méthode qui procède à l'ajout de la race dans la base de données et affiche un Toast selon la
     * valeur retourné par la méthode insertBreed();
     *
     * @param maRace la race qui doit être ajoutée à la base de données.
     */
    private void addBreedToDb(Race maRace) {
        long isInserted = dataAccess.insertBreed( maRace );
        if (isInserted != -1){
            Toast.makeText( mContext, "La race a été ajouté.",
                    Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( mContext,"Une erreur est survenue",
                    Toast.LENGTH_LONG ).show();
        }
    }

    private void SwipeToDelete() {
        SwipeToDelete swipeToDelete = new SwipeToDelete(mContext) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Race race = adapter.getData().get(position);

                adapter.deleteItem(position);

                ConfirmDelete delete = new ConfirmDelete();
                delete.setListener( new ConfirmDelete.ConfirmDeleteListener() {
                    @Override
                    public void confirmDelete(boolean response) {
                        if (!response) {
                            adapter.restoreItem( position, race );
                        }else {
                            deleteBreedFromDb(race);
                        }
                    }
                } );
                delete.show( manager,"delete" );
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDelete);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Méthode qui procède à la suppression de la race dans la base de données et affiche un Toast
     * selon la valeur retournée par deleteBreedById();
     *
     * @param race la race qui doit être supprimer.
     */
    private void deleteBreedFromDb(Race race) {
        int isInserted = dataAccess.deleteBreedById( race.getId() );
        if (isInserted == 1){
            Toast.makeText( mContext, "La race a été supprimé.",
                    Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG ).show();
        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
