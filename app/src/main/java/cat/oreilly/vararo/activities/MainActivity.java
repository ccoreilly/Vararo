package cat.oreilly.vararo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.util.List;
import java.util.UUID;

import cat.oreilly.vararo.adapters.InventoryItemListAdapter;
import cat.oreilly.vararo.interfaces.ItemLoaderInterface;
import cat.oreilly.vararo.model.InventoryItem;
import cat.oreilly.vararo.R;

public class MainActivity extends AppCompatActivity implements ItemLoaderInterface {
    RecyclerView recyclerView;
    RecyclerView.Adapter rvAdapter;
    List<InventoryItem> items;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.no_items);
        recyclerView = (RecyclerView) findViewById(R.id.item_list); // cerquem la nostra vista de tipus RecyclerView
        loadChildren(null);
        if (items.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rvAdapter = new InventoryItemListAdapter(this, items); // creem un nou adapter que generarà la nostra llista
            recyclerView.setAdapter(rvAdapter); // establim mAdapter com l'adapter de la nostra vista
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
    }

    public void loadChildren(UUID id) {
        if (id == null) {
            id = new UUID(0,0);
        }
        items = InventoryItem.find(InventoryItem.class, "parent = ?", id.toString());
    }
}
