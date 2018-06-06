package com.xyz.leki;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class AddMedActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    public static final long INTERVAL_24H = 0;
    public static final long INTERVAL_ONCE = 1;
    private Calendar cal;
    private List<Calendar> alarmTimes;

    private ListView list ;
    private ArrayAdapter<String> timeListAdapter;
    List<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        cal = Calendar.getInstance();
        alarmTimes = new ArrayList<>();

        //get the spinner from the xml.
        Spinner alarmIntervalSpinner = findViewById(R.id.alarmIntervalSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Powtarzaj co 24h", "Nie powtarzaj"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        // There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        alarmIntervalSpinner.setAdapter(adapter);

        //set time picker format to 24h
        //TimePicker timePicker = findViewById(R.id.alermTimePicker);
        //timePicker.setIs24HourView(true);

        //set ListView
        list = (ListView) findViewById(R.id.timeListView);

        strings = new ArrayList<>(alarmTimes.size());

        ArrayList<String> timeStringsList = new ArrayList<String>();
        timeStringsList.addAll(strings);

        timeListAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, timeStringsList);

        list.setAdapter(timeListAdapter);
    }

    public void addTime(View view) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddMedActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        alarmTimes.add(cal);

                        String minuteString = "";
                        if(minute < 10)
                            minuteString += "0";
                        minuteString += minute;
                        timeListAdapter.add(hourOfDay + ":" + minuteString);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void addNotification(View view) {
        Spinner alarmIntervalSpinner = findViewById(R.id.alarmIntervalSpinner);

        TextView medNameTextView = findViewById(R.id.medNameTextView);
        String medName = medNameTextView.getText().toString();

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(this, AlarmReceiverActivity.class);
        intent.putExtra("MED_NAME", medName);
        intent.putExtra("MED_NUMBER", MedicineList.getSize());

        AlarmManager am =
                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        long selectedInterval = alarmIntervalSpinner.getSelectedItemId();

        int i = MedicineList.getMedicineList().size() * 100;
        int j = 0;
        List<Integer> requestCodes = new ArrayList<>();
        for (Calendar alarmTime : alarmTimes) {
            i += j;
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            requestCodes.add(i);
            j++;
            addAlarm(selectedInterval, am, pendingIntent, alarmTime);
        }
        TextView qt = findViewById(R.id.quantityEditText);
        TextView bc = findViewById(R.id.barcodeTextView);
        String qtStr = qt.getText().toString();
        int quantity = 0;
        if (!qtStr.isEmpty()) {
            quantity = Integer.parseInt(qtStr);
        }
        String barcode = bc.getText().toString();
        Medicine med = new Medicine(medName, alarmTimes, requestCodes, selectedInterval != INTERVAL_ONCE, quantity, barcode);
        MedicineList.addMedicine(med);
        writeArray();

        Toast.makeText(this, "Dodano lek " + medName, Toast.LENGTH_LONG).show();
    }

    private void addAlarm(long selectedInterval, AlarmManager am, PendingIntent pendingIntent, Calendar alarmCal) {
        if (selectedInterval == INTERVAL_24H) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }else if (selectedInterval == INTERVAL_ONCE) {
            am.set(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(),
                    pendingIntent);
        }
    }

    private void writeArray() {
        File f = new File(getFilesDir(), "medicine_list.srl");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream objectwrite = new ObjectOutputStream(fos);
            objectwrite.writeObject(MedicineList.getMedicineList());
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
