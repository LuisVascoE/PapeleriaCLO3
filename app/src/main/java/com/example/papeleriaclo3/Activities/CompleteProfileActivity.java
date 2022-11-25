package com.example.papeleriaclo3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.papeleriaclo3.R;
import com.example.papeleriaclo3.models.User;
import com.example.papeleriaclo3.providers.AuthProviders;
import com.example.papeleriaclo3.providers.UsersProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {
    TextInputEditText mTextInputUsername;
    Button mButtonRegister;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFirestore;
    UsersProviders mUsersproviders;
    AuthProviders mAuthProviders;
    AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextInputUsername=findViewById(R.id.textInputUsernameC);
        mButtonRegister=findViewById(R.id.btnregisterC);

        mAuthProviders=new AuthProviders();
        mUsersproviders=new UsersProviders();

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String username=mTextInputUsername.getText().toString();

        if (!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "Inserte nombre de usuario", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUser(final String username) {
        String id=mAuthProviders.getUid();
        User user=new User();
        user.setUsername(username);
        user.setId(id);
        mDialog.show();

        mUsersproviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent=new Intent(CompleteProfileActivity.this, HomeActivity2.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(CompleteProfileActivity.this, "Usuario no almacenado en base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}