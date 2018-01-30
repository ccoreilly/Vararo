package cat.oreilly.vararo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cat.oreilly.vararo.R;
import cat.oreilly.vararo.interfaces.SaveImageInterface;
import cat.oreilly.vararo.model.InventoryItem;
import cat.oreilly.vararo.view.CameraView;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener, SaveImageInterface {

    private String TAG = "CameraActivity";
    private CameraView cameraView;
    private ImageButton shutterButton;
    private String picturePath;
    private View saveLayer;
    private ImageView imagePreview;
    private EditText itemName;
    private Long parent;

    public static String PARENT_UUID = "parent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        parent = (Long) this.getIntent().getSerializableExtra(PARENT_UUID);

        cameraView = findViewById(R.id.camera_view);
        cameraView.setSaveImageCallback(this);

        shutterButton = findViewById(R.id.shutter);
        saveLayer = findViewById(R.id.save_layer);
        imagePreview = findViewById(R.id.image_preview);
        itemName = findViewById(R.id.item_name);
        Button saveButton = findViewById(R.id.save_button);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Ask permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }

        shutterButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            cameraView.start();
        } catch (IOException e) {
            Log.d(TAG, "Cannot start cameraView: " + e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            cameraView.stop();
        } catch (IOException e) {
            Log.d(TAG, "Cannot stop cameraView: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.shutter:
                cameraView.takePicture();
                break;
            case R.id.save_button:
                saveItemAndBack();
        }
    }

    public void saveImage(byte[] data) {
        File pictureFile = null;
        try {
            pictureFile = createImageFile();
        } catch (IOException e) {
            Log.d(TAG, "Error creating image file: " + e.getMessage());
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        Bitmap bmp = BitmapFactory.decodeFile(pictureFile.getPath().toString());
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            loadImage();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.d(TAG, timeStamp);
        String imageFileName = "Vararo_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        picturePath = image.getAbsolutePath();
        return image;
    }

    private void loadImage(){
        shutterButton.setVisibility(View.GONE);
        cameraView.setVisibility(View.GONE);
        File f = new File(picturePath);
        Picasso.with(this).load(f).into(imagePreview);
        imagePreview.setVisibility(View.VISIBLE);
        saveLayer.setVisibility(View.VISIBLE);
        itemName.requestFocus();
    }

    private void saveItemAndBack() {
        InventoryItem item = new InventoryItem(itemName.getText().toString(), picturePath, null, parent, null);
        item.save();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PARENT_UUID, parent);
        startActivity(intent);
    }
}
