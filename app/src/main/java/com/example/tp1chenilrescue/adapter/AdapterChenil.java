package com.example.tp1chenilrescue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp1chenilrescue.dialogfragment.AddChenil;
import com.example.tp1chenilrescue.views.DogFragmentRV;
import com.example.tp1chenilrescue.R;
import com.example.tp1chenilrescue.databinding.CellChenilRvBinding;
import com.example.tp1chenilrescue.models.Chenil;
import com.example.tp1chenilrescue.models.ChenilDataAccess;
import com.example.tp1chenilrescue.models.DatabaseHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterChenil extends RecyclerView.Adapter<AdapterChenil.MyViewHolder> {

    private ArrayList<Chenil> chenilArrayList;
    private FragmentManager fragmentManager;
    private boolean showDogRV, showMedicalRV;
    private Context mContext;
    private ChenilDataAccess dataAccess;
    private DatabaseHelper helper;
    private boolean kennelDogRV;

    public AdapterChenil(ArrayList<Chenil> maListe, FragmentManager manager,
                         boolean showDog, Context context, boolean showMedical, boolean RVkennelDog) {
        chenilArrayList = maListe;
        fragmentManager = manager;
        showDogRV = showDog;
        mContext = context;
        showMedicalRV = showMedical;
        helper = new DatabaseHelper( context );
        dataAccess = new ChenilDataAccess( helper );
        kennelDogRV = RVkennelDog;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        CellChenilRvBinding mBinding = DataBindingUtil.inflate( inflater, R.layout.cell_chenil_rv, parent, false );

        return new MyViewHolder( mBinding );
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.binding.setChenil( chenilArrayList.get( holder.getAdapterPosition() ) );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDogRV) {
                    showDogRecyclerView( holder.getAdapterPosition() );
                } else {
                    showChenilInfo( holder.getAdapterPosition() );
                }
            }
        } );
    }


    @Override
    public int getItemCount() {
        return chenilArrayList.size();
    }

    /**
     * Méthode qui ajoute un chenil à la liste pour raffraichir le RecyclerView et le notifie à L'adapter.
     *
     * @param monChenil le chenil a ajouter.
     */
    public void addChenil(Chenil monChenil) {
        chenilArrayList.add( monChenil );
        this.notifyDataSetChanged();
    }

    /**
     * Méthode qui supprime un chenil de la liste à une position précise et le notifie à l'adapter.
     *
     * @param position la position du chenil dans la liste.
     */
    public void deleteItem(final int position) {
        chenilArrayList.remove( position );
        notifyItemRemoved( position );
    }

    /**
     * Méthode qui réinsère le chenil à la même position si l'utilisateur
     * n'a pas confirmé la suppression du chenil.
     *
     * @param position la position où le chenil doit être réinséré.
     * @param chenil le chenil à réinsérer.
     */
    public void restoreItem(final int position, Chenil chenil) {
        chenilArrayList.add( position, chenil );
        notifyItemInserted( position );
    }

    /**
     * Méthode qui renvoie la liste de chenil pour déterminer quel chenil est supprimé.
     *
     * @return la liste de chenils.
     */
    public ArrayList<Chenil> getData() {
        return chenilArrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CellChenilRvBinding binding;

        public MyViewHolder(final CellChenilRvBinding mBinding) {
            super( mBinding.getRoot() );
            binding = mBinding;
        }
    }

    /**
     * Lors du OnclickListener sur les cellules du RecyclerView, un dialog fragment est affiché
     * pour pouvoir être midifié ou consulté.
     *
     * @param position la position dans le RecyclerView.
     */
    private void showChenilInfo(int position) {
        AddChenil addChenil = new AddChenil();
        addChenil.setNewChenil( dataAccess.selectKennellById( chenilArrayList.get( position ).getId() ) );
        addChenil.setChenilIndex( position );
        addChenil.setListener( new AddChenil.AddChenilFragmentInteraction() {
            @Override
            public void ChenilListener(Chenil mChenil, int index) {
                chenilArrayList.set( index, mChenil );
                notifyItemChanged( index );
                updateKennelToDB( mChenil );


            }
        } );
        addChenil.setLocationContext( mContext );
        addChenil.show( fragmentManager, "diaolog" );
    }

    /**
     * Méthode qui modifie les information du chenil dans la base de données et renvoie un message
     * de succès/erreur selon le cas
     *
     * @param mChenil le chenil a modifié
     */
    private void updateKennelToDB(Chenil mChenil) {
        boolean isUpdated = dataAccess.updateKennel( mChenil );
        if (isUpdated) {
            Toast.makeText( mContext, "Les modifications du chenil ont été effectuées.",
                    Toast.LENGTH_LONG ).show();
        } else {
            Toast.makeText( mContext, "Une erreur est survenue.",
                    Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * Méthode qui affiche un RecyclerView des chiens qui ne sont pas dans le chenil.
     *
     * @param chenilIndex la position du chenil dans le RecyclerView.
     */
    private void showDogRecyclerView(int chenilIndex) {
        DogFragmentRV fragment = new DogFragmentRV();
        fragment.setShowMedical( showMedicalRV, !showMedicalRV, kennelDogRV);
        // Sert à garder une référence au chenil pour créer de nouveaux chiens qui lui appartient.
        fragment.setChenilForID( chenilArrayList.get( chenilIndex ) );
        fragment.setContext( mContext );
        fragment.setFragmentManager( fragmentManager );
        // Faire une requete pour avoir les chiens qui appartiennent au chenil.
        fragment.setList( dataAccess.selectDogByKennelToShowInRV( String.valueOf( chenilArrayList.get( chenilIndex ).getId() ) ) );
        fragmentManager.beginTransaction().replace( R.id.fragment_container,
                fragment ).commit();
    }
}
