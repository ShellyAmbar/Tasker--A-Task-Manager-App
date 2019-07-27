package com.shellyAmbar.ambar.workoutplanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.support.v7.widget.Toolbar;

import com.shellyAmbar.ambar.workoutplanner.Models.ModelTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {
    private FloatingActionButton addBTN;
    private RecyclerView recyclerView;
    private List<ModelTask> taskArrayList;
    private TaskAdapter taskAdapter;
    private Toolbar toolbar;
    private String category;
    private MediaPlayer click_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        toolbar=findViewById(R.id.toolbarTask);
        setSupportActionBar(toolbar);
        toolbar.setTitle("     ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Intent intent = new Intent(NewTaskActivity.this,MainActivity.class);
               //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              // startActivity(intent);
               // moveTaskToBack(false);
                finish();
            }
        });

        click_sound = MediaPlayer.create(NewTaskActivity.this,R.raw.click);
        addBTN=findViewById(R.id.add_btn);
        recyclerView=findViewById(R.id.tasksRecycler);
        recyclerView.setHasFixedSize(true);


        taskArrayList=new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        taskAdapter = new TaskAdapter(taskArrayList, NewTaskActivity.this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(manager);
        category=getIntent().getStringExtra("category");


        addBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(NewTaskActivity.this,NewTaskSettings.class);
                intent.putExtra("category",category);
                startActivity(intent);

            }
        });

       // SetAllTasks();
       setModelsInListFromDatabase();


    }

    private void SetAllTasks(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(category);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ModelTask modelTask=snapshot.getValue(ModelTask.class);
                    taskArrayList.add(modelTask);



                }

                Collections.reverse(taskArrayList);
                recyclerView.scrollToPosition(0);
                taskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setModelsInListFromDatabase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(category);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(category);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        taskArrayList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            ModelTask modelTask = snapshot.getValue(ModelTask.class);
                            taskArrayList.add(modelTask);
                        }
                        Collections.reverse(taskArrayList);
                        recyclerView.scrollToPosition(0);
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(category);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        taskArrayList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            ModelTask modelTask = snapshot.getValue(ModelTask.class);
                            taskArrayList.add(modelTask);
                        }
                        Collections.reverse(taskArrayList);
                        recyclerView.scrollToPosition(0);
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(NewTaskActivity.this,MainActivity.class);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      //  startActivity(intent);
        finish();
    }
}
