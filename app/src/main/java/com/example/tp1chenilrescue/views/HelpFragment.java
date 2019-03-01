package com.example.tp1chenilrescue.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.tp1chenilrescue.R;
import com.google.firebase.storage.StorageReference;


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

        //File file = new File(  );
        helpWebView = view.findViewById( R.id.webView );

        helpWebView.loadUrl( "file:///android_asset/help.html" );
        /*ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService( Context.CONNECTIVITY_SERVICE );

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
            mStorageRef = FirebaseStorage.getInstance().getReference();
            mFileRef = mStorageRef.child( "help/ChenilHelp.html" );

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

        }*/



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
