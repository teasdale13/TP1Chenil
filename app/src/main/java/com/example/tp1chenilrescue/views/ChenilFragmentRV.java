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
import com.example.tp1chenilrescue.adapter.AdapterChenil;
import com.example.tp1chenilrescue.dialogfragment.AddChenil;
import com.example.tp1chenilrescue.dialogfragment.ConfirmDelete;
import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.ChenilDataAccess;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.example.tp1chenilrescue.models.SwipeToDelete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 *
 * Fragment qui contient un RecyclerView qui affiche tous les chenils.
 */
public class ChenilFragmentRV extends Fragment {

    private ChenilFragmentListener mListener;
    private ArrayList<Chenil> mList;
    private RecyclerView recyclerView;
    private AdapterChenil adapter;
    private boolean needToShowDogRV, needToShowMedical, rvInDialogFragment;
    private FragmentManager fragmentManager;
    private Context mContext;
    private DatabaseHelper helper;
    private ChenilDataAccess dataAccess;
    private boolean kennelDogRV;

    public ChenilFragmentRV() {
        // Required empty public constructor
    }


    public static ChenilFragmentRV newInstance() {
        ChenilFragmentRV fragment = new ChenilFragmentRV();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setOption(boolean dog, boolean medical, FragmentManager manager, Context context,
                          boolean showInDialogFragment, boolean rvKennelDog) {
        needToShowDogRV = dog;
        needToShowMedical = medical;
        fragmentManager = manager;
        rvInDialogFragment = showInDialogFragment;
        this.mContext = context;
        kennelDogRV = rvKennelDog;

    }

    public void setList(ArrayList<Chenil> list) {
        mList = list;
    }

    public void setListener(ChenilFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.rv_chenil_fragment, container, false );

        helper = new DatabaseHelper( mContext );
        dataAccess = new ChenilDataAccess( helper );
        recyclerView = view.findViewById( R.id.recyclerViewChenil );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        adapter = new AdapterChenil( mList, fragmentManager, needToShowDogRV, mContext, needToShowMedical, kennelDogRV );
        recyclerView.setAdapter( adapter );


        if (needToShowDogRV) {
            // Cache le FloatingActionButton lorsque l'utilisateur veut parcourir les chiens/chenil pour ne pas
            // permettre d'ajouter un chenil à ce moment là.
            view.findViewById( R.id.floatBtnChenil ).setVisibility( View.INVISIBLE );
        } else {
            FloatingActionButton button = view.findViewById( R.id.floatBtnChenil );
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddChenil dialogFragment = new AddChenil();
                    dialogFragment.setLocationContext( mContext );
                    dialogFragment.setListener( new AddChenil.AddChenilFragmentInteraction() {
                        @Override
                        public void ChenilListener(Chenil chenil, int position) {
                            // Ajoute un chenil dans l'ArrayList<Chenil> dans l'adapter.
                            adapter.addChenil( chenil );
                            insertKennelIntoDb(chenil);

                        }
                    } );
                    dialogFragment.setNewChenil( new Chenil() );
                    dialogFragment.show( fragmentManager, "dialog" );

                }
            } );
            SwipeToDelete();
        }

        return view;
    }

    /**
     * Méthode qui fait l'ajout d'un chenil dans la base de données et vérifie si l'insertion
     * a fonctionné et en affiche un Toast selon le cas.
     *
     * @param chenil le chenil qui doit être ajouté à la base de données.
     */
    private void insertKennelIntoDb(Chenil chenil) {
        long isInserted = dataAccess.insertKennel( chenil );
        if (isInserted != -1) {
            Toast.makeText( mContext, "Le chenil a été ajouté.", Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG ).show();
        }
    }

    private void SwipeToDelete() {
        SwipeToDelete swipeToDelete = new SwipeToDelete( mContext ) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Chenil chenil = adapter.getData().get( position );
                adapter.deleteItem( position );

                ConfirmDelete delete = new ConfirmDelete();
                delete.setListener( new ConfirmDelete.ConfirmDeleteListener() {
                    @Override
                    public void confirmDelete(boolean response) {
                        if (!response) {
                            adapter.restoreItem( position, chenil );
                        } else {
                            deleteKennelFromDb(chenil);
                        }
                    }
                } );
                delete.show( fragmentManager, "delete" );
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper( swipeToDelete );
        itemTouchhelper.attachToRecyclerView( recyclerView );
    }

    /**
     * Méthode qui supprime un chenil de la base de données et en affiche un Toast selon le cas.
     *
     * @param chenil le chenil qui doit être supprimé de la base de données.
     */
    private void deleteKennelFromDb(Chenil chenil) {
        int isDeleted =  dataAccess.deleteKennelById(  chenil.getId() );
        if (isDeleted == 1) {
            Toast.makeText( mContext, "Le chenil à été effacé.", Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.", Toast.LENGTH_LONG ).show();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.ChenilListener( uri );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ChenilFragmentListener {
        void ChenilListener(Uri uri);
    }
}
