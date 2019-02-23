package com.example.tp1chenilrescue.dialogfragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tp1chenilrescue.R;


public class ConfirmDelete extends DialogFragment {

    private ConfirmDeleteListener mListener;

    public ConfirmDelete() {
        // Required empty public constructor
    }

    public static ConfirmDelete newInstance() {
        ConfirmDelete fragment = new ConfirmDelete();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setListener(ConfirmDeleteListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.confirm_delete, container, false );

        Button btnNo = view.findViewById( R.id.btnNo );
        Button btnYes = view.findViewById( R.id.btnYess );


        btnNo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( false );
            }
        } );

        btnYes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( true );
            }
        } );

        return view;
    }

    public void onButtonPressed(boolean yesOrNo) {
        if (mListener != null) {
            mListener.confirmDelete( yesOrNo );
            this.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface ConfirmDeleteListener {

        void confirmDelete(boolean response);
    }
}
