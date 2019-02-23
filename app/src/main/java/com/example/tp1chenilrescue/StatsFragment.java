package com.example.tp1chenilrescue;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tp1chenilrescue.models.Chien;

import java.util.ArrayList;


public class StatsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Chien> chienList;
    private Context mContext;

    public StatsFragment() {
    }

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    public void setList(ArrayList<Chien> chiens){
        chienList = chiens;
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.stats_fragment, container, false );
        BarDiagram diagram = view.findViewById( R.id.barChart );
        diagram.setValeurs( setNumberOfDogs( chienList ) );

        return view;
    }

    /**
     * Méthode qui prends un ArrayList de Chien et qui créer un tableau de int répertoriant le nombre
     * de chien selon leur age afin de l'afficher dans un diagramme a bandes.
     *
     * @param chiens ArrayList qui contient des chiens.
     * @return array de int qui contient la quantité de chiens par tranches d'ages.
     */
    private int[] setNumberOfDogs(ArrayList<Chien> chiens) {
        int[] arrays = new int[5];

        for (Chien chien : chiens)
            switch (chien.getAge()) {
                case 0:
                    arrays[0] += 1;
                    break;
                case 1:
                    arrays[1] += 1;
                    break;
                case 2:
                case 3:
                    arrays[2] += 1;
                    break;
                default:
                    if (chien.getAge() >= 8){
                        arrays[4] += 1;
                    }else {
                        arrays[3] += 1;
                    }
                    break;
            }
        return arrays;
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
