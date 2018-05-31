package com.xyz.leki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xyz.leki.Resource.Medicine;
import com.xyz.leki.Resource.MedicineList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MyMedActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_med);

        list = (ListView) findViewById(R.id.myMedTimesListView);


        int nr = getIntent().getIntExtra("MED_NUMBER", -1);
        if (nr != -1) {
            setList(nr);
        }
    }

    private void setList(int nr) {
        TextView medNameTextView = findViewById(R.id.myMedNameTextView);
        Medicine med = MedicineList.getMedicineList().get(nr);
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
}
