package com.shellyAmbar.ambar.workoutplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shellyAmbar.ambar.workoutplanner.Models.ModelCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<ModelCategory> modelCategoryList;
    private Context context;

    public CategoriesAdapter(List<ModelCategory> modelCategoryList, Context context) {
        this.modelCategoryList = modelCategoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_category,viewGroup,false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final ModelCategory modelCategory =modelCategoryList.get(i);

        final MediaPlayer click_sound = MediaPlayer.create(context,R.raw.click);

        viewHolder.title.setText(modelCategory.getImageTitle());

        //Glide.with(context).load(modelCategory.getImageSRC()).into(viewHolder.circleImageView);

        viewHolder.circleImageView.setBackground(ContextCompat.getDrawable(context,Integer.parseInt(modelCategory.getImageBack())));
        viewHolder.circleImageView.setImageResource(Integer.parseInt(modelCategory.getImageSRC()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click_sound.start();
                Intent intent = new Intent(context, NewTaskActivity.class);
                intent.putExtra("category",modelCategory.getImageTitle());
                context.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(modelCategory.getKeyId().equals("0")|| modelCategory.getKeyId().equals("1")|| modelCategory.getKeyId().equals("2")||
                modelCategory.getKeyId().equals("3")|| modelCategory.getKeyId().equals("4") || modelCategory.getKeyId().equals("5") )
                {
                    Toast.makeText(context, R.string.defult_categories_toast, Toast.LENGTH_SHORT).show();

                }else{
                    final android.app.AlertDialog.Builder alertDialog=
                            new android.app.AlertDialog.Builder(context,R.style.AlertDialogCustom);



                    final TextView titleTEXT = new TextView(context);
                    titleTEXT.setText(R.string.category_sure);
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

                            click_sound.start();
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setPositiveButton(R.string.task_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            click_sound.start();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Categories");
                            reference.child(modelCategory.getKeyId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(modelCategory.getImageTitle());
                                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, R.string.toast_category_removed, Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });
                        }
                    });



                    alertDialog.show();




                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.category_image);
            title=itemView.findViewById(R.id.category_title);
        }
    }
}
