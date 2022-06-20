package com.example.getthingsdonewithadwin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {
    TextView addTask;
    RecyclerView taskRecycler;
    ImageView logout;
    ArrayList<TaskModel> taskArrayList;
    TaskAdapter taskAdapter;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        fAuth= FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userId= fAuth.getCurrentUser().getUid();

         progressDialog= new ProgressDialog(this);
         progressDialog.setCancelable(false);
         progressDialog.setMessage("Fetching data");
         progressDialog.show();

        taskRecycler=findViewById(R.id.taskRecycler);

        taskRecycler.setHasFixedSize(true);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));

        addTask= findViewById(R.id.addTask);
        logout= findViewById(R.id.logout);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CreateTask.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        db= FirebaseFirestore.getInstance();
        //check this
        taskArrayList= new ArrayList<TaskModel>();
        taskAdapter= new TaskAdapter(TasksActivity.this, taskArrayList);
        taskRecycler.setAdapter(taskAdapter);
           //EventChangeListener();
        ShowTasks();


    }

    private void ShowTasks() {
        db.collection("users").document(userId).collection("tasks").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                                progressDialog.dismiss();
                            List<DocumentSnapshot> list= queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d: list){
                                TaskModel taskModel= d.toObject(TaskModel.class);
                                taskArrayList.add(taskModel);
                            }
                            taskAdapter.notifyDataSetChanged();
                        }else{
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(TasksActivity.this, "You have no tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TasksActivity.this, "Failed to retrieve tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void EventChangeListener() {
//        db.collection("tasks")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(error!= null){
//
//                              if(progressDialog.isShowing())
//                                  progressDialog.dismiss();
//                            Log.e("Firestore error", error.getMessage());
//                            return;
//                        }
//                        for(DocumentChange dc: value.getDocumentChanges()){
//                            if(dc.getType()== DocumentChange.Type.ADDED){
//                                taskArrayList.add(dc.getDocument().toObject(TaskModel.class));
//                            }
//
//                            taskAdapter.notifyDataSetChanged();
//                              if(progressDialog.isShowing())
//                                  progressDialog.dismiss();
//                        }
//
//                    }
//                });
//    }

}