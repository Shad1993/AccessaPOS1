package com.accessa.ibora.sales.scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.List;

public class InbuiltScannerFragment extends Fragment {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private static final long SCAN_TIMEOUT = 3000; // Timeout in milliseconds
    private long lastScanTime = 0;
    private boolean isProcessingBarcode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbuild_scanner_sunmi_t2_mini, container, false);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = view.findViewById(R.id.surface_view);
        barcodeText = view.findViewById(R.id.barcode_text);
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep);
        initialiseDetectorsAndSources();
        return view;
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(requireContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        boolean displayFlash = true; // Set this flag based on your requirements

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            private boolean detectionConsumed = false;

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (detectionConsumed) {
                    return;
                }

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isProcessingBarcode) {
                                isProcessingBarcode = true;
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                mediaPlayer.start();
                                lastScanTime = System.currentTimeMillis();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (System.currentTimeMillis() - lastScanTime > SCAN_TIMEOUT) {
                                            isProcessingBarcode = false;
                                        }
                                    }
                                }, SCAN_TIMEOUT);

                                // Control flash display
                                // Control flash display
                                // Control flash display
                                if (displayFlash) {
                                    Camera camera = cameraSource.getCamera();
                                    if (camera != null) {
                                        Camera.Parameters parameters = camera.getParameters();
                                        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
                                        if (supportedFlashModes != null && supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                            camera.setParameters(parameters);
                                        }
                                    }
                                }

                            }
                        }
                    });
                    detectionConsumed = true;
                }
            }
        });


    }
    private void processBarcode(String barcodeData) {
        // Inside your receiveDetections() method, after getting the barcodeData:
        Bundle resultBundle = new Bundle();
        resultBundle.putString("barcode", barcodeData);
        getParentFragmentManager().setFragmentResult("barcodeKey", resultBundle);
        mediaPlayer.start();

        // Delay before processing next barcode
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isProcessingBarcode = false;
                processNextBarcode();
            }
        }, SCAN_TIMEOUT);
    }
    private void processNextBarcode() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            private boolean detectionConsumed = false;

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (detectionConsumed) {
                    return;
                }

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);

                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);

                            }

                            detectionConsumed = true;
                            isProcessingBarcode = true;
                            cameraSource.stop();
                            barcodeDetector.release();
                            processBarcode(barcodeData);
                            processNextBarcode();
                        }
                    });
                }
            }
        });

        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void turnFlashlightOn() {
        CameraManager cameraManager = (CameraManager) requireContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length > 0) {
                String cameraId = cameraIdList[0];
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Boolean hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (hasFlash != null && hasFlash) {
                    cameraManager.setTorchMode(cameraId, true);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void turnFlashlightOff() {
        CameraManager cameraManager = (CameraManager) requireContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length > 0) {
                cameraManager.setTorchMode(cameraIdList[0], false);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.release();
            turnFlashlightOff();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        turnFlashlightOn();
    }
}
