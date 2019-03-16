package com.example.tp1chenilrescue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.AddDog;
import com.example.tp1chenilrescue.views.PoidsFragmentRV;
import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.CellChienRvBinding;
import com.example.tp1chenilrescue.models.Chien;
import com.example.tp1chenilrescue.models.ChienDataAccess;
import com.example.tp1chenilrescue.models.DatabaseHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterDog extends RecyclerView.Adapter<AdapterDog.MyViewHolder> {

    private ArrayList<Chien> maListe;
    private FragmentManager fragmentManager;
    private boolean needToShowMedical;
    private Context mContext;
    private CellChienRvBinding chienRvBinding;
    private ChienDataAccess dataAccess;
    private DatabaseHelper helper;
    private boolean chenilChien;

    public AdapterDog(ArrayList<Chien> mList, FragmentManager manager, boolean showMed,
                      Context context, boolean showInKennelDog){
        maListe = mList;
        fragmentManager = manager;
        needToShowMedical = showMed;
        mContext = context;
        helper = new DatabaseHelper( mContext );
        dataAccess = new ChienDataAccess( helper );
        chenilChien = showInKennelDog;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        chienRvBinding = DataBindingUtil.inflate( inflater, R.layout.cell_chien_rv,parent,false );

        return new MyViewHolder(chienRvBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.binding.setChien( maListe.get( position ) );
        if (!chenilChien) {
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (needToShowMedical) {
                        showMedicalFragment( holder.getAdapterPosition() );
                    } else {
                        modifyDogInfo( holder.getAdapterPosition() );
                    }
                }
            } );
        }

    }

    /**
     * Méthode qui modifie les iformation du chien dans le RecyclerView.
     *
     * @param position chien qui doit être afficher dans le dialog fragment.
     */
    private void modifyDogInfo(int position) {
        final AddDog addDog = new AddDog();
        addDog.setDogInDiagFrag( maListe.get( position ) );
        addDog.setContextForSpinner( mContext );
        addDog.setDatabase( dataAccess );
        addDog.setBreedList(dataAccess.selectAllBreed());
        addDog.setIndex( position );
        addDog.setListener( new AddDog.OnDogInteractionListener() {
            @Override
            public void dogFragmentInteraction(Chien monChien, int index, int id) {
                notifyItemChanged( index, monChien );
                notifyDataSetChanged();
                updateDogInfoToDB(monChien);


            }
        } );
        addDog.show( fragmentManager, "dialog" );
    }

    /**
     * Méthode qui mets à jour le chien dans la base de données et affiche un Toast selon si une erreur
     * est survenue ou non.
     *
     * @param monChien le chien qui doit être mis à jour.
     */
    private void updateDogInfoToDB(Chien monChien) {
        boolean isUpdated = dataAccess.updateById(monChien);
        if (isUpdated){
            Toast.makeText( mContext, "Les informations du chien ont bien été modifiés.",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode qui affiche un fragment qui contient les poids qui sont relié au chien.
     *
     * @param position position du chien dans le Recyclerview
     */
    private void showMedicalFragment(int position) {
        PoidsFragmentRV fragmentRV = new PoidsFragmentRV();
        fragmentRV.setList( dataAccess.selectMEdicalByDogId( maListe.get( position ).getId() ) );
        fragmentRV.setOption( fragmentManager, mContext, maListe.get( position ) );
        fragmentManager.beginTransaction().replace( R.id.fragment_container, fragmentRV ).commit();
    }

    @Override
    public int getItemCount() {
        return maListe.size();
    }

    /**
     *Méthode qui renvoie la liste de chien pour déterminer quel chien est supprimé.
     *
     *@return la liste de chiens.
     */
    public ArrayList<Chien> getData() {
        return  maListe;
    }

    /**
     * Méthode qui procède à la suppression du chien dans la liste et le RecyclerView et qui avise
     * qu'un item a été effacé.
     *
     * @param position position dans le RecyclerView.
     */
    public void deleteItem(int position) {
        maListe.remove( position );
        notifyItemRemoved( position );
    }

    /**
     * Méthode qui procède à la restoration du chien dans le RecyclerView puisque l'utilisateur n'a pas confirmé
     * qu'il désirait effacer le chien.
     *
     * @param position position du chien dans la liste.
     * @param chien chien qui doit être restoré.
     */
    public void restoreItem(int position, Chien chien) {
        maListe.add( position, chien );
        notifyItemInserted( position );
    }

    /**
     * Méthode qui procède à l'ajout d'un chien dans la liste/RecyclerView et qui avise qu'un item a été ajouté.
     * @param monChien le chien qui a été ajouté.
     */
    public void addItemInRV(Chien monChien) {
        maListe.add( monChien );
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CellChienRvBinding binding;

        public MyViewHolder(CellChienRvBinding mBinding) {
            super( mBinding.getRoot());
            binding = mBinding;



        }

    }
}
