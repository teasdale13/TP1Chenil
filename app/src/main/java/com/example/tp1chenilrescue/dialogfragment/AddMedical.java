package com.example.tp1chenilrescue.dialogfragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.AddMedicalFragmentBinding;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.Poids;

/**
 * @author Kevin Teasdale-Dubé
 *
 * DialogFragment qui permet d'ajouter un Poids à la Database.
 */
public class AddMedical extends DialogFragment {

    private MedicalInteractionListener mListener;
    private int mIndex;
    private Poids poids;
    private Chien mChien;
    private AddMedicalFragmentBinding binding;

    public AddMedical() {
        // Required empty public constructor
    }

    public static AddMedical newInstance() {
        AddMedical fragment = new AddMedical();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    /**
     * Méthode qui passe l'index du poids dans la liste pour facilité la notification lorsque le poids
     * aura été modifié.
     * @param index position du poids dans la liste
     */
    public void setIndexToModify(int index){
        mIndex = index;
    }

    /**
     * Méthode qui passe une instance d'un interface pour déclancher un évenement et déléguer le comportement
     *
     * @param listener instance de l'interface.
     */
    public void setMedListener(MedicalInteractionListener listener){
        mListener = listener;
    }

    /**
     * Méthode qui passe le chien pour lui associer le poids.
     * @param chien chien auquel sera associé le poids créé.
     */
    public void setDog(Chien chien){
        mChien = chien;
    }

    /**
     * Méthode qui passe une instance de poids qui sera créé ou modifié.
     *
     * @param mPoids poids à modifier au créer.
     */
    public void setNewPoids(Poids mPoids){
        poids = mPoids;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.add_medical_fragment, container, false  );
        binding.setPoids( poids );
        binding.setChien( mChien );


        binding.button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( mIndex , poids, mChien );
            }
        } );

        return binding.getRoot();
    }

    public void onButtonPressed(int index, Poids poids, Chien dog) {
        if (mListener != null) {
            mListener.onMedicalInteraction( index, poids, dog );
            this.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MedicalInteractionListener {
        // TODO: Update argument type and name
        void onMedicalInteraction(int index, Poids monPoids, Chien mChien);
    }
}
