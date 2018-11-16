package com.amsavarthan.professorlv.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amsavarthan.professorlv.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ProfileSetup extends AppCompatActivity {

    private static final String TAG =ProfileSetup.class.getSimpleName() ;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String phone;
    private EditText name_field,age_field,phone_field;
    private ProgressDialog mDialog;
    public static Activity profile_activity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/bold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        profile_activity=this;

        mFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();

        phone=getIntent().getStringExtra("phone");
        name_field=findViewById(R.id.name);
        age_field=findViewById(R.id.age);
        phone_field=findViewById(R.id.phone);

        if(!TextUtils.isEmpty(phone)) {
            phone_field.setText(phone);
        }else{
            phone_field.setText(mCurrentUser.getPhoneNumber());
        }

        mDialog=new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

    }

    public void onFabClicked(View view) {

        if(validate()){

            mDialog.show();
            Map<String,Object> userMap=new HashMap<>();
            userMap.put("id",mCurrentUser.getUid());
            userMap.put("name",name_field.getText().toString());
            userMap.put("age",age_field.getText().toString());
            userMap.put("phone",phone_field.getText().toString());

            mFirestore.collection("Users")
                    .document(mCurrentUser.getUid())
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDialog.dismiss();
                            startActivity(new Intent(ProfileSetup.this, MainActivity.class));
                            Toast.makeText(ProfileSetup.this, "Welcome "+name_field.getText().toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    });

        }

    }

    public void onChangePhoneClicked(View view) {

        startActivity(new Intent(ProfileSetup.this, Login.class).putExtra("phone",phone_field.getText().toString()));

    }

    private boolean validate() {
        String name = name_field.getText().toString();
        String age = age_field.getText().toString();
        String phoneNumber = phone_field.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(phoneNumber)) {
            Snackbar.make(findViewById(R.id.layout), "Invalid details.",
                    Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
