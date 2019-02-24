package com.example.qr.qscan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.service.autofill.TextValueSanitizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();
    }

    private void initActivity(){
        this.barcodeDetector = new BarcodeDetector.Builder(this)
                                .setBarcodeFormats(Barcode.QR_CODE).build();

        this.cameraSource = new CameraSource.Builder(this, barcodeDetector)
                                .setRequestedPreviewSize(640, 480).build();

        this.textView = findViewById(R.id.main_tv_qr);
        this.surfaceView = findViewById(R.id.main_surface_view);

        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Camera not permitted", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    cameraSource.start(holder);
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();

                if(qrCode.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCode.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }
}

// create a button
// as long as it's pressed sets a variable to true (View.onTouchListener)
// when it's released, the variable is false
// when the barcode detects something, it will run logic only if the variable is true
//should work nice, eh?