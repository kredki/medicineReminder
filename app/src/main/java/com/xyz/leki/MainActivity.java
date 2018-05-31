package com.xyz.leki;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xyz.leki.Resource.Medicine;
import com.xyz.leki.Resource.MedicineList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medicineList = new ArrayList<>();
        /*/
        List <Long> reqCodes = new ArrayList<>();
        reqCodes.add(1L);
        List<Calendar> calList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 20);
        calList.add(cal);
        medicineList.add(new Medicine("Apap2", reqCodes, calList, true));
        writeArray();
        /*/
        MedicineList.getInstance();
        medicineList = null;
        medicineList = read(this);
        if (medicineList != null) {
            MedicineList.setMedicineList(medicineList);
            Toast.makeText(this, "lista wczytana", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " lista pusta", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeActivityAddMed(View view) {
        Intent intent = new Intent(this, AddMedActivity.class);
        startActivity(intent);
    }

    public void writeArray() {
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

    public ArrayList<Medicine> read(Context context) {

        ObjectInputStream input = null;
        ArrayList<Medicine> ReturnClass = null;
        File f = new File(getFilesDir(), "medicine_list.srl");
        try {

            input = new ObjectInputStream(new FileInputStream(f));
            ReturnClass = (ArrayList<Medicine>) input.readObject();
            input.close();

        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ReturnClass;
    }

    public void changeToMyMedsActivity(View view) {
        Intent intent = new Intent(this, MyMedsActivity.class);
        startActivity(intent);
    }
}
