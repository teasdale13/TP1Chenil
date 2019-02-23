package com.example.tp1chenilrescue;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tp1chenilrescue.adapter.AdapterDogKennel;
import com.example.tp1chenilrescue.models.Chien;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 *
 * DialogFragment qui contient un RecyclerView des chiens qui ne sont pas dans le chenil selectionné.
 */
public class ChenilChienDialogFragment extends DialogFragment {

    private OnChenilChienListener mListener;
    private RecyclerView recyclerView;
    private AdapterDogKennel adapter;
    private ArrayList<Chien> chiens;
    private Context mContext;

    public ChenilChienDialogFragment() {
        // Required empty public constructor
    }

    public static ChenilChienDialogFragment newInstance() {
        ChenilChienDialogFragment fragment = new ChenilChienDialogFragment();

        return fragment;
    }

    public void setOptions(ArrayList<Chien> list, Context context){
        chiens = list;
        mContext = context;
    }

    public void setListener(OnChenilChienListener listener){
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.chenil_chien_dialogfrag, container, false );

        recyclerView = view.findViewById( R.id.recyclerViewChenilChien );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        adapter = new AdapterDogKennel( chiens );
        recyclerView.setAdapter( adapter );

        adapter.setListener( new AdapterDogKennel.AddToKennelInterface() {
            @Override
            public void onDogAddToKennelListener(Chien chien) {
                adapter.removeItem(chien);
                onButtonPressed( chien );
            }
        } );
        return view;
    }

    public void onButtonPressed(Chien mChien) {
        if (mListener != null) {
            mListener.onChenilChienInteraction( mChien );
            this.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnChenilChienListener {
        // TODO: Update argument type and name
        void onChenilChienInteraction(Chien chien);
    }
}
