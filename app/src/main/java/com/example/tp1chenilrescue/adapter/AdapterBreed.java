package com.example.tp1chenilrescue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.AddBreed;
import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.CellBreedRvBinding;
import com.example.tp1chenilrescue.models.Race;
import com.example.tp1chenilrescue.models.RaceDataAccess;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBreed extends RecyclerView.Adapter<AdapterBreed.MyViewHolder> {

    private ArrayList<Race> races;
    private FragmentManager fragmentManager;
    private CellBreedRvBinding cellBreedRvBinding;
    private RaceDataAccess raceDataAccess;
    private Context mContext;

    public AdapterBreed(ArrayList<Race> list , FragmentManager manager) {
        races = list;
        fragmentManager = manager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cellBreedRvBinding = DataBindingUtil.inflate( LayoutInflater.from( parent.getContext() ),
                R.layout.cell_breed_rv,parent, false);

        return new MyViewHolder( cellBreedRvBinding );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.binding.setRace( races.get( position ) );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBreed fragment = new AddBreed();
                fragment.setIndex(holder.getAdapterPosition());
                fragment.setAddBreed( races.get( holder.getLayoutPosition() ) );
                fragment.setListener( new AddBreed.OnFragmentInteractionListener() {
                    @Override
                    public void onFragmentInteraction(Race mRace, int index) {
                        notifyItemChanged( index );
                        modifyBreedToDb(mRace);
                    }
                } );
                fragment.show(fragmentManager, "dialog" );
            }
        } );
    }

    /**
     * Méthode qui sert à modifier l'enregistrement.
     *
     * @param mRace La race qui doit être modifié dans la base de données.
     */
    private void modifyBreedToDb(Race mRace) {
        boolean isUpdated = raceDataAccess.updateBreed(mRace);
        if (isUpdated){
            Toast.makeText( mContext, "Les informations de la race ont été modifiés.",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode servant à passer divers objets pour le bon fonctionnement de l'adapter.
     *
     * @param dataAccess L'objet qui à accès à la base de données.
     * @param context context servant à afficher un Toast.
     */
    public void setDatabaseToAdapter(RaceDataAccess dataAccess, Context context) {
        raceDataAccess = dataAccess;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public void addBreedToRV(Race maRace) {
        races.add( maRace );
        notifyDataSetChanged();
    }

    public ArrayList<Race> getData() {
        return races;
    }

    public void deleteItem(int position) {
        races.remove( position );
        notifyItemRemoved( position );
    }

    public void restoreItem(int position, Race race) {
        races.add( position, race );
        notifyItemInserted( position );
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        CellBreedRvBinding binding;

        public MyViewHolder(CellBreedRvBinding mBinding) {
            super( mBinding.getRoot() );
            binding = mBinding;
        }
    }
}
