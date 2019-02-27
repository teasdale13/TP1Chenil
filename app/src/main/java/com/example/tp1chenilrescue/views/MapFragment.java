package com.example.tp1chenilrescue.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.Chenil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapFragmentListener mListener;
    private ArrayList<Chenil> chenilArrayList;
    private FragmentManager fragmentManager;
    private SupportMapFragment supportMapFragment;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        int nbrOfNoneShowKennel = 0;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Chenil chenil: chenilArrayList) {
            if (chenil.getLongitude() != null && chenil.getLatitude() != null){
                LatLng latLng = new LatLng( chenil.getLatitude(), chenil.getLongitude() );
                MarkerOptions mMarker = new MarkerOptions();
                mMarker.position( latLng ).title(chenil.getName()  );
                builder.include(mMarker.getPosition());
                googleMap.addMarker( mMarker );
            }else {
                nbrOfNoneShowKennel++;
            }
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 70);
        googleMap.animateCamera( cu );
        // Affiche un Toast pour dire à l'utilisateur combien de chenil ne possède pas de LatLng.
        Toast.makeText( getContext(), nbrOfNoneShowKennel > 1? nbrOfNoneShowKennel + " chenils n'ont pu être localisés":
                        nbrOfNoneShowKennel + " chenil n'a pu être localisé",
                Toast.LENGTH_LONG ).show();
    }

    public void setManager(FragmentManager manager){
        fragmentManager = manager;
    }
    public void setSupportMapFragment(SupportMapFragment supportMap) {
        supportMapFragment = supportMap;
    }

    public void setList(ArrayList<Chenil> chenils){
        chenilArrayList = chenils;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_map, container, false );
        supportMapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById( R.id.fragment );

        supportMapFragment.getMapAsync( this);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.MapListener( uri );
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface MapFragmentListener {
        // TODO: Update argument type and name
        void MapListener(Uri uri);
    }
}
