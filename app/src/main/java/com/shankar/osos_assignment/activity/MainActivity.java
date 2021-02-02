package com.shankar.osos_assignment.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jaiky.imagespickers.ImageConfig;
import com.shankar.customtoast.Toasty;
import com.shankar.osos_assignment.R;
import com.shankar.osos_assignment.adapter.ImageInterface;
import com.shankar.osos_assignment.adapter.ParentAdapter;
import com.shankar.osos_assignment.model.ParentClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ImageInterface {

    public final int PICK_IMAGE = 1;
    final int REQUEST_EXTERNAL_STORAGE = 100;

    int globalPosition;

    FloatingActionButton floatingButton;
    RecyclerView parentRecyclerView;
    List<ParentClass> parentClassList;
    AlertDialog alertDialog;
    ParentAdapter parentAdapter;




    private ImageConfig imageConfig;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        parentClassList = new ArrayList<>();

        // Initializing parent RecyclerView, Linear layout manager, parent adapter
        parentRecyclerView = findViewById(R.id.parent_recycler);
        parentRecyclerView.setNestedScrollingEnabled(false);
        parentRecyclerView.setHasFixedSize(true);
        
        LinearLayoutManager latestLinearLayout = new GridLayoutManager(getApplicationContext(),1);
        parentRecyclerView.setLayoutManager(latestLinearLayout);
        
        parentAdapter = new ParentAdapter(this, parentClassList);
        parentRecyclerView.setAdapter(parentAdapter);

        floatingButton = findViewById(R.id.add_button);
        floatingButton.setOnClickListener(v -> {


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            //adding layout file to Alert Dialog
            View dialogView = inflater.inflate(R.layout.layout_alert_dialog, null);
            dialogBuilder.setView(dialogView);

            alertDialog = dialogBuilder.create();

            //Initializing Alert Dialog components
            TextInputEditText titleET = dialogView.findViewById(R.id.titleET);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            Button okayButton = dialogView.findViewById(R.id.okayButton);
            cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
            okayButton.setOnClickListener(v1 -> {

                String titleST = Objects.requireNonNull(titleET.getText()).toString();
                if(titleST.isEmpty())
                {
                    titleET.setError("Empty");
                    titleET.requestFocus();
                }
                else
                {
                    //Passing Title into adapter
                    parentClassList.add(new ParentClass(titleST));
                    alertDialog.dismiss();
                    parentAdapter.notifyDataSetChanged();
                }

            });
            alertDialog.show();

        });

    }

    //I had to implement Interface method because ParentAdapter didn't accept onActivityResult
    @Override
    public void onImageClick(int position) {

        globalPosition = position;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);

        } else {

//            if(isPackageInstalled())
//            {
//                //Opens google photos
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                //intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setPackage("com.google.android.apps.photos");
//                startActivityForResult(Intent.createChooser(intent, "Pictures: "), PICK_IMAGE);
//            }
//            else
//            {
//                //Opens file manager
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(Intent.createChooser(intent, "Pictures: "), PICK_IMAGE);
//
//            }
            ImagePicker.create(this) // Activity or Fragment
                    .limit(10)
                    .start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Toasty.infoToast(this,""+data.getClipData().getItemAt(1).getUri());

                    for (int i = 0; i < count; i++) {
//                        uri.add(data.getClipData().getItemAt(i).getUri());
                        parentClassList.get(globalPosition).addImage(data.getClipData().getItemAt(i).getUri());
                    }
                   parentAdapter.notifyDataSetChanged();

                }
            }
        }

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<com.esafirm.imagepicker.model.Image> images = ImagePicker.getImages(data);

            for (int i = 0; i < images.size(); i++) {
                parentClassList.get(globalPosition).addImage(images.get(i).getUri());
            }
            parentAdapter.notifyDataSetChanged();
        }

    }

    private boolean isPackageInstalled() {
        try {
            PackageManager packageManager = this.getPackageManager();
            packageManager.getPackageInfo("com.google.android.apps.photos", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }




}