package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail;
    private EditText textPassword;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        textEmail = findViewById(R.id.txtEmail);
        textPassword = findViewById(R.id.txtPass);

        btnRegistrar = findViewById(R.id.btnSignUp);

        progressDialog = new ProgressDialog(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("TAG", "1Value is: ");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user/name");

        myRef.setValue("Hello, World!");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String post = dataSnapshot.getValue(String.class);
                Log.w("TAG", "ssss" + post);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    public void registrar(View view) {
        registrarUsuario();
    }

    private void registrarUsuario(){

        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "Ingresar Email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingresar Contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this,"Se ha registrado el usuario con el email: "+ textEmail.getText(),Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(MainActivity.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }


}
