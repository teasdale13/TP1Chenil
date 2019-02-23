package com.example.tp1chenilrescue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.models.Race;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Kevin Teasdale-Dubé
 *
 * Adapter du RecyclerView qui affiche les races qui proviennent de Firebase.
 */
public class FirebaseRVAdapter extends RecyclerView.Adapter<FirebaseRVAdapter.MyViewHolder> {

    private ArrayList<Race> races;
    private TextView textView;
    private BreedListener mListener;

    public interface BreedListener{
        void onBreedListener(Race race);
    }

    public FirebaseRVAdapter(ArrayList<Race> mRaces) {
        races = mRaces;
    }

    public void setListener(BreedListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.firebase_rv_cell, parent, false);
        textView = view.findViewById( R.id.firebaseTextView );

        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        textView.setText( races.get( position ).getName()) ;

        textView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBreedListener( races.get( position ) );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
        }
    }
}
