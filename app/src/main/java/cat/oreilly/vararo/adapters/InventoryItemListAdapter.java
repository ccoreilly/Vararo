package cat.oreilly.vararo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import cat.oreilly.vararo.R;
import cat.oreilly.vararo.interfaces.ItemLoaderInterface;
import cat.oreilly.vararo.model.InventoryItem;


public class InventoryItemListAdapter extends RecyclerView.Adapter<InventoryItemListAdapter.ViewHolder> {

    private List<InventoryItem> items;
    private ItemLoaderInterface itemLoader;

    public InventoryItemListAdapter(ItemLoaderInterface itemLoader, List<InventoryItem> items) {
        this.items = items;
        this.itemLoader = itemLoader;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InventoryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = null;

        // En un mòbil fem servir el layout item_llista_imatge.xml
        v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card, parent, false);

        // Creem el nostre ViewHolder a partir de la vista (pare) inflada a partir del layout corresponent i el retornem
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final InventoryItemListAdapter.ViewHolder holder, int position) {
        final InventoryItem item = items.get(position);

        final Context context = holder.recyclerView.getContext();
        String basePath = "" + context.getFilesDir();
        String imagePath = basePath + "/images/" + item.getMainPicture();
        Picasso.with(context).load(imagePath).into(holder.imatgeView);
        final UUID id = item.getUid();

        // Establim un listener per quan l'usuari pitgi sobre la nostra vista principal
        holder.recyclerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                itemLoader.loadChildren(id);
            }
        });

    }

    // Amb aquest métode el que fem es incrementar l'eficiencia de l'adapter ja que sap perfectament quantes vistes
    // ha de crear i no ho ha de comprovar
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // El ViewHolder conté les vistes de la nostra RecyclerView pare i és el que ens ajuda
        // a incrementar el rendiment a l'hora de crear les vistes per cada element de la nostra llista.
        // En comptes d'inicialitzar una vista nova per cada element el que fem és reciclar les vistes
        // del ViewHolder una vegada inicialitzat el primer i ens evita accedir al métode findViewById continuament,
        // cosa que alentiria la nostra aplicació

        // definim una variable per cada vista a la que voldrem accedir després.
        private ImageView imatgeView;
        private View recyclerView;

        public ViewHolder(View v) {
            // En el constructor del ViewHolder inicialitzem les variables per cada vista cercant-les amb el métode
            // findViewById
            super(v); // Passem primer la nostra vista pel constructor de la classe original: RecyclerView.ViewHolder

            recyclerView = v;
            imatgeView = v.findViewById(R.id.imatge_item_card);

        }
    }
}
