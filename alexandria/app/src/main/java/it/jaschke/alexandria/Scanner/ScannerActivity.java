package it.jaschke.alexandria.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import it.jaschke.alexandria.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        List<BarcodeFormat> barcodeFormats = new ArrayList<BarcodeFormat>();
        barcodeFormats.add(BarcodeFormat.EAN_13);
        mScannerView.setFormats(barcodeFormats);
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result result) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(getString(R.string.scan_extra), result.getText());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
