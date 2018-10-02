package com.votclick.andrew.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.votclick.andrew.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.votclick.andrew.myapplication.activity.MainActivity.URL;

public class QRActivity extends AppCompatActivity {

    private CodeScanner codeScanner;

    @BindView(R.id.codeScannerView)
    CodeScannerView codeScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        if (!checkCameraPermissions()) {
            requestCameraPermissions();
        } else {
            initCodeScanner();
        }
    }

    private void initCodeScanner() {
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            if (!URLUtil.isValidUrl(result.getText())) {
                Toast.makeText(QRActivity.this, getString(R.string.not_web), Toast.LENGTH_SHORT).show();
            } else {
                sendResult(result.getText());
            }
        }));
        codeScannerView.setOnClickListener(v -> codeScanner.startPreview());

    }

    private boolean checkCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCodeScanner();
                } else {
                    requestCameraPermissions();
                }
                break;
            }
        }
    }

    private void sendResult(String url) {
        Intent intent = new Intent();
        intent.putExtra(URL, url);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }
}
