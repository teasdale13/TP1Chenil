package com.example.tp1chenilrescue;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.os.Parcelable;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.DogInfoNFCReader;
import com.example.tp1chenilrescue.models.ChienDataAccess;

import java.io.UnsupportedEncodingException;

/**
 * @author Kevin Teasdale-Dubé
 *
 * FragmentActivity qui gère le NFC Reader.
 */
public class NFCReader extends FragmentActivity {

    private NFCFragmentListener mListener;
    private NfcAdapter nfcAdapter;
    private Context mContext;
    private ChienDataAccess dataAccess;

    public NFCReader() {
        // Required empty public constructor
    }

    public static NFCReader newInstance(String param1, String param2) {
        NFCReader fragment = new NFCReader();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.fragment_nfcreader );
        Bundle bundle = getIntent().getBundleExtra( "dataAccess" );
        dataAccess = (ChienDataAccess) bundle.getSerializable( "database" );

        NFCAvailable();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            byte[] payload = msg.getRecords()[0].getPayload();
            String languageCode = "";
            try {
                languageCode = new String(payload, 1
                        ,payload[0] & Byte.parseByte("00111111",2),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Envoyer à la DB le int receuilli pour afficher dans le dialogfragment.

            int index = Integer.parseInt( new String( payload,languageCode.length()+1
                    , payload.length - languageCode.length()-1 ) );

            DogInfoNFCReader infoNFCReader = new DogInfoNFCReader();
            infoNFCReader.setDogToFragment(dataAccess.selectDogById( index ));
            infoNFCReader.show( getSupportFragmentManager(), "show" );

        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter == null) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[] { };
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setListener(NFCFragmentListener listener){
        mListener = listener;
    }


    /**
     * Méthode qui vérifie si l'application possède la fonction du NFC Reader sur le téléphone. Affiche
     * un Toast selon le cas.
     */
    private void NFCAvailable(){
        nfcAdapter = NfcAdapter.getDefaultAdapter( getApplicationContext() );

        boolean isAvailable = nfcAdapter != null && nfcAdapter.isEnabled();
        if (isAvailable){
            Toast.makeText( getApplicationContext(), "NFC AVAILABLE", Toast.LENGTH_SHORT ).show();
        }else {
            Toast.makeText( getApplicationContext(), "NFC NOT AVAILABLE", Toast.LENGTH_SHORT ).show();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.NFCReaderListener( uri );
        }
    }


    public interface NFCFragmentListener {
        // TODO: Update argument type and name
        void NFCReaderListener(Uri uri);
    }
}
