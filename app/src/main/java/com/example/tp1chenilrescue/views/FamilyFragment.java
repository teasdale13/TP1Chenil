package com.example.tp1chenilrescue.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.Race;

import java.util.ArrayList;


public class FamilyFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Chien> list;
    private Spinner spinner;
    private ListView listView;

    public FamilyFragment() {

    }

    public static FamilyFragment newInstance() {
        FamilyFragment fragment = new FamilyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    public void setDogList(ArrayList<Chien> chiens){
        list = chiens;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.family_fragment, container, false );

        listView = view.findViewById( R.id.listview );
        spinner = view.findViewById( R.id.spinnerGenealogie );





        final Chien mChien = new Chien(  );
        mChien.setId( 0 );
        list.add( 0, mChien );
        Chien[] spinnerItem = new Chien[list.size()];
        spinnerItem = list.toArray( spinnerItem );

        ArrayAdapter<Chien> arrayAdapter = new ArrayAdapter<Chien>( getContext(), android.R.layout.simple_spinner_item, spinnerItem );
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( arrayAdapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Chien monChien = (Chien) parent.getItemAtPosition( position );
                if (monChien.getId() != 0){
                    listView.setVisibility( View.VISIBLE );
                    setListViewArray();
                }else {
                    listView.setVisibility( View.GONE );
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        return view;
    }

    private void setListViewArray() {
        String[] firstArray = {"Allo", "Bonjour", "Bye", "Salut", "Bleu", "Jaune", "Rouge", "Vert",
                "Patate", "Pomme" ,"Allo", "Bonjour", "Bye", "Salut", "Bleu", "Jaune", "Rouge", "Vert",
                "Patate", "Pomme"};

        ArrayAdapter adapter = new ArrayAdapter<String>( getContext(), R.layout.listview_cell, firstArray );
        listView.setAdapter( adapter );
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
