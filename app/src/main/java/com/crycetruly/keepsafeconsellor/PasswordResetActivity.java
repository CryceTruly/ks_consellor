package com.crycetruly.keepsafeconsellor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crycetruly.keepsafeconsellor.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordResetActivity";
    private Context mContext;
    private TextInputEditText textInputLayout;
    private Button update;
    private Toolbar toolbar;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        dialog = new ProgressDialog(this);
        toolbar = findViewById(R.id.cpt);
        mContext = PasswordResetActivity.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enter your Email ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        update = findViewById(R.id.reqEmail);
        textInputLayout = findViewById(R.id.jj);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textInputLayout.getText().toString().trim();

                if (!TextUtils.isEmpty(email)) {
                    if (Utils.isValidEmail(email)) {
                        RequestPasswordResetMail(email);
                    } else {
                        Toast.makeText(mContext, "Your Email is not a valid one,please check it first", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(mContext, "Please enter an email", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void RequestPasswordResetMail(String email) {
        dialog.setMessage("Requesting Reset Email");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        update.setEnabled(false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Email sent", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User email sent updated.");
                            startActivity(new Intent(mContext, AuthActivity.class));
                            dialog.dismiss();
                            update.setEnabled(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                update.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

