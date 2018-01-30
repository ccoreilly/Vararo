package cat.oreilly.vararo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.UUID;

import cat.oreilly.vararo.R;
import cat.oreilly.vararo.activities.MainActivity;
import cat.oreilly.vararo.interfaces.ItemLoaderInterface;
import cat.oreilly.vararo.model.InventoryItem;


public class InventoryItemListAdapter extends RecyclerView.Adapter<InventoryItemListAdapter.ViewHolder> {
    private String TAG = "InvItemListAdapter";

    private List<InventoryItem> items;
    private ItemLoaderInterface itemLoader;
    private final static int EVEN = 0;
    private final static int ODD = 1;

    public InventoryItemListAdapter(ItemLoaderInterface itemLoader, List<InventoryItem> items) {
        this.items = items;
        Log.d(TAG, items.get(0).getMainPicture());
        this.itemLoader = itemLoader;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position % 2 == 0) {
            type = EVEN;
        } else {
            type = ODD;
        }
        return type;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public InventoryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card, parent, false);

        // Creem el nostre ViewHolder a partir de la vista (pare) inflada a partir del layout corresponent i el retornem
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        InventoryItem item = items.get(position);
        Log.d(TAG, item.getName());
        final Context context = (MainActivity) holder.recyclerView.getContext();
        //String basePath = "" + context.getFilesDir();
        //String imagePath = basePath + "/images/" + item.getMainPicture();
        File f = new File(item.getMainPicture());
        //Picasso.with(context).load(f).into(holder.itemImage);
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        holder.itemImage.setImageBitmap(bmp);
        holder.itemName.setText(item.getName());
        final Long id = item.getId();

        // Establim un listener per quan l'usuari pitgi sobre la nostra vista principal
        holder.recyclerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                itemLoader.openItem(id);
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
        private ImageView itemImage;
        private TextView itemName;
        private View recyclerView;

        public ViewHolder(View v) {
            // En el constructor del ViewHolder inicialitzem les variables per cada vista cercant-les amb el métode
            // findViewById
            super(v); // Passem primer la nostra vista pel constructor de la classe original: RecyclerView.ViewHolder

            recyclerView = v;
            itemImage = v.findViewById(R.id.item_card_image);
            itemName = v.findViewById(R.id.item_card_name);

        }
    }
}
