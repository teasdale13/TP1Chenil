package com.example.tp1chenilrescue.views;

import android.content.Context;
import android.icu.util.ChineseCalendar;
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
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.Race;

import java.util.ArrayList;


public class FamilyFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Chien> list;
    private Spinner spinner;
    private ListView listView;
    private ChienDataAccess dataAccess;

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

    public void setDataAccess(ChienDataAccess chienDataAccess) {
        dataAccess = chienDataAccess;
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
                listView.setVisibility(monChien.getId() != 0 ? View.VISIBLE : View.GONE  );
                if (monChien.getId() != 0 ){
                    setListViewArray( dataAccess.selectDogNotInFamily(dataAccess.selectDogById( monChien.getId() )) );

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listView.setVisibility( View.GONE );
            }
        } );

        return view;
    }

    private void setListViewArray(ArrayList<Chien> list) {
        Chien[] chiensArray = new Chien[list.size()];
        chiensArray = list.toArray(chiensArray);

        ArrayAdapter adapter = new ArrayAdapter<Chien>( getContext(), R.layout.listview_cell, chiensArray );
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
