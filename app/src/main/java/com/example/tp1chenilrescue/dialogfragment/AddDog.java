package com.example.tp1chenilrescue.dialogfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.AddDogDiagfragBinding;
import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.Race;

import java.util.ArrayList;


public class AddDog extends DialogFragment {

    private OnDogInteractionListener mListener;
    private onDogToKennelList monListener;
    private Chien chien;
    private Context context;
    private int itemToModify;
    private Chenil chenil;
    private boolean addDogToList;
    private AddDogDiagfragBinding binding;
    private ArrayList<Race> breedList;
    private ArrayList<Chien> fatherList;
    private ArrayList<Chien> motherList;
    private ChienDataAccess chienDataAccess;
    private EditText dogName;
    private DatePicker picker;
    private RadioButton selectedButton;

    public AddDog() {

    }

    public static AddDog newInstance() {
        AddDog fragment = new AddDog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    public void setBreedList(ArrayList<Race> selectAllBreed) {
        breedList = selectAllBreed;
    }

/*    public void setFatherList(ArrayList<Chien> arrayList) {
        fatherList = arrayList;
    }

    public void setMotherList(ArrayList<Chien> list) {
        motherList = list;
    }*/

    public void setDogInDiagFrag(Chien monChien) {
        chien = monChien;
    }

    public void setDatabase(ChienDataAccess database){
        chienDataAccess = database;
    }

    public void setIndex(int layoutPosition) {
        itemToModify = layoutPosition;
    }

    public void setBoolToAddDogInList(boolean addDogToDatabase) {
        addDogToList = addDogToDatabase;
    }

    public void setContextForSpinner(Context mContext) {
        context = mContext;
    }

    public void setListener(OnDogInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.add_dog_diagfrag, container, false );

        binding.setChien( chien );


        if (!chien.isFemale() || chien.isFemale()) {
            binding.radioGroup.check( binding.getChien().isFemale() ? binding.btnFemale.getId() : binding.btnMale.getId() );
        }
        int btnid = binding.radioGroup.getCheckedRadioButtonId();
        selectedButton = binding.getRoot().findViewById( btnid );

        binding.radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == binding.btnMale.getId()) {
                    binding.getChien().setSexe( binding.btnMale.getText().toString() );
                } else {
                    binding.getChien().setSexe( binding.btnFemale.getText().toString() );
                }
            }
        } );


        createBreedSpinner( breedList, binding.spinnerBreedAdd, binding.spinnerDad, binding.spinnerMother );


        return binding.getRoot();
    }

    private void createBreedSpinner(final ArrayList<Race> breedList, final Spinner spinner,
                                    final Spinner father, final Spinner mother) {

        Race mRace = new Race(  );
        mRace.setId( 0 );
        breedList.add( 0, mRace );
        Race[] spinnerItem = new Race[breedList.size()];
        spinnerItem = breedList.toArray( spinnerItem );

        ArrayAdapter<Race> arrayAdapter = new ArrayAdapter<Race>( context, android.R.layout.simple_spinner_item, spinnerItem );
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( arrayAdapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Race mRace = (Race) parent.getItemAtPosition( position );
                if (mRace.getId() != 0) {
                    binding.getChien().setRace( mRace.toString() );
                    binding.getChien().setRaceId( mRace.getId() );

                    if (mRace.getId() > 0) {
                        createParentSpinner( chienDataAccess.getParentFromDB( "M", mRace.getId() ), father, true );
                        createParentSpinner( chienDataAccess.getParentFromDB( "F", mRace.getId() ), mother, false );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        binding.buttonDogConfirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed( itemToModify, chien, chien.getId() );
            }
        } );


    }

    private void createParentSpinner(ArrayList<Chien> list, final Spinner spinner, final boolean male) {
        Chien chien = new Chien(  );
        chien.setId(0);
        list.add( 0,chien );
        Chien[] spinnerItem = new Chien[list.size()];
        spinnerItem = list.toArray( spinnerItem );

        ArrayAdapter<Chien> arrayAdapter = new ArrayAdapter<Chien>( context, android.R.layout.simple_spinner_item, spinnerItem );
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( arrayAdapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Chien chien = (Chien) parent.getItemAtPosition( position );
                if (chien.getId() != 0) {
                    if (male) {
                        binding.getChien().setIdPere( chien.getId() );
                        binding.getChien().setPere( chien.toString() );
                    } else {
                        binding.getChien().setMereId( chien.getId() );
                        binding.getChien().setMere( chien.toString() );
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        } );

    }


    public void onButtonPressed(int index, Chien monChien, int id) {
        if (mListener != null) {
            mListener.dogFragmentInteraction( monChien, index, id );
        }
        this.dismiss();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface onDogToKennelList {
        void dogToKennelList();
    }


    public interface OnDogInteractionListener {
        // TODO: Update argument type and name
        void dogFragmentInteraction(Chien monChien, int index, int id);
    }
}
