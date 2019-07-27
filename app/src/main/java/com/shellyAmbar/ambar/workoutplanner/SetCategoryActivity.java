package com.shellyAmbar.ambar.workoutplanner;

import android.content.ContentResolver;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shellyAmbar.ambar.workoutplanner.Models.ModelCategory;

import java.util.HashMap;


public class SetCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView imageAuto,colorAuto;
    private ImageView arrowImage,arrowColor;
    private EditText title_edt;
    private Button add_new_category_btn;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String titleOfCategory;
    private String colorSelected;
    private String imageSelected;
    private Context context;
    private  boolean isCategoryExist = false;


    private HashMap<String,String> realImages;
    private HashMap<String,String> realColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_category);
        title_edt=findViewById(R.id.title_edt);
        arrowColor=findViewById(R.id.Arrow_down_color);
        arrowImage=findViewById(R.id.Arrow_down_image);
        imageAuto=findViewById(R.id.new_category_image);
        colorAuto=findViewById(R.id.new_category_color);
        add_new_category_btn=findViewById(R.id.add_new_category_btn);
        progressBar=findViewById(R.id.progressBar);
        toolbar=findViewById(R.id.toolbar);
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

        context = getApplicationContext();
        final String[] colors = {context.getResources().getString(R.string.red),context.getResources().getString(R.string.pink),context.getResources().getString(R.string.blue),context.getResources().getString(R.string.purple),context.getResources().getString(R.string.yellow),context.getResources().getString(R.string.green),context.getResources().getString(R.string.orange)};
        final String[] images = {context.getResources().getString(R.string.pets),context.getResources().getString(R.string.party),context.getResources().getString(R.string.flights),context.getResources().getString(R.string.holiday),context.getResources().getString(R.string.personal),context.getResources().getString(R.string.kids),context.getResources().getString(R.string.shopping),context.getResources().getString(R.string.market),context.getResources().getString(R.string.car)};
        ArrayAdapter<String> adapterImages=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,images);
        imageAuto.setAdapter(adapterImages);
        ArrayAdapter<String> adapterColors=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,colors);
        colorAuto.setAdapter(adapterColors);

        arrowImage.setOnClickListener(this);
        arrowColor.setOnClickListener(this);
        add_new_category_btn.setOnClickListener(this);
        realColors= new HashMap<>();
        realImages=new HashMap<>();
        realImages.put(context.getResources().getString(R.string.pets),String.valueOf(R.drawable.ic_animal));
        realImages.put(context.getResources().getString(R.string.party),String.valueOf(R.drawable.ic_party));
        realImages.put(context.getResources().getString(R.string.flights),String.valueOf(R.drawable.ic_travels));
        realImages.put(context.getResources().getString(R.string.holiday),String.valueOf(R.drawable.ic_holiday));
        realImages.put(context.getResources().getString(R.string.personal),String.valueOf(R.drawable.ic_person));
        realImages.put(context.getResources().getString(R.string.kids),String.valueOf(R.drawable.ic_kids));
        realImages.put(context.getResources().getString(R.string.shopping),String.valueOf(R.drawable.ic_shopping));
        realImages.put(context.getResources().getString(R.string.market),String.valueOf(R.drawable.ic_market));
        realImages.put(context.getResources().getString(R.string.car),String.valueOf(R.drawable.ic_car));



        realColors.put(context.getResources().getString(R.string.red),String.valueOf(R.drawable.medical_button));
        realColors.put(context.getResources().getString(R.string.pink),String.valueOf(R.drawable.personaly_button));
        realColors.put(context.getResources().getString(R.string.blue),String.valueOf(R.drawable.home_button));
        realColors.put(context.getResources().getString(R.string.purple),String.valueOf(R.drawable.meetings_button));
        realColors.put(context.getResources().getString(R.string.yellow),String.valueOf(R.drawable.yello_button));
        realColors.put(context.getResources().getString(R.string.green),String.valueOf(R.drawable.studies_button));
        realColors.put(context.getResources().getString(R.string.orange),String.valueOf(R.drawable.work_button));

        imageSelected="";
        colorSelected="";
        titleOfCategory="";



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.add_new_category_btn:
                AddNewCategoryToDataBase();
                break;

            case R.id.Arrow_down_image:
                ArrowDownImages();

                break;

            case R.id.Arrow_down_color:
                ArrowDownColors();
                break;
        }

    }

    private void ArrowDownColors() {

        colorAuto.setDropDownHeight(400);
        colorAuto.showDropDown();
        colorAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                colorSelected = colorAuto.getText().toString();
                colorAuto.setHint(colorSelected);
                colorAuto.setHintTextColor(Color.GREEN);
            }
        });
    }

    private void ArrowDownImages() {

        imageAuto.setDropDownHeight(400);
        imageAuto.showDropDown();
        imageAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageSelected = imageAuto.getText().toString();
                imageAuto.setHint(imageSelected);
                imageAuto.setHintTextColor(Color.GREEN);
            }
        });

    }

    private void AddNewCategoryToDataBase() {

        isCategoryExist =false;
        
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Categories");

        final String key = reference.push().getKey();

        titleOfCategory=title_edt.getText().toString();

        

        if(imageSelected.equals("") || colorSelected.equals("")||titleOfCategory.equals(""))
        {
            Toast.makeText(this, R.string.toast_null_category, Toast.LENGTH_SHORT).show();

        }else{
            
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ModelCategory modelCategory = snapshot.getValue(ModelCategory.class);
                        if(modelCategory.getImageTitle().equals(titleOfCategory))
                        {
                            isCategoryExist = true;
                        }
                    }
                    if(titleOfCategory.equals(context.getResources().getString(R.string.home))
                            ||titleOfCategory.equals(context.getResources().getString(R.string.work))
                            ||titleOfCategory.equals(context.getResources().getString(R.string.meetings))
                            ||titleOfCategory.equals(context.getResources().getString(R.string.medical))
                            ||titleOfCategory.equals(context.getResources().getString(R.string.travels_main))
                            ||titleOfCategory.equals(context.getResources().getString(R.string.studies))){
                        isCategoryExist = true;
                    }

                    if(!isCategoryExist){

                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("imageSRC",realImages.get(imageSelected));
                        hashMap.put("imageBack",realColors.get(colorSelected));
                        hashMap.put("imageTitle",titleOfCategory);
                        hashMap.put("keyId",key);


                        reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(SetCategoryActivity.this, R.string.category_toast_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }
                        });

                    }else{

                        Toast.makeText(context, R.string.category_exist, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            


            
        }





    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
