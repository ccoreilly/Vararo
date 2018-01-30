package cat.oreilly.vararo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cat.oreilly.vararo.adapters.InventoryItemListAdapter;
import cat.oreilly.vararo.interfaces.ItemLoaderInterface;
import cat.oreilly.vararo.model.InventoryItem;
import cat.oreilly.vararo.R;

public class MainActivity extends AppCompatActivity implements ItemLoaderInterface, View.OnClickListener {
    private String TAG = "MainActivity";

    RecyclerView recyclerView;
    InventoryItemListAdapter rvAdapter;
    List<InventoryItem> items;
    TextView textView;
    ImageView mainImage;
    private FloatingActionButton fabAddItem, fabAddPicture, fabAddFolder;
    private Boolean isFabOpen = false;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    Long currentParent;
    InventoryItem currentItem;

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

        textView =  findViewById(R.id.no_items);
        recyclerView = findViewById(R.id.item_list); // cerquem la nostra vista de tipus RecyclerView
        mainImage = findViewById(R.id.item_main_image);

        captureIntent(null);

        loadChildren(currentParent);
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

    @Override
    protected void onNewIntent(Intent intent) {
        captureIntent(intent);
    }

    private void captureIntent(Intent intent) {
        if (intent == null) {
            if (getIntent() != null && getIntent().getAction() != null) {
                intent = getIntent();
            }
        }

        currentParent = (Long) this.getIntent().getSerializableExtra(CameraActivity.PARENT_UUID);
    }

    private void loadChildren(Long id) {
        if (id == null) {
            currentParent = 0L;
        } else {
            currentParent = id;
        }
        items = InventoryItem.find(InventoryItem.class, "parent = ?", currentParent.toString());
    }

    public void openItem(Long id) {
        Log.d(TAG, "Current item: " + id.toString());
        loadChildren(id);
        if(!id.equals(0L)) {
            currentItem = InventoryItem.findById(InventoryItem.class, id);
            File f = new File(currentItem.getMainPicture());
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            mainImage.setImageBitmap(bmp);
            mainImage.setImageAlpha(100);
        } else {
            mainImage.setImageResource(android.R.color.transparent);
        }

        if (items.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rvAdapter.setItems(items);
        }
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
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(CameraActivity.PARENT_UUID, currentParent);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        if (!currentParent.equals(0L)) {
            Long id = currentItem.getParent();
            Log.d(TAG, "Loading parent: " + id.toString());
            openItem(id);
        }
    }

}
