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
import com.example.tp1chenilrescue.adapter.AdapterMedical;
import com.example.tp1chenilrescue.dialogfragment.AddMedical;
import com.example.tp1chenilrescue.dialogfragment.ConfirmDelete;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.example.tp1chenilrescue.models.Poids;
import com.example.tp1chenilrescue.models.PoidsDataAccess;
import com.example.tp1chenilrescue.models.SwipeToDelete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 *
 * Classe qui affiche les Poids relié à un chien dans un RecyclerVire
 */
public class PoidsFragmentRV extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private AdapterMedical adapterMedical;
    private ArrayList<Poids> poidsArrayList;
    private FragmentManager fragmentManager;
    private Context mContext;
    private Chien mChien;
    private PoidsDataAccess dataAccess;

    public PoidsFragmentRV() {
        // Required empty public constructor
    }

    public static PoidsFragmentRV newInstance() {
        PoidsFragmentRV fragment = new PoidsFragmentRV();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setList(ArrayList<Poids> list){
        poidsArrayList = list;
    }

    public void setOption(FragmentManager manager, Context context, Chien chien){
        fragmentManager = manager;
        mContext = context;
        mChien = chien;
        DatabaseHelper helper = new DatabaseHelper( mContext );
        dataAccess = new PoidsDataAccess( helper );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.rv_poids_fragment, container, false );

        recyclerView = view.findViewById( R.id.recyclerviewMedical );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        adapterMedical = new AdapterMedical( poidsArrayList, fragmentManager, mContext, dataAccess );
        recyclerView.setAdapter( adapterMedical );
        adapterMedical.passDogToAdapter(mChien);

        FloatingActionButton button = view.findViewById( R.id.floatBtnPoids );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedical dialogFragment = new AddMedical();
                dialogFragment.setDog( mChien );
                dialogFragment.setMedListener( new AddMedical.MedicalInteractionListener() {
                    @Override
                    public void onMedicalInteraction(int index, Poids poids, Chien chien) {
                        poids.setChien_nom( chien.getNom() );
                        adapterMedical.addMedicalToRV(poids);
                        addMedicalToDatabase(poids, chien);
                    }
                } );
                dialogFragment.setNewPoids( new Poids(  ) );
                dialogFragment.show( fragmentManager, "poids" );
            }
        } );

        SwipeToDelete();

        return view;
    }

    /**
     * Méthode qui ajoute un poids associé au chien dans la Base de données.
     * @param poids poids à ajouter.
     * @param mChien le chien a qui le poids doit être ajouté.
     */
    private void addMedicalToDatabase(Poids poids, Chien mChien) {
        long isInserted = dataAccess.insertMedicalExam( poids, mChien.getId() );
        if (isInserted != -1){
            Toast.makeText( mContext, "Le poids à été sauvegardé.",
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
                final Poids poids = adapterMedical.getData().get(position);
                adapterMedical.deleteItem(position);

                ConfirmDelete delete = new ConfirmDelete();
                delete.setListener( new ConfirmDelete.ConfirmDeleteListener() {
                    @Override
                    public void confirmDelete(boolean response) {
                        deleteOrNotFromTheDB(response, poids, position);
                    }
                } );
                delete.show( fragmentManager,"delete" );
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDelete);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    /**
     * Méthode qui procède a la suppression (Base de données) ou à la restoration (Adapter RV)
     * de l'objet Poids selon le paramêtre response.
     *
     * @param response valeur qui est retourné par le ConfirmDeleteFragment.
     * @param poids poids qui doit être effacé ou restoré.
     * @param position la poisition de l'item dans le RV qui doit être restoré.
     */
    private void deleteOrNotFromTheDB(boolean response, Poids poids, int position) {
        if (!response) {
            adapterMedical.restoreItem( position, poids );
        }else {
            int isDeleted =  dataAccess.deleteMedicalById(poids.getId());
            if (isDeleted == 1) {
                Toast.makeText( mContext, "Le poids à été effacé.", Toast.LENGTH_LONG ).show();
            }else {
                Toast.makeText( mContext, "Une erreur est survenue.", Toast.LENGTH_LONG ).show();
            }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
