package com.example.tp1chenilrescue.dialogfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.AddChenilDiagfragBinding;
import com.example.tp1chenilrescue.models.Chenil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class AddChenil extends DialogFragment {

    private AddChenilFragmentInteraction mListener;
    private Chenil monChenil;
    private Context mContext;
    AddChenilDiagfragBinding binding;
    private int mIndex;

    public AddChenil() {
        // Required empty public constructor
    }

    public static AddChenil newInstance() {
        AddChenil fragment = new AddChenil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    /**
     * Méthode qui passe une instance d'une interface pour trapper un évenement et déléguer le comportement
     * de l'évenement à une autre méthode.
     * @param listener instance de l'interface.
     */
    public void setListener(AddChenilFragmentInteraction listener){
        this.mListener = listener;
    }

    /**
     * Méthode qui passe l'index du chenil pour permettre la modification du chenil.
     *
     * @param position index du chenil qui servira a indiquer au RecyclerView quel chenil
     *                 a été modifé.
     */
    public void setChenilIndex(int position){
        mIndex = position;
    }

    /**
     * Méthode qui permet de passer le Context de l'application pour permettre de capter la localisation
     * du chenil.
     *
     * @param context context qui sert a capter la position GPS du chenil.
     */
    public void setLocationContext(Context context){
        this.mContext = context;
    }

    /**
     * Méthode qui passe une instance d'un chenil pour l'ajout ou la modification.
     * @param chenil chenil qui doit être modifié ou créé.
     */
    public void setNewChenil(Chenil chenil){
        this.monChenil = chenil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate( inflater, R.layout.add_chenil_diagfrag,
                container,false );

        binding.setChenil( monChenil );
        binding.btnLocalisationChenil.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKennelLocation();

            }
        } );
        binding.btnConfirmer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( monChenil, mIndex );
            }
        } );

        binding.executePendingBindings();
        View view = binding.getRoot();

        return view;
    }

    private void getKennelLocation() {
        /* Vérification de la permission de capter la position GPS */
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){

            LocationServices.getFusedLocationProviderClient( mContext ).getLastLocation().
                    addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                Toast.makeText( mContext, String.valueOf(location.getLatitude()) + "   " +
                                        String.valueOf( location.getLongitude() ),
                                        Toast.LENGTH_LONG ).show();
                                monChenil.setLatitude((float)location.getLatitude());
                                monChenil.setLongitude( (float)location.getLongitude() );
                                binding.txtLongitude.setText( String.valueOf( location.getLongitude() ) );
                                binding.txtLatitude.setText( String.valueOf( location.getLatitude() ) );

                            }
                        }
                    } );

        }else {
            Toast.makeText( mContext, "L'application n'est pas autorisée à capter la localisation",
                    Toast.LENGTH_LONG ).show();
        }
    }

    public void onButtonPressed(Chenil chenil, int index) {
        if (mListener != null){
            mListener.ChenilListener( chenil, index );
        }
        this.dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AddChenilFragmentInteraction {
        // TODO: Update argument type and name
        void ChenilListener(Chenil mChenil, int index);
    }
}
