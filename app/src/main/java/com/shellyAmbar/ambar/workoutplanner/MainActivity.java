package com.shellyAmbar.ambar.workoutplanner;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideOption;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.shellyAmbar.ambar.workoutplanner.Models.ModelCategory;
import com.shellyAmbar.ambar.workoutplanner.Models.ModelTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MediaPlayer click_sound;
    private TextView headline;
    private CategoriesAdapter categoriesAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private List<ModelCategory> modelCategoryList;
    private Context context;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("     ");
        click_sound = MediaPlayer.create(MainActivity.this,R.raw.click);
        progressBar = findViewById(R.id.progressBar);
        headline=findViewById(R.id.headlineText);
        floatingActionButton = findViewById(R.id.add_category_btn);
        recyclerView=findViewById(R.id.recycler);
        modelCategoryList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(modelCategoryList,MainActivity.this) ;
        GridLayoutManager layoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setHasFixedSize(true);
        context=getApplicationContext();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click_sound.start();
                Intent intent = new Intent(MainActivity.this, SetCategoryActivity.class);
                startActivity(intent);
            }
        });




        YoYo.with(Techniques.RotateIn)
                .duration(600)
                .playOn(headline);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                click_sound.start();

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                click_sound.start();

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // PutBasicCategoriesInList();
        PutAllCategoriesInRecycle();
        CheckAndSetAlertRing();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


           if (id == R.id.nav_manage) {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            finish();


        } else if (id == R.id.nav_share) {

            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.tasket_prompt));
            intent.setType("text/*");
            startActivity(intent);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


        }

        return true;
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if(report.areAllPermissionsGranted()){
                            CropImage.activity()
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(MainActivity.this);
                        }else{
                            Toast.makeText(MainActivity.this, context.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();
    }

    @Override
@GlideOption

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(result!=null)
            {
                progressBar.setVisibility(View.GONE);
                saveImageToLocalStorage(result.getBitmap());


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }else{

            }


        }else{
            Toast.makeText(this, context.getResources().getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToLocalStorage(Bitmap bitmap) {

    }

    private void PutBasicCategoriesInList(){
        modelCategoryList.clear();

        ModelCategory one = new ModelCategory(String.valueOf(R.drawable.ic_home),String.valueOf(R.drawable.home_button),context.getResources().getString(R.string.home),"0");
        ModelCategory two = new ModelCategory(String.valueOf(R.drawable.ic_work),String.valueOf(R.drawable.work_button),context.getResources().getString(R.string.work),"1");
        ModelCategory three = new ModelCategory(String.valueOf(R.drawable.ic_meetings),String.valueOf(R.drawable.meetings_button),context.getResources().getString(R.string.meetings),"2");
        ModelCategory four = new ModelCategory(String.valueOf(R.drawable.ic_medical),String.valueOf(R.drawable.medical_button),context.getResources().getString(R.string.medical),"3");
        ModelCategory five = new ModelCategory(String.valueOf(R.drawable.ic_travels),String.valueOf(R.drawable.personaly_button),context.getResources().getString(R.string.travels_main),"4");
        ModelCategory six = new ModelCategory(String.valueOf(R.drawable.ic_studies),String.valueOf(R.drawable.studies_button),context.getResources().getString(R.string.studies),"5");

        modelCategoryList.add(one);
        modelCategoryList.add(two);
        modelCategoryList.add(three);
        modelCategoryList.add(four);
        modelCategoryList.add(five);
        modelCategoryList.add(six);


        categoriesAdapter.notifyDataSetChanged();

    }

   private void PutAllCategoriesInRecycle(){

       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
               .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
               .child("Categories");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               modelCategoryList.clear();
               ModelCategory one = new ModelCategory(String.valueOf(R.drawable.ic_home),String.valueOf(R.drawable.home_button),context.getResources().getString(R.string.home),"0");
               ModelCategory two = new ModelCategory(String.valueOf(R.drawable.ic_work),String.valueOf(R.drawable.work_button),context.getResources().getString(R.string.work),"1");
               ModelCategory three = new ModelCategory(String.valueOf(R.drawable.ic_meetings),String.valueOf(R.drawable.meetings_button),context.getResources().getString(R.string.meetings),"2");
               ModelCategory four = new ModelCategory(String.valueOf(R.drawable.ic_medical),String.valueOf(R.drawable.medical_button),context.getResources().getString(R.string.medical),"3");
               ModelCategory five = new ModelCategory(String.valueOf(R.drawable.ic_travel),String.valueOf(R.drawable.personaly_button),context.getResources().getString(R.string.travels_main),"4");
               ModelCategory six = new ModelCategory(String.valueOf(R.drawable.ic_studies),String.valueOf(R.drawable.studies_button),context.getResources().getString(R.string.studies),"5");

               modelCategoryList.add(one);
               modelCategoryList.add(two);
               modelCategoryList.add(three);
               modelCategoryList.add(four);
               modelCategoryList.add(five);
               modelCategoryList.add(six);


               for(DataSnapshot snapshot : dataSnapshot.getChildren())
               {
                   ModelCategory modelCategory = snapshot.getValue(ModelCategory.class);
                   modelCategoryList.add(modelCategory);
               }

               categoriesAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }




    private void CheckAndSetAlertRing(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Categories");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> titles = new ArrayList<>();
                titles.add(context.getResources().getString(R.string.home));
                titles.add(context.getResources().getString(R.string.work));
                titles.add(context.getResources().getString(R.string.meetings));
                titles.add(context.getResources().getString(R.string.medical));
                titles.add(context.getResources().getString(R.string.travels_main));
                titles.add(context.getResources().getString(R.string.studies));
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    titles.add(snapshot.getValue(ModelCategory.class).getImageTitle());
                }
                for(String title : titles)
                {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(title);
                    HandelReferenceAlerting(reference1);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }

    private void HandelReferenceAlerting(DatabaseReference reference){



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  alertDate=null;
                String isDeleted = "";
                DateFormat df= new SimpleDateFormat("dd/M/yyyy");
                String date=df.format(Calendar.getInstance().getTime());;

               for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   final ModelTask modelTask = snapshot.getValue(ModelTask.class);
                   alertDate= modelTask.getAlertDate();
                   isDeleted = modelTask.getDeleted();
                   if(alertDate.equals(date) && isDeleted.equals("false"))
                   {
                       Intent intent = new Intent(MainActivity.this, AlertActivity.class);
                       intent.putExtra("taskName",modelTask.getTaskName());
                       intent.putExtra("taskBody",modelTask.getTaskBody());
                       intent.putExtra("date",modelTask.getAlertDate());
                       startActivity(intent);

                   }
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
