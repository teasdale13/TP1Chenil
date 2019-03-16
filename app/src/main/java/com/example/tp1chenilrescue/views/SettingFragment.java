package com.example.tp1chenilrescue.views;

import android.content.Context;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SettingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button firebaseButton;
    private StorageReference storageReference;
    private Context context;
    private String DBNAME;

    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setContext(Context mContext){
        context = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.how_to_fragment, container, false );

        firebaseButton = view.findViewById( R.id.firebaseBtn );
        storageReference = FirebaseStorage.getInstance().getReference().child( "database/" );

        final DatabaseHelper helper = new DatabaseHelper( context );
        DBNAME = helper.getDatabaseName();
        firebaseButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.close();
                PushDbToFirebase();
            }
        } );

        return view;
    }

    /**
     * Méthode qui prends le fichier de la base de données et qui fait un "BACKUP" sur Firebase.
     * affiche un Toast quand c'est effectué ou si une erreur survient.
     */
    private void PushDbToFirebase() {
            StorageReference reference = storageReference;
            Uri file = Uri.fromFile(new File("/data/data/" + context.getPackageName() +"/databases/chenilandreinc.db"));

            reference.child( DBNAME ).putFile( file ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText( context,"Sauvegardé dans Firebase", Toast.LENGTH_LONG ).show();

                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( context,"Une erreur est survenue", Toast.LENGTH_LONG ).show();
                    Log.d( "Firebase BACKUP ERROR", e.getMessage() );
                }
            } );

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
