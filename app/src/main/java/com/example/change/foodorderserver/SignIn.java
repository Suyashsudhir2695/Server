package com.example.change.foodorderserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.change.foodorderserver.Common.Common;
import com.example.change.foodorderserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText editPhone;
    EditText editPass;
    Button btnSignIn;
    FirebaseDatabase db;
    DatabaseReference users;
    private static final String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Sign In");

        editPhone = findViewById(R.id.editPhone);
        editPass = findViewById(R.id.editPass);
        btnSignIn = findViewById(R.id.btnLoginS);


        db = FirebaseDatabase.getInstance();
        users = db.getReference("user");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString();
                String pass = editPass.getText().toString();
                signIn(phone,pass);
            }
        });


    }

    private void signIn(String phone, String pass) {
        final ProgressDialog pd = new ProgressDialog(SignIn.this);
        pd.setMessage("Hang On...");
        pd.show();

        final String localPhone = phone;
        final String localPass = pass;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(localPhone.replace(".","_")).exists()){
                    User user = dataSnapshot.child(localPhone.replace(".","_")).getValue(User.class);


                    if (Boolean.parseBoolean(user.getIsStaff())){
                        if (user.getPassword().equals(localPass))
                        {
                            Intent loginIntent = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(loginIntent);
                            finish();
                        }
                        else
                            Toast.makeText(SignIn.this, "Check Your Credentials!", Toast.LENGTH_SHORT).show();


                    }
                    else
                        Toast.makeText(SignIn.this, "Please Login With Staff Account!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignIn.this, "We Can\'t find That User", Toast.LENGTH_SHORT).show();

                pd.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: " + databaseError.getMessage());

            }
        });
    }
}
