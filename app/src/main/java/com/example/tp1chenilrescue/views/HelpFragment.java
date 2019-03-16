package com.example.tp1chenilrescue.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.tp1chenilrescue.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class HelpFragment extends Fragment {

    private WebView helpWebView;
    private StorageReference mStorageRef;
    private StorageReference mFileRef;
    private OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.help_fragment, container, false );

        helpWebView = view.findViewById( R.id.webView );


        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService( Context.CONNECTIVITY_SERVICE );

        /* Vérifie si une connection à une réseau WIFI ou cellulaire est disponible */
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            mStorageRef = FirebaseStorage.getInstance().getReference();
            mFileRef = mStorageRef.child( "help/index.html" );

            try {
                final File file = File.createTempFile( "ChenilHelp",".html" );
                final FileDownloadTask task = mFileRef.getFile(file);

                task.addOnSuccessListener( new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        helpWebView.loadUrl( file.toURI().toString());
                    }
                } );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            helpWebView.loadUrl( "file:///android_asset/ChenilHelp.html" );
        }

        return view;
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
