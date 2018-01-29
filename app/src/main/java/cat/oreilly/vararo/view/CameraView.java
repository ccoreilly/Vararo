package cat.oreilly.vararo.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.Camera.CameraInfo;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraView extends SurfaceView  implements SurfaceHolder.Callback {
    private String TAG = "CameraView";

    private SurfaceHolder holder;
    private Camera camera;
    private Surface surface;
    private boolean previewStarted = false;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() throws IOException {
        Log.d(TAG, "Opening camera");
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
        if (camera == null) throw new IOException("No back-facing camera");

        // Start the preview when the camera and the surface are both ready
        if (surface != null && !previewStarted) startPreview(getHolder());
    }

    private void startPreview(SurfaceHolder holder) throws IOException {
        Log.d(TAG, "Starting preview");
        if (camera == null) throw new IOException("Camera is null");
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewStarted = true;
        } catch (IOException | RuntimeException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created");
        surface = holder.getSurface();
        try {
            if (camera != null && !previewStarted) startPreview(holder);
        } catch (IOException e) {
            Log.d(TAG, "Error starting preview: " + e.toString());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            Parameters params = camera.getParameters();
            camera.setDisplayOrientation(90);
            List<Size> sizes = params.getSupportedPreviewSizes();
            params.setPreviewSize(h, w);
            camera.setParameters(params);
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (IOException e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface destroyed");
        if (surface != null && surface != holder.getSurface()) {
            Log.d(TAG,"Releasing old surface");
            surface.release();
        }
        surface = null;
        holder.getSurface().release();
    }


}
