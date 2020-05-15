package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText textUsuario;
    private EditText textPassword;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;
    private View decorView;

    private FirebaseAuth firebaseAuth;
    private Map<String, Object> usersData;

    User[] users = {};

    ArrayList<User> userList = new ArrayList<User>();

    ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        textUsuario = findViewById(R.id.txtUsuario);
        textPassword = findViewById(R.id.txtPass);

        btnRegistrar = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userExist()) {
            SharedPreferences preferences = this.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
            String role = preferences.getString("role", "");

            if (role.equals("servicer")) {
                Intent intent = new Intent(this, ServicerHomeActivity.class);
                startActivity(intent);
                return;
            }

            if (role.equals("seeker")) {
                Intent intent = new Intent(this, SeekerHomeActivity.class);
                startActivity(intent);
                return;
            }
        } else {
            // Busca Todo los usuarios
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");

            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    usersData = (Map<String, Object>) data.getValue();

                    for (DataSnapshot childDataSnapshot : data.getChildren()) {
                        try {
                            userList.add(new User(childDataSnapshot));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("TAG", "Cancelado", databaseError.toException());
                }
            };
            myRef.addValueEventListener(postListener);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)  {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void registrar(View view) {
        String email = textUsuario.getText().toString().trim();
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

                            Toast.makeText(MainActivity.this,"Se ha registrado el usuario con el email: "+ textUsuario.getText(),Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(MainActivity.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void ingresar(View view) {

        String userName = textUsuario.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Campos invalidos", Toast.LENGTH_LONG).show();
            return;
        }

        if (usersData == null) {
            Toast.makeText(this,"Error de Red",Toast.LENGTH_LONG).show();
            return;
        }

        User[] ts = {};
        users = userList.toArray(ts);

        for (User user: userList) {
            if (userName.equals(user.getName()) && password.equals(user.getPass())) {
                //save user preference
                saveUser(user.getId(), user.getRole());
                if (user.getRole().equals("servicer")) {
                    cleanFields();
                    Intent intent = new Intent(this, ServicerHomeActivity.class);
                    startActivity(intent);
                    return;
                }

                if (user.getRole().equals("seeker")) {
                    cleanFields();
                    Intent intent = new Intent(this, SeekerHomeActivity.class);
                    startActivity(intent);
                    return;
                }

            }
        }

        Toast.makeText(this, "Combinacion invalida", Toast.LENGTH_LONG).show();
    }

   private void saveUser(String id, String role) {
       SharedPreferences preferences = getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = preferences.edit();
       editor.putString("userID",id);
       editor.putString("role",role);
       editor.commit();
   }

   private Boolean userExist() {
       SharedPreferences preferences = this.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
       String userID = preferences.getString("userID", "");
       return TextUtils.isEmpty(userID) == false;
    }

    private void cleanFields(){
        textUsuario.setText("");
        textPassword.setText("");
    }


}


