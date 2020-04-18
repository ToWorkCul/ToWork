package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail;
    private EditText textPassword;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private Map<String, Object> usersData;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        //myRef.setValue("Edwin Polo");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                // Get Post object and use the values to update the UI
                Map<String, Object> map = (Map<String, Object>) data.getValue();
                usersData = map;
                Log.w("TAG", "Exitoso: Data: " + map);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "Cancelado", databaseError.toException());
                // TODO: Manejar escenario cuando falle
            }
        };
        myRef.addValueEventListener(postListener);
    }

    public void registrar(View view) {
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

    public void ingresar(View view) {

        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Intenta con 'Edwin' (Admin) o con 'Jorge' (User)", Toast.LENGTH_LONG).show();
            return;
        }

        //TODO: Crear objeto user con constructor para pasarle el user data y armar el usuario
        Object userObject1 = usersData.get("test1");
        Map<String, Object> userData1 = (Map<String, Object>) userObject1;

        Object userObject2 = usersData.get("test2");
        Map<String, Object> userData2 = (Map<String, Object>) userObject2;

        if (userData1.containsValue(email)) {
            Intent intent = new Intent(this, ServicerHomeActivity.class);
            intent.putExtra("userData", userData1.toString());
            startActivity(intent);
            return;
        }

        if (userData2.containsValue(email)) {
            Intent intent = new Intent(this, SeekerHomeActivity.class);
            intent.putExtra("userData", userData2.toString());
            startActivity(intent);
            return;
        }

        Toast.makeText(this, "Intenta con 'Edwin' (Admin) o con 'Jorge' (User)", Toast.LENGTH_LONG).show();
    }


}
