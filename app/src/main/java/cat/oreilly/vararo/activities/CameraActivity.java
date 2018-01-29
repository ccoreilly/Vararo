package cat.oreilly.vararo.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import cat.oreilly.vararo.R;
import cat.oreilly.vararo.view.CameraView;

public class CameraActivity extends AppCompatActivity {

    private String TAG = "CameraActivity";
    private CameraView cameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.camera_view);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Ask permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        } else {

        }
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

}
