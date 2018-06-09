package com.xyz.leki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.xyz.leki.Resource.Medicine;
import com.xyz.leki.Resource.MedicineList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity {
    private Bitmap myBitmap;
    private List<Medicine> meds;
    private Button saveButton;
    private NumberPicker medQuantityNumberPicker;
    TextView medNameTextView;
    int medIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        medIndex = -1;
        saveButton = findViewById(R.id.saveQuanitytyButton);
        medQuantityNumberPicker = findViewById(R.id.medQuantityNumberPicker);
        medNameTextView = findViewById(R.id.medNameTextView);
        saveButton.setVisibility(View.GONE);
        medQuantityNumberPicker.setVisibility(View.GONE);
        medNameTextView.setVisibility(View.GONE);

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
            int i = findMedIndex(thisCode.rawValue);
            if(i > -1) {
                medIndex = i;
                setMedInfo(i);
            } else
                txtView.setText("Nie znaleziono leku na liście");
        } else {
            txtView.setText("Nie znaleziono kodu");
        }
    }

    //find med id returns -1 if not found
    private int findMedIndex(String barcode) {
        meds = MedicineList.getMedicineList();
        int i = 0;
        for (Medicine med : meds) {
            if(med.getBarcode().equals(barcode))
                return i;
            i++;
        }
        return -1;
    }

    private void setMedInfo(int index) {
        saveButton.setVisibility(View.VISIBLE);
        medQuantityNumberPicker.setVisibility(View.VISIBLE);
        medNameTextView.setVisibility(View.VISIBLE);

        Medicine med = meds.get(index);
        medNameTextView.setText(med.getName());
        medQuantityNumberPicker.setMinValue(0);
        medQuantityNumberPicker.setMaxValue(1000);
        medQuantityNumberPicker.setValue(med.getQuantity());
    }

    public void saveMed(View view) {
        if (medIndex > -1) {
            Medicine med = meds.get(medIndex);
            med.setQuantity(medQuantityNumberPicker.getValue());
            meds.set(medIndex, med);
            writeArray(meds);
        }
    }

    public void writeArray(List<Medicine> medicineList) {
        File f = new File(getFilesDir(), "medicine_list.srl");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream objectwrite = new ObjectOutputStream(fos);
            objectwrite.writeObject(medicineList);
            fos.close();

            if (!f.exists()) {
                f.mkdirs();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
