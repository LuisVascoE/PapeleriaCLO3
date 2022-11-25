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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity2 extends AppCompatActivity {

    CircleImageView mCircleImageViewback;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    Button mButtonRegister;
    //FirebaseAuth mAut;
    //FirebaseFirestore mFirestore;
    AuthProviders mAuthProvider;
    UsersProviders mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        //INSTANCIAS
        mCircleImageViewback= findViewById(R.id.cirleimageback);
        mTextInputEmail=findViewById(R.id.textInputEmail);
        mTextInputUsername=findViewById(R.id.textInputUsername);
        mTextInputPassword=findViewById(R.id.textInputPassword);
        mTextInputConfirmPassword=findViewById(R.id.textInputConfirPassword);


        mAuthProvider=new AuthProviders();
        mUsersProvider=new UsersProviders();

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();


        mButtonRegister=findViewById(R.id.btnregister);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }


        });
        mCircleImageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void register() {
        String username=mTextInputUsername.getText().toString();
        String email=mTextInputEmail.getText().toString();
        String password=mTextInputPassword.getText().toString();
        String confirmpassword=mTextInputConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if (isEmailValid(email)){

                if (password.equals(confirmpassword)){
                    if (password.length()>=6){
                        createUser(username,email,password);
                    }else{
                        Toast.makeText(this, "Las contraseñas deben tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Datos Correctos", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "correo no valido",
                        Toast.LENGTH_SHORT).show();
            }


        }
        else {
            Toast.makeText(this, "Insertar todos los campos",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private void createUser( final String username, final String email, String password) {
        mDialog.show();
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id=mAuthProvider.getUid();
                    User user=new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity2.this, "Usuario almacenado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity2.this, HomeActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity2.this, "Usuario no se almaceno", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    Toast.makeText(RegisterActivity2.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                }else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity2.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}


