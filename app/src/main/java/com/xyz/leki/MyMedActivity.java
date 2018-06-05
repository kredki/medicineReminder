package com.xyz.leki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.xyz.leki.Resource.Medicine;
import com.xyz.leki.Resource.MedicineList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyMedActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter;
    private Medicine med;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_med);

        list = (ListView) findViewById(R.id.myMedTimesListView);
        TextView barcode = findViewById(R.id.barcodeEditText);
        NumberPicker quantity = findViewById(R.id.quantityNumberPicker);

        index = getIntent().getIntExtra("MED_NUMBER", -1);
        med = null;
        if (index != -1) {
            med = MedicineList.getMedicineList().get(index);
            setList(index, med);
            barcode.setText(med.getBarcode());
            quantity.setMaxValue(10000);
            quantity.setMinValue(0);
            quantity.setValue(med.getQuantity());
        }
    }

    public void saveMedInfo(View view) {
        TextView barcode = findViewById(R.id.barcodeEditText);
        NumberPicker quantity = findViewById(R.id.quantityNumberPicker);
        med.setQuantity(quantity.getValue());
        med.setBarcode(barcode.getText().toString());
        MedicineList.setMedicine(index, med);
        writeArray(MedicineList.getMedicineList());
        Toast.makeText(this, "zapisano", Toast.LENGTH_SHORT).show();
    }

    private void setList(int nr, Medicine med) {
        TextView medNameTextView = findViewById(R.id.myMedNameTextView);
        medNameTextView.setText(med.getName());

        List<String> strings = new ArrayList<>();
        List<Calendar> alarmTimes = med.getAlarmTimeList();
        for (Calendar cal : alarmTimes) {
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String time = hour + ":";
            if(minute < 10)
                time += "0";
            time += minute;
            strings.add(time);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_layout, strings);
        list.setAdapter(adapter);
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
}
