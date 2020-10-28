package com.example.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CheckAct extends AppCompatActivity {
    ImageView ImageCheck;
    File localFile = null;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        ImageCheck = findViewById(R.id.imageView);
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://bluetoothbus-c92e2.appspot.com").child("Чек.png");



        try {
            localFile = File.createTempFile("images", "png");
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImageCheck.setImageBitmap(bitmap);
                            Toast.makeText(CheckAct.this, "Image downloaded", Toast.LENGTH_LONG).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CheckAct.this, "Download failed", Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
    }
}