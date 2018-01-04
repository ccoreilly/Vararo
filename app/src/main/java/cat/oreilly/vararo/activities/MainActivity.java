package cat.oreilly.vararo.activities;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;

import java.util.List;
import java.util.UUID;

import cat.oreilly.vararo.adapters.InventoryItemListAdapter;
import cat.oreilly.vararo.interfaces.ItemLoaderInterface;
import cat.oreilly.vararo.model.InventoryItem;
import cat.oreilly.vararo.R;

public class MainActivity extends AppCompatActivity implements ItemLoaderInterface, View.OnClickListener {
    RecyclerView recyclerView;
    InventoryItemListAdapter rvAdapter;
    List<InventoryItem> items;
    TextView textView;
    private FloatingActionButton fabAddItem, fabAddPicture, fabAddFolder;
    private Boolean isFabOpen = false;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddItem = (FloatingActionButton) findViewById(R.id.fab_new_item);
        fabAddFolder = (FloatingActionButton) findViewById(R.id.fab_new_folder);
        fabAddPicture = (FloatingActionButton) findViewById(R.id.fab_new_picture);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fabAddItem.setOnClickListener(this);
        fabAddFolder.setOnClickListener(this);
        fabAddPicture.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.no_items);
        recyclerView = (RecyclerView) findViewById(R.id.item_list); // cerquem la nostra vista de tipus RecyclerView
        loadChildren(null);
        if (items.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        rvAdapter = new InventoryItemListAdapter(this, items); // creem un nou adapter que generarà la nostra llista
        recyclerView.setAdapter(rvAdapter); // establim mAdapter com l'adapter de la nostra vista
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void loadChildren(UUID id) {
        if (id == null) {
            id = new UUID(0,0);
        }
        items = InventoryItem.find(InventoryItem.class, "parent = ?", id.toString());
    }

    public void openItem(UUID id) {
        loadChildren(id);
        rvAdapter.setItems(items);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_new_item:
                fabAnimation();
                break;
            case R.id.fab_new_folder:
                break;
            case R.id.fab_new_picture:
                break;
        }
    }
    private void fabAnimation(){

        if(isFabOpen){
            fabAddItem.startAnimation(rotateBackward);
            fabAddFolder.startAnimation(fabClose);
            fabAddPicture.startAnimation(fabClose);
            fabAddFolder.setClickable(false);
            fabAddPicture.setClickable(false);
            isFabOpen = false;
        } else {
            fabAddItem.startAnimation(rotateForward);
            fabAddFolder.startAnimation(fabOpen);
            fabAddPicture.startAnimation(fabOpen);
            fabAddFolder.setClickable(true);
            fabAddPicture.setClickable(true);
            isFabOpen = true;
        }
    }

}
