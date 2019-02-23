package com.example.tp1chenilrescue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.AddDog;
import com.example.tp1chenilrescue.PoidsFragmentRV;
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

        /*holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DogInfoNFCReader infoNFCReader = new DogInfoNFCReader();
                infoNFCReader.setDogToFragment( maListe.get( holder.getAdapterPosition() ) );
                infoNFCReader.show( fragmentManager, "show" );
                return true;
            }
        } );*/

    }

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
                updateDogInfoToDB(monChien, id);


            }
        } );
        addDog.show( fragmentManager, "dialog" );
    }

    private void updateDogInfoToDB(Chien monChien, int id) {
        boolean isUpdated = dataAccess.updateById(monChien);
        if (isUpdated){
            Toast.makeText( mContext, "Les informations du chien ont bien été modifiés.",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG).show();
        }
    }

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

    public ArrayList<Chien> getData() {
        return  maListe;
    }

    public void deleteItem(int position) {
        maListe.remove( position );
        notifyItemRemoved( position );
    }

    public void restoreItem(int position, Chien chien) {
        maListe.add( position, chien );
        notifyItemInserted( position );
    }

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
