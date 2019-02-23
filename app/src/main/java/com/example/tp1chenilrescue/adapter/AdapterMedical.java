package com.example.tp1chenilrescue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.AddMedical;
import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.CellMedicalRvBinding;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.Poids;
import com.example.tp1chenilrescue.models.PoidsDataAccess;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMedical extends RecyclerView.Adapter<AdapterMedical.MyViewHolder> {

    private ArrayList<Poids> poidsArrayList;
    private CellMedicalRvBinding binding;
    private Chien monChien;
    private FragmentManager manager;
    private PoidsDataAccess dataAccess;
    private Context context;

    public AdapterMedical(ArrayList<Poids> list, FragmentManager fragmentManager, Context mContext,
                          PoidsDataAccess poidsDataAccess) {
        poidsArrayList = list;
        manager = fragmentManager;
        context = mContext;
        dataAccess = poidsDataAccess;
    }

    @Override
    public AdapterMedical.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        binding = DataBindingUtil.inflate( inflater, R.layout.cell_medical_rv, parent, false );

        return new MyViewHolder( binding );
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMedical.MyViewHolder holder, final int position) {

        holder.medicalRvBinding.setPoids( poidsArrayList.get( position ) );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedicalFragment(holder.getAdapterPosition());

            }
        } );


    }

    private void showMedicalFragment(int adapterPosition) {
        AddMedical medical = new AddMedical();
        medical.setDog( monChien );
        medical.setNewPoids( poidsArrayList.get( adapterPosition ) );
        medical.setIndexToModify( adapterPosition );
        medical.setMedListener( new AddMedical.MedicalInteractionListener() {
            @Override
            public void onMedicalInteraction(int index, Poids monPoids, Chien dog) {
                notifyItemChanged( index );
                updateMedicalInfoToDB(monPoids);
            }
        } );
        medical.show( manager, "timeToBegin" );
    }

    private void updateMedicalInfoToDB(Poids monPoids) {
        boolean isModified = dataAccess.updateMedical(monPoids);
        if (isModified){
            Toast.makeText( context,"Les informations du poids ont été modifié",
                    Toast.LENGTH_LONG ).show();
        }else {
            Toast.makeText( context,"Une erreur est survenue", Toast.LENGTH_LONG ).show();
        }
    }


    @Override
    public int getItemCount() {
        return poidsArrayList.size();
    }

    public ArrayList<Poids> getData() {
        return poidsArrayList;
    }

    public void deleteItem(int position) {
        poidsArrayList.remove( position );
        notifyItemRemoved( position );
    }

    public void restoreItem(int position, Poids poids) {
        poidsArrayList.add( position, poids );
        notifyItemInserted( position );
    }

    public void addMedicalToRV(Poids poids) {
        poidsArrayList.add( poids );
        notifyDataSetChanged();
    }

    public void passDogToAdapter(Chien mChien) {
        monChien = mChien;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CellMedicalRvBinding medicalRvBinding;


        public MyViewHolder(@NonNull CellMedicalRvBinding mBinding) {
            super( mBinding.getRoot() );
            medicalRvBinding = mBinding;

        }


    }
}
