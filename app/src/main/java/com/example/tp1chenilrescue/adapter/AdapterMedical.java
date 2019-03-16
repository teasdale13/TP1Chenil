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
        CellMedicalRvBinding binding = DataBindingUtil.inflate( inflater, R.layout.cell_medical_rv, parent, false );

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

    /**
     * Méthode qui affiche un dialog fragment pour modifier le poids au chien.
     *
     * @param adapterPosition position du poids dans la liste.
     */
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

    /**
     * Méthode qui modifie le poids du chien dans la base de données et affiche un Toast de succès
     * ou échec selon le cas.
     *
     * @param monPoids le poids qui doit être modifié.
     */
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

    /**
     * Méthode qui renvoie la liste de poids pour le SwipeDelete();
     *
     * @return la liste de poids.
     */
    public ArrayList<Poids> getData() {
        return poidsArrayList;
    }

    /**
     * Méthode qui supprime de la liste le poids et avise qu'un item a été supprimé.
     *
     * @param position position du poids dans la liste.
     */
    public void deleteItem(int position) {
        poidsArrayList.remove( position );
        notifyItemRemoved( position );
    }

    /**
     * Méthode qui restore le poids à l'endroit où il était avant la suppression.
     *
     * @param position position où il doit être réinséré.
     * @param poids le poids qui doit être réinséré.
     */
    public void restoreItem(int position, Poids poids) {
        poidsArrayList.add( position, poids );
        notifyItemInserted( position );
    }

    /**
     * Méthode qui ajoute un poids dans la liste et avise qu'un item a été ajouté.
     *
     * @param poids poids qui doit être ajouté.
     */
    public void addMedicalToRV(Poids poids) {
        poidsArrayList.add( poids );
        notifyDataSetChanged();
    }

    /**
     * Méthode qui passe un chien pour pouvoir associer le poids au chien.
     *
     * @param mChien le chien a qui le poids doit être associé.
     */
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
