package com.example.papeleriaclo3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.papeleriaclo3.R;
import com.example.papeleriaclo3.models.Post;
import com.example.papeleriaclo3.providers.AuthProviders;
import com.example.papeleriaclo3.providers.ImageProviders;
import com.example.papeleriaclo3.providers.PostProvider;
import com.example.papeleriaclo3.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {
    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File getmImageFile2;
    private final int Gallery_REQUEST_CODE = 1;
    private final int Gallery_REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;
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
    PostProvider mPostProvider;
    String mTitle="";
    String mDescription="";
    AuthProviders mAuthProviders;
    AlertDialog mDialog;
    CircleImageView mCircleImageView;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];


    String mAbsolutePhotePathc;
    String mPhotoPathc;
    File mPhotoFile;


    String mAbsolutePhotePathc2;
    String mPhotoPathc2;
    File mPhotoFile2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2=findViewById(R.id.imageViewPost2);
        mtextInputTitle=findViewById(R.id.textInputLibro);
        mtextInputDescription=findViewById(R.id.textInputDescription);
        mImageViewFiccion=findViewById(R.id.imageViewFiccion);
        mImageViewAventuras=findViewById(R.id.imageViewAventuras);
        mImageViewParanormal=findViewById(R.id.imageViewParanormal);
        mImageViewFantasia=findViewById(R.id.imageViewFantasia);
        mTextViewCategory=findViewById(R.id.TextViewCategory);
        mButtonPost=findViewById(R.id.btnPost);
        mCircleImageView=findViewById(R.id.cirleimageback2);



        mImageProvider=new ImageProviders();
        mPostProvider=new PostProvider();
        mAuthProviders=new AuthProviders();

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        mBuilderSelector=new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Seleccione una opción");
        options=new CharSequence[]{"Imagen de Galeria", "Tomar Foto"};




        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostActivity.this,HomeActivity2.class);
                startActivity(intent);
            }
        });




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
                selectOptionImage(1);
                //openGallery(Gallery_REQUEST_CODE);
            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
                //openGallery(Gallery_REQUEST_CODE_2);
            }
        });



        mImageViewFiccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Ficción";
                mTextViewCategory.setText(mCategory);
            }
        });


        mImageViewFantasia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Fantasia";
                mTextViewCategory.setText(mCategory);
            }
        });


        mImageViewParanormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Paranormal";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewAventuras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory="Aventuras";
                mTextViewCategory.setText(mCategory);
            }
        });
    }

    private void selectOptionImage(int numberImage) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which ==0){
                    if (numberImage==1){
                        openGallery(Gallery_REQUEST_CODE);
                    }else if (numberImage==2){
                        openGallery(Gallery_REQUEST_CODE_2);
                    }

                }else if (which==1){
                    if (numberImage==1){
                        TakePhoto(PHOTO_REQUEST_CODE);
                    }else if (numberImage==2){
                        TakePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });
        mBuilderSelector.show();
    }

    private void TakePhoto( int requestCode) {
        //Toast.makeText(this, "Selecciono tomar foto", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try {
                photoFile=createPhotoFile(requestCode);

            }catch (Exception e){
                Toast.makeText(this, "Error de archivo"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (photoFile !=null){
                Uri photoUri= FileProvider.getUriForFile(PostActivity.this,"com.example.papeleriaclo3",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile=File.createTempFile(
                new Date()+"_photo",
                ".jpg",
                storageDir


       );
        if (requestCode==PHOTO_REQUEST_CODE){
            mPhotoPathc="file:"+photoFile.getAbsolutePath();
            mAbsolutePhotePathc=photoFile.getAbsolutePath();
        }else if (requestCode==PHOTO_REQUEST_CODE_2){
            mPhotoPathc2="file:"+photoFile.getAbsolutePath();
            mAbsolutePhotePathc2=photoFile.getAbsolutePath();
        }

        return photoFile;
    }

    private void ClickPost() {
         mTitle=mtextInputTitle.getText().toString();
         mDescription=mtextInputDescription.getText().toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
                //MABAS IMAGENES DE GALLERY
            if (mImageFile !=null && getmImageFile2 !=null){
                SaveImage(mImageFile, getmImageFile2);

                //TOMO LAS DOS FOTOS DE CAMARA
            }else if (mPhotoFile !=null && mPhotoFile2 !=null){
                SaveImage(mPhotoFile, mPhotoFile2);

            }  //UNA IMAGEN DE GALLERIA Y OTRA DE FOTOGRAFIA
            else if (mImageFile !=null && mPhotoFile2 !=null){
                SaveImage(mImageFile, mPhotoFile2);
            }
              //UNA FOTOGRAFIA Y OTRA DE GALERIA
            else if (mPhotoFile !=null && getmImageFile2 !=null){
                SaveImage(mPhotoFile, getmImageFile2);
            }




            else{
                Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Complete campos para publicar", Toast.LENGTH_SHORT).show();
        }


    }



    private void SaveImage(File mImageFile, File getmImageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url=uri.toString();
                            mImageProvider.save(PostActivity.this,getmImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2=uri2.toString();
                                                Post post=new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProviders.getUid());

                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> tasksave) {
                                                        mDialog.dismiss();
                                                        if (tasksave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "Información almacenada correctamente", Toast.LENGTH_SHORT).show();

                                                        }else{
                                                            Toast.makeText(PostActivity.this, "No se almaceno la información", Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });
                                            }
                                        });

                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Error al almacenar imagen 2", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                    Toast.makeText(PostActivity.this, "Imagen almacenada con exito", Toast.LENGTH_SHORT).show();
                }else {
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Error al almacenar imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void clearForm() {
        mtextInputTitle.setText("");
        mtextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.ic_subir_foto);
        mImageViewPost2.setImageResource(R.drawable.ic_subir_foto);
        mTitle="";
        mDescription="";
        mCategory="";
        mImageViewPost1=null;
        mImageViewPost2=null;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* VALIDACION DE IMAGEN CON GALERIA */
        if (requestCode == Gallery_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mPhotoFile=null;
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Gallery_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            try {
                mPhotoFile2=null;
                getmImageFile2 = FileUtil.from(this, data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(getmImageFile2.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode==PHOTO_REQUEST_CODE && resultCode==RESULT_OK){
            mImageFile=null;
            mPhotoFile=new File(mAbsolutePhotePathc);
            Picasso.with(PostActivity.this).load(mPhotoPathc).into(mImageViewPost1);

        }
        if (requestCode==PHOTO_REQUEST_CODE_2 && resultCode==RESULT_OK){
            getmImageFile2=null;
            mPhotoFile2=new File(mAbsolutePhotePathc2);
            Picasso.with(PostActivity.this).load(mPhotoPathc2).into(mImageViewPost2);

        }

    }


}