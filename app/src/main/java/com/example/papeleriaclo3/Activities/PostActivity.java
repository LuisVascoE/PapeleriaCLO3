package com.example.papeleriaclo3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.papeleriaclo3.R;
import com.example.papeleriaclo3.providers.ImageProviders;
import com.example.papeleriaclo3.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity {
    ImageView mImageViewPost1;
    File mImageFile;
    private final int Gallery_REQUEST_CODE = 1;
    Button mButtonPost;
    ImageProviders mImageProvider;
    TextInputEditText mtextInputTitle;
    TextInputEditText mtextInputDescription;
    ImageView mImageViewFiccion;
    ImageView mImageViewAventuras;
    ImageView mImageViewParanormal;
    ImageView mImageViewFantasia;
    TextView mTextViewCategory;
    String mCategory="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mtextInputTitle=findViewById(R.id.textInputLibro);
        mtextInputDescription=findViewById(R.id.textInputDescription);
        mImageViewFiccion=findViewById(R.id.imageViewFiccion);
        mImageViewAventuras=findViewById(R.id.imageViewAventuras);
        mImageViewParanormal=findViewById(R.id.imageViewParanormal);
        mImageViewFantasia=findViewById(R.id.imageViewFantasia);
        mTextViewCategory=findViewById(R.id.TextViewCategory);
        mButtonPost=findViewById(R.id.btnPost);



        mImageProvider=new ImageProviders();


        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SaveImage(); antes
                ClickPost();
            }
        });

        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });



        mImageViewFiccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Ficción";
            }
        });


        mImageViewFantasia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Fantasia";
            }
        });


        mImageViewParanormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Paranormal";
            }
        });

        mImageViewAventuras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Aventuras";
            }
        });
    }

    private void ClickPost() {
        String Title=mtextInputTitle.getText().toString();
        String Description=mtextInputDescription.getText().toString();


    }

    private void SaveImage() {
        mImageProvider.save(PostActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PostActivity.this, "Imagen almacenada con exito", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PostActivity.this, "Error al almacenar imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* VALIDACION DE IMAGEN CON GALERIA */
        if (requestCode == Gallery_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

}