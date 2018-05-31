package com.xyz.leki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xyz.leki.Resource.Medicine;
import com.xyz.leki.Resource.MedicineList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyMedsActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meds);

        list = (ListView) findViewById(R.id.myMedsListView);

        List<Medicine> medList = MedicineList.getMedicineList();

        List<String> strings = new ArrayList<>(medList.size());
        for (Object object : medList) {
            strings.add(Objects.toString(object, null));
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_layout, strings);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //ListEntry entry= (ListEntry) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyMedsActivity.this, MyMedActivity.class);
                //intent.putExtra("MED_NAME", "Med Name");
                intent.putExtra("MED_NUMBER", position);
                startActivity(intent);
            }
        });
    }
}
