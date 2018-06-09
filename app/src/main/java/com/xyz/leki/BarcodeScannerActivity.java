package com.xyz.leki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarcodeScannerActivity extends AppCompatActivity {
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        Button btn = (Button) findViewById(R.id.scanButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });

        //set img
        ImageView myImageView = (ImageView) findViewById(R.id.barcodeImageView);
        myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.barcode2);
        myImageView.setImageBitmap(myBitmap);
    }

    private void scanBarcode() {
        //set barcode detector
        TextView txtView = findViewById(R.id.barcodeTextView);
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.CODE_128 | Barcode.EAN_13 |Barcode.EAN_8
                                | Barcode.UPC_A | Barcode.UPC_E)
                        .build();
        if(!detector.isOperational()){
            txtView.setText("Could not set up the detector!");
            return;
        }

        //detect barcode
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        //decode barcode
        if (barcodes.size() != 0) {
            Barcode thisCode = barcodes.valueAt(0);
            txtView.setText(thisCode.rawValue);
        } else {
            txtView.setText("Nie znaleziono kodu");
        }
    }

//    public void scanBarcode(View view) {
//        ImageView barcodeImageView = findViewById(R.id.barcodeImageView);
//        Bitmap bitmap=((BitmapDrawable)barcodeImageView.getDrawable()).getBitmap();
//
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
//                .build();
//        if(barcode.isOperational()){
//            SparseArray<Barcode> sparseArray = barcodeDetector.detect(frame);
//            if(sparseArray != null && sparseArray.size() > 0){
//                for (int i = 0; i < sparseArray.size(); i++){
//                    Log.d(LOG_TAG, "Value: " + sparseArray.valueAt(i).rawValue + "----" + sparseArray.valueAt(i).displayValue);
//                    Toast.makeText(LOG_TAG, sparseArray.valueAt(i).rawValue, Toast.LENGTH_SHORT).show();
//
//                }
//            }else {
//                Log.e(LOG_TAG,"SparseArray null or empty");
//            }
//
//        }else{
//            Log.e(LOG_TAG, "Detector dependencies are not yet downloaded");
//        }
//    }
}
