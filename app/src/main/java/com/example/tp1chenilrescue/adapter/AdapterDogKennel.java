package com.example.tp1chenilrescue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.CellChenilDogBinding;
import com.example.tp1chenilrescue.models.Chien;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDogKennel extends RecyclerView.Adapter<AdapterDogKennel.MyViewHolder> {

    private ArrayList<Chien> chiens;
    private CellChenilDogBinding binding;
    private AddToKennelInterface mListener;


    public AdapterDogKennel(ArrayList<Chien> chiens) {
        this.chiens = chiens;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate( LayoutInflater.from( parent.getContext() ),
                R.layout.cell_chenil_dog, parent, false );


        return new MyViewHolder( binding );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mBinding.setChien( chiens.get( position ) );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDogAddToKennelListener( chiens.get( position ) );
            }
        } );

    }

    public void removeItem(Chien chien) {
        chiens.remove( chien );
        notifyDataSetChanged();
    }

    public interface AddToKennelInterface{
        void onDogAddToKennelListener(Chien chien);
    }

    public void setListener(AddToKennelInterface listener){
        mListener = listener;
    }


    @Override
    public int getItemCount() {
        return chiens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CellChenilDogBinding mBinding;

        public MyViewHolder(CellChenilDogBinding binding) {
            super( binding.getRoot() );
            mBinding = binding;
        }
    }
}
