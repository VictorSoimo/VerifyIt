package com.food.verifyit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.common.InputImage;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraPreview extends ViewGroup {
    private final PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private final ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
    private ImageAnalysis imageAnalysis;
    private OnBarcodeScannedListener barcodeScannedListener;

    @OptIn(markerClass = ExperimentalGetImage.class)
    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        previewView = new PreviewView(context);
        addView(previewView);
    }

    @ExperimentalGetImage
    public void start(OnBarcodeScannedListener listener) {
        this.barcodeScannedListener = listener;
        setupCamera(getContext());
    }

    @ExperimentalGetImage
    private void setupCamera(Context context) {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder()
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                    @SuppressLint("UnsafeExperimentalUsageError")
                    InputImage inputImage = InputImage.fromMediaImage(Objects.requireNonNull(image.getImage()), image.getImageInfo().getRotationDegrees());
                    BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_CODE_128)
                            .build();
                    BarcodeScanner scanner = BarcodeScanning.getClient(options);

                    scanner.process(inputImage)
                            .addOnSuccessListener(barcodes -> {
                                for (Barcode barcode : barcodes) {
                                    String barcodeData = barcode.getRawValue();
                                    if (barcodeScannedListener != null) {
                                        barcodeScannedListener.onBarcodeScanned(barcodeData);
                                    }
                                }
                            })
                            .addOnFailureListener(Throwable::printStackTrace)
                            .addOnCompleteListener(task -> image.close());
                });


                cameraProvider.bindToLifecycle((LifecycleOwner) getContext(), cameraSelector, preview);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                retryCamera();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    public void retryCamera(){
        setupCamera(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        previewView.layout(0, 0, getWidth(), getHeight());
    }

    public void stop() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    public interface OnBarcodeScannedListener {
        void onBarcodeScanned(String scannedData);
    }
}
