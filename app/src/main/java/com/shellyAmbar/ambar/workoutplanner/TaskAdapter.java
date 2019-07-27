package com.shellyAmbar.ambar.workoutplanner;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shellyAmbar.ambar.workoutplanner.Models.ModelTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<ModelTask> taskArrayList;
    private Context context;
    private static boolean starChecked;
    private static boolean isChecked=false;


    public TaskAdapter(List<ModelTask> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_new_task,viewGroup,false);
        return new TaskAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final ModelTask modelTask = taskArrayList.get(i);


        final MediaPlayer check_sound = MediaPlayer.create(context,R.raw.click_check);

        final MediaPlayer star_sound = MediaPlayer.create(context,R.raw.clicking);



        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(modelTask.getDeleted().equals("true")){


            isChecked=true;
            viewHolder.checkBox.setChecked(true);
            viewHolder.taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.taskName.setTextColor(Color.RED);
            viewHolder.taskBody.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.taskBody.setTextColor(Color.RED);

        }else{

            isChecked=false;
            viewHolder.checkBox.setChecked(false);
            viewHolder.taskName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            viewHolder.taskName.setTextColor(context.getResources().getColor(R.color.color_turkiz));
            viewHolder.taskBody.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            viewHolder.taskBody.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        if(modelTask.getImportant().equals("true")){

            starChecked=true;
            viewHolder.starButton.setBackground(context.getDrawable(R.drawable.ic_star_black_24dp));
        }else{

            starChecked=false;
            viewHolder.starButton.setBackground(context.getDrawable(R.drawable.ic_star_border_black_24dp));


        }



        viewHolder.taskName.setText(modelTask.getTaskName());
        if(modelTask.getTaskAlerted().equals("true"))
        {
            viewHolder.taskBody.setText(modelTask.getTaskBody() +" "+ modelTask.getAlertDate());
        }else{
            viewHolder.taskBody.setText(modelTask.getTaskBody());
        }




        viewHolder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starChecked)
                {

                    modelTask.setImportant("false");
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("important", "false");
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child(modelTask.getCategory()).child(modelTask.getKeyId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            starChecked=false;
                            viewHolder.starButton.setBackground(context.getDrawable(R.drawable.ic_star_border_black_24dp));
                        }
                    });

                }else{


                   // star_sound.start();
                    modelTask.setImportant("true");
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("important","true");
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child(modelTask.getCategory()).child(modelTask.getKeyId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            starChecked=true;
                            viewHolder.starButton.setBackground(context.getDrawable(R.drawable.ic_star_black_24dp));
                        }
                    });
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!viewHolder.checkBox.isChecked())
                {

                    isChecked=false;
                   // viewHolder.checkBox.setChecked(false);
                    modelTask.setDeleted("false");
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("deleted", "false");
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child(modelTask.getCategory()).child(modelTask.getKeyId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            viewHolder.taskName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                            viewHolder.taskName.setTextColor(context.getResources().getColor(R.color.color_turkiz));
                            viewHolder.taskBody.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                            viewHolder.taskBody.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        }
                    });

                }
                else if(viewHolder.checkBox.isChecked()) {


                    //viewHolder.checkBox.setChecked(true);
                    //check_sound.start();

                    isChecked=true;
                    modelTask.setDeleted("true");
                  //  viewHolder.checkBox.setChecked(true);
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("deleted", "true");
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child(modelTask.getCategory()).child(modelTask.getKeyId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {



                            viewHolder.taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            viewHolder.taskName.setTextColor(Color.RED);
                            viewHolder.taskBody.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            viewHolder.taskBody.setTextColor(Color.RED);
                            Intent intent = new Intent(context, MyAlaram.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(modelTask.getAlarm_id()), intent, 0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);


                        }
                    });



                }
            }
        });

        viewHolder.shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                star_sound.start();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT,modelTask.getTaskBody());
                intent.putExtra(Intent.EXTRA_SUBJECT,modelTask.getTaskName());
                context.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final android.app.AlertDialog.Builder alertDialog=
                        new android.app.AlertDialog.Builder(context,R.style.AlertDialogCustom);
                final TextView titleTEXT = new TextView(context);
                titleTEXT.setText(R.string.task_sure);
                titleTEXT.setGravity(Gravity.CENTER);
                titleTEXT.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                titleTEXT.setTextSize(30);

                LinearLayout linearLayout=new LinearLayout(context);




                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(lllp);
                linearLayout.addView(titleTEXT,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                                ,ViewGroup.LayoutParams.MATCH_PARENT,0));
                alertDialog.setView(linearLayout);
                alertDialog.setNeutralButton(R.string.task_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton(R.string.task_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                .child(modelTask.getCategory()).child(modelTask.getKeyId())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, R.string.task_success, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });






                alertDialog.show();




                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView taskName,taskBody;
        private CheckBox checkBox;
        private Button starButton;
        private ImageView shareBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName=itemView.findViewById(R.id.taskName);
            taskBody=itemView.findViewById(R.id.taskBody);
            checkBox=itemView.findViewById(R.id.checkBox);
            starButton=itemView.findViewById(R.id.starButton);
            shareBTN=itemView.findViewById(R.id.share);
        }
    }
}
