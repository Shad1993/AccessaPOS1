package com.accessa.ibora.sales.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.accessa.ibora.R;

import java.io.IOException;

public class inbuildScannerSunmiT2Mini extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private View scannerSquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.scanner_surface);
        scannerSquare = findViewById(R.id.scanner_square);

        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        // Set deprecated camera API
        camera = Camera.open();
        camera.setDisplayOrientation(90);

        // Set the preview callback
        camera.setPreviewCallback(this);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // No need to handle surface changes in this example
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Release the camera and surface holder
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
        surfaceHolder = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // Process the camera preview frame here
    }

    // Handle camera permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
