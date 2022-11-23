package com.example.papeleriaclo3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity2 extends AppCompatActivity {

    CircleImageView mCircleImageViewback;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mCircleImageViewback= findViewById(R.id.cirleimageback);
        mTextInputEmail=findViewById(R.id.textInputEmail);
        mTextInputUsername=findViewById(R.id.textInputUsername);
        mTextInputPassword=findViewById(R.id.textInputPassword);
        mTextInputConfirmPassword=findViewById(R.id.textInputConfirPassword);

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
                        createUser(email,password);
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

    private void createUser(String email, String password) {
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}


