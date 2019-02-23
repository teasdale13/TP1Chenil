package com.example.tp1chenilrescue.dialogfragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.DogInfoNfcreaderBinding;
import com.example.tp1chenilrescue.models.Chien;


public class DogInfoNFCReader extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private Chien monChien;
    private DogInfoNfcreaderBinding binding;

    public DogInfoNFCReader() {
    }

    public static DogInfoNFCReader newInstance() {
        DogInfoNFCReader fragment = new DogInfoNFCReader();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    public void setDogToFragment(Chien selectDogById) {
        monChien = selectDogById;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.dog_info_nfcreader, container, false  );
        binding.setChien( monChien );
        binding.infoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( );
            }
        } );
        return binding.getRoot();
    }

    public void onButtonPressed() {
        this.dismiss();
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
