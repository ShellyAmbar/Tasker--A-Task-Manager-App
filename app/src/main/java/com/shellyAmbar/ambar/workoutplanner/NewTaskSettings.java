package com.shellyAmbar.ambar.workoutplanner;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.shellyAmbar.ambar.workoutplanner.Models.ModelTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewTaskSettings extends AppCompatActivity implements View.OnClickListener,TimePickerDialog.OnTimeSetListener,TimePicker.OnTimeChangedListener {
    private EditText title_edt,body_edt;
    private RadioGroup radioGroup;
    private RadioButton important_radio,un_important_radio;
    private SwitchCompat switch_alert;
    private Button add_new_task_btn;
    private boolean isAlerted = false;
    private boolean isImportant = false;
    private int dayPicked,monthPicked,yearPicked,hourPicked,minutePicked;
    private TextView alert_date_text;
    private String category;
    private Toolbar toolbar;
    private MediaPlayer click_sound;
    private Context context;
    private TimePicker timePicker;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task_settings);
        title_edt=findViewById(R.id.title_edt);
        body_edt=findViewById(R.id.body_edt);
        radioGroup =findViewById(R.id.radio_group);
        important_radio=findViewById(R.id.important_radio);
        un_important_radio=findViewById(R.id.un_important_radio);
        switch_alert=findViewById(R.id.switch_alert);

        add_new_task_btn=findViewById(R.id.add_new_task_btn);
        alert_date_text=findViewById(R.id.alert_date_text);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("     ");
        context=getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        click_sound = MediaPlayer.create(NewTaskSettings.this,R.raw.click);
        category=getIntent().getStringExtra("category");
        dayPicked=0;
        monthPicked=0;
        yearPicked=0;
        hourPicked=0;
        minutePicked=0;
        id=0;
        add_new_task_btn.setOnClickListener(this);

        switch_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    click_sound.start();
                    switch_alert.setText(R.string.remove_alaram);
                    switch_alert.setTextColor(ContextCompat.getColor(NewTaskSettings.this,R.color.red));
                    isAlerted=true;
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    int hour= calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    final Calendar newCalendar = Calendar.getInstance();
                    final DatePickerDialog  StartTime = new DatePickerDialog(NewTaskSettings.this,R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            dayPicked = dayOfMonth;
                            monthPicked = monthOfYear +1;
                            yearPicked = year;

                            Toast.makeText(NewTaskSettings.this, year +"/"+monthPicked+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                            alert_date_text.setText(dayPicked+"/"+monthPicked+"/"+yearPicked);


                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    StartTime.show();
                    setTime();

                }else{
                    click_sound.start();
                    hourPicked=0;
                    minutePicked=0;
                    dayPicked=0;
                    monthPicked=0;
                    yearPicked=0;
                    alert_date_text.setText(R.string.alert_hint);
                    isAlerted=false;
                    switch_alert.setText(R.string.add_alaram);
                    switch_alert.setTextColor(ContextCompat.getColor(NewTaskSettings.this,R.color.greeen));
                }
            }
        });





        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.important_radio)
                {
                    click_sound.start();
                    radioGroup.findViewById(R.id.important_radio)
                            .setBackgroundColor(Color.GREEN);
                    radioGroup.findViewById(R.id.un_important_radio)
                            .setBackgroundColor(Color.TRANSPARENT);
                    isImportant=true;


                }else if(checkedId == R.id.un_important_radio){

                    click_sound.start();
                    radioGroup.findViewById(R.id.important_radio)
                            .setBackgroundColor(Color.TRANSPARENT);
                    radioGroup.findViewById(R.id.un_important_radio)
                            .setBackgroundColor(Color.GREEN);
                    isImportant=false;
                }

            }
        });


    }

    private void setTime()
    {
        final android.app.AlertDialog.Builder alertDialog=
                new android.app.AlertDialog.Builder(NewTaskSettings.this,R.style.AlertDialogCustom);
        TimePicker timePicker = new TimePicker(NewTaskSettings.this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                final StringBuffer strBuf = new StringBuffer();
                strBuf.append(hourOfDay);
                strBuf.append(":");
                strBuf.append(minute);
                minutePicked=minute;
                hourPicked=hourOfDay;
                Toast.makeText(NewTaskSettings.this, strBuf.toString(), Toast.LENGTH_SHORT).show();



            }
        });

        LinearLayout linearLayout=new LinearLayout(context);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lllp);
        linearLayout.addView(timePicker,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        ,ViewGroup.LayoutParams.MATCH_PARENT,0));
        alertDialog.setView(linearLayout);




        alertDialog.show();

    }









    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.add_new_task_btn:
                click_sound.start();
                AddNewTask();
                break;
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void AddNewTask() {
        ModelTask modelTask = new ModelTask();
        modelTask.setTaskName(title_edt.getText().toString());
        modelTask.setTaskBody(body_edt.getText().toString());
        if(isAlerted){
            modelTask.setTaskAlerted("true");
            modelTask.setAlertDate(dayPicked+"/"+monthPicked+"/"+yearPicked + "   " +hourPicked+":"+minutePicked);

            setAlaram(modelTask.getTaskName(), modelTask.getTaskBody());

        }else{
            modelTask.setTaskAlerted("false");
            modelTask.setAlertDate("");
        }
        if(isImportant){
            modelTask.setImportant("true");
        }else{
            modelTask.setImportant("false");
        }

        String key = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth
                        .getInstance()
                        .getCurrentUser().getUid())
                .child(category).push().getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("taskName",modelTask.getTaskName());
        hashMap.put("taskBody",modelTask.getTaskBody());
        hashMap.put("alertDate",modelTask.getAlertDate());
        hashMap.put("taskAlerted",modelTask.getTaskAlerted());
        hashMap.put("important",modelTask.getImportant());
        hashMap.put("deleted","false");
        hashMap.put("category", category);
        hashMap.put("keyId",key);
        hashMap.put("alarm_id",String.valueOf(id));




        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth
                        .getInstance()
                        .getCurrentUser().getUid())
                .child(category).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewTaskSettings.this, R.string.task_added_seccess, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(NewTaskSettings.this, R.string.task_eror, Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setAlaram(String taskName, String taskBody) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date date = null;

        Calendar cal_alarm=Calendar.getInstance();
        id =(int) System.currentTimeMillis();

        cal_alarm.set(Calendar.YEAR,yearPicked);
        cal_alarm.set(Calendar.MONTH,monthPicked-1);
        cal_alarm.set(Calendar.DAY_OF_MONTH,dayPicked);
        cal_alarm.set(Calendar.HOUR_OF_DAY,hourPicked);
        cal_alarm.set(Calendar.MINUTE,minutePicked);
        cal_alarm.set(Calendar.SECOND, 0);
        cal_alarm.set(Calendar.MILLISECOND, 0);



        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal_alarm.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal_alarm.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, taskName)
                .putExtra(CalendarContract.Events.DESCRIPTION, taskBody)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        final StringBuffer strBuf = new StringBuffer();



        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);



    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        final StringBuffer strBuf = new StringBuffer();

        strBuf.append(hourOfDay);
        strBuf.append(":");
        strBuf.append(minute);
        minutePicked=minute;
        hourPicked=hourOfDay;

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Toast.makeText(NewTaskSettings.this, strBuf.toString(), Toast.LENGTH_SHORT).show();
    }
}
