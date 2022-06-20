package com.example.getthingsdonewithadwin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.getthingsdonewithadwin.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CreateTask extends AppCompatActivity {
    EditText addTaskTitle, taskDate, taskTime, addTaskDescription;
    Button addTask;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    CollectionReference taskRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        addTaskTitle= findViewById(R.id.addTaskTitle);
        addTaskDescription= findViewById(R.id.addTaskDescription);
        taskDate= findViewById(R.id.taskDate);
        taskTime= findViewById(R.id.taskTime);
        addTask= findViewById(R.id.addTask);

        //Disable keyboard
        taskDate.setInputType(InputType.TYPE_NULL);
        taskTime.setInputType(InputType.TYPE_NULL);

        //Date Picker
        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(taskDate);
            }
        });

        //Time Picker
        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(taskTime);
            }
        });

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        userId= fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        taskRef= fStore.collection("users");

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName= addTaskTitle.getText().toString().trim();
                String taskDescr= addTaskDescription.getText().toString().trim();
                String date= taskDate.getText().toString().trim();
                String time= taskTime.getText().toString().trim();
                Map<String, Object> task =new HashMap<>();
                task.put("taskTitle", taskName);
                task.put("taskDesc", taskDescr);
                task.put("taskDate", date);
                task.put("taskTime", time);
                taskRef.document(userId).collection("tasks").add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateTask.this, "Task Added successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Task added successfully for user:"+ userId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateTask.this, "Failed to add task", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed"+ e.toString());
                    }
                });

                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
            }
        });


    }

    private void showDateDialog(EditText taskDate){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yy-MM-dd");
                taskDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(CreateTask.this, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(EditText taskTime) {
        Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                taskTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(CreateTask.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
}