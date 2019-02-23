package com.example.tp1chenilrescue;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.adapter.AdapterDog;
import com.example.tp1chenilrescue.dialogfragment.AddDog;
import com.example.tp1chenilrescue.dialogfragment.ConfirmDelete;
import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 *
 * Le fragment qui contient le RecyclerView de tous les chiens.
 */
public class DogFragmentRV extends Fragment {


    private DogFragmentListener mListener;
    private ArrayList<Chien> chiens;
    private AdapterDog adapter;
    private RecyclerView recyclerView;
    private boolean needToShowMedical, rvInDialogFragment;
    private Context mContext;
    private FragmentManager fragmentManager;
    private Chenil monChenil;
    private ChienDataAccess chienDataAccess;
    private boolean kennelDog;

    public DogFragmentRV() {
        // Required empty public constructor
    }

    public static DogFragmentRV newInstance(String param1, String param2) {
        DogFragmentRV fragment = new DogFragmentRV();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setChenilForID(Chenil chenil) {
        monChenil = chenil;
    }

    public void setListener(DogFragmentListener listener){
        mListener = listener;
    }
    public void setShowMedical(boolean showMed, boolean showDogInDiagFragment, boolean rvDogKennel){
        needToShowMedical = showMed;
        rvInDialogFragment = showDogInDiagFragment;
        kennelDog = rvDogKennel;

    }
    public void setContext(Context context) {
        mContext = context;
    }
    public void setFragmentManager(FragmentManager manager){
        fragmentManager = manager;
    }
    public void setList(ArrayList<Chien> list){
        chiens = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.rv_dog_fragment, container, false );

        DatabaseHelper helper = new DatabaseHelper( mContext );
        chienDataAccess = new ChienDataAccess( helper );

        recyclerView = view.findViewById( R.id.recyclerViewChien );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        adapter = new AdapterDog( chiens, getFragmentManager(), needToShowMedical, mContext, kennelDog);
        recyclerView.setAdapter( adapter );

        if (needToShowMedical){
            // Cache le FloatingActionButton lorsque l'utilisateur veut afficher les Poids pour
            // ne pas permettre d'ajouter un chien à ce moment là.
            view.findViewById( R.id.floatBtnChien ).setVisibility( View.INVISIBLE );

        }else {
            FloatingActionButton button = view.findViewById( R.id.floatBtnChien );
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Vérifie si l'utilisateur veut ajouter un chien à un chenil.
                    if (rvInDialogFragment) {
                        showKennelDogFragment();

                    } else {
                        addDogToList();
                    }
                }
            } );
            SwipeToDelete();
        }

        return view;
    }

    /**
     * Méthode qui affiche un DialogFragment pour ajouter un chien à la liste de tous les chiens.
     * Plusieurs paramêtres lui sont envoyé pour permettre de "remplir" les Spinners.
     *
     */
    private void addDogToList() {
        final AddDog addDog = new AddDog();
        addDog.setContextForSpinner( mContext );
        addDog.setBreedList( chienDataAccess.selectAllBreed() );
        addDog.setDatabase( chienDataAccess );
        addDog.setDogInDiagFrag( new Chien() );
        addDog.setListener( new AddDog.OnDogInteractionListener() {
            @Override
            public void dogFragmentInteraction(Chien monChien, int index, int id) {
                adapter.addItemInRV( monChien );
                insertDogIntoDb(monChien);

            }
        } );
        addDog.setDogInDiagFrag( new Chien() );
        addDog.show( fragmentManager, "ajouterChien" );
    }

    /**
     * Méthode qui ajoute le chien créé dans la base de données.
     *
     * @param monChien le chien a ajouter dans la DB.
     */
    private void insertDogIntoDb(Chien monChien) {
        long isInserted = chienDataAccess.insertDog( monChien );
        if (isInserted != -1){
            Toast.makeText( mContext, "Le chien à été ajouté.",
                    Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * Méthode qui affiche un DialogFragment contenant les chiens qui ne font pas parti
     * du chenil. Un Toast est affiché si le chien a été ajouté ou si le chien
     * est déjà associé au chenil.
     */
    private void showKennelDogFragment() {
        ChenilChienDialogFragment fragment = new ChenilChienDialogFragment();
        fragment.setOptions( chienDataAccess.selectDogNotInThisKennel( monChenil.getId() ), mContext );
        fragment.setListener( new ChenilChienDialogFragment.OnChenilChienListener() {
            @Override
            public void onChenilChienInteraction(Chien chien) {
                try{
                    long isInserted = chienDataAccess.insertIntoChenilChien( chien.getId(), monChenil.getId() );
                    if (isInserted != -1){
                        adapter.addItemInRV( chien );
                        Toast.makeText( mContext, "Le chien à été ajouté au chenil.",
                                Toast.LENGTH_LONG ).show();
                    }else {
                        Toast.makeText( mContext, "Le chien est déjà aassocié au chenil.",
                                Toast.LENGTH_LONG ).show();
                    }
                }catch (Exception e){
                    Log.d( "ERROR", e.getMessage() );
                }

            }
        } );
        fragment.show( fragmentManager, "showTime" );
    }

    private void SwipeToDelete() {
        SwipeToDelete swipeToDelete = new SwipeToDelete(mContext) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Chien chien = adapter.getData().get(position);
                adapter.deleteItem(position);

                ConfirmDelete delete = new ConfirmDelete();
                delete.setListener( new ConfirmDelete.ConfirmDeleteListener() {
                    @Override
                    public void confirmDelete(boolean response) {
                        if (!response) {
                            // restore l'item si l'utilisateur à appuyé sur non.
                            adapter.restoreItem( position, chien );
                        }
                        if (response){
                            // si le lien entre le chenil et le chien doit être supprimé.
                            if (rvInDialogFragment){
                                Toast.makeText( mContext, "Patate!!!!", Toast.LENGTH_LONG ).show();
                                chienDataAccess.deleteFromChenilChien( chien.getId(), monChenil.getId()  );
                                // si le chien en entier doit être supprimé par son ID.
                            }else {
                                int isdeleted = chienDataAccess.deleteDogById(chien.getId());

                                if (isdeleted == 1){
                                    Toast.makeText( mContext, "Le chien à été supprimé", Toast.LENGTH_LONG ).show();
                                }
                            }
                        }
                    }
                } );
                delete.show( fragmentManager,"delete" );
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDelete);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.DogListener( uri );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface DogFragmentListener {
        // TODO: Update argument type and name
        void DogListener(Uri uri);
    }
}
