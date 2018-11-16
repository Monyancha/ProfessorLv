package com.amsavarthan.professorlv.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amsavarthan.professorlv.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import shortbread.Shortbread;
import shortbread.Shortcut;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 100;
    private int cameraPermissionCheck = PackageManager.PERMISSION_DENIED;
    private int externalStoragePermissionCheck = PackageManager.PERMISSION_DENIED;


    @Shortcut(id = "scan_question", icon = R.drawable.ic_photo_camera_black_24dp, shortLabel = "Scan a question")
    public void scanQuestion() {
        if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED && externalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","camera"));
        } else {
            verifyAndRequestPermission();
        }    }


    @Shortcut(id = "enter_question", icon = R.drawable.ic_edit_black_24dp, shortLabel = "Enter a question")
    public void enterQuestion() {
        startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","input"));
    }


    @Shortcut(id = "tell_question", icon = R.drawable.ic_mic_black_24dp, shortLabel = "Tell a question")
    public void tellQuestion() {
        startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","speech"));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
    }

    public static void startActivity(Context context,String fragment){
        context.startActivity(new Intent(context,MainActivity.class).putExtra("frag",fragment));
    }

    private void verifyAndRequestPermission() {

        cameraPermissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        externalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED || externalStoragePermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        cameraPermissionCheck = PackageManager.PERMISSION_GRANTED;
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        externalStoragePermissionCheck = PackageManager.PERMISSION_GRANTED;

                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeActivity();
        super.onCreate(savedInstanceState);
        Shortbread.create(this);
        setContentView(R.layout.activity_main_prof);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            verifyAndRequestPermission();

        if(!isDeviceSupportCamera()){
            Snackbar.make(findViewById(R.id.main),"Sorry ,Your device does not support camera :(.",Snackbar.LENGTH_LONG).show();
        }

    }

    private void initializeActivity() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/bold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public void changeFragment(View view) {

        switch (view.getId()){

            case R.id.input:

                startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","input"));
                return;

            case R.id.camera:

                if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED && externalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","camera"));
                } else {
                    verifyAndRequestPermission();
                }

                return;
            case R.id.speech:

                startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","speech"));
                return;

            case R.id.math:

                if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED && externalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, FragmentContainer.class).putExtra("name","math"));
                } else {
                    verifyAndRequestPermission();
                }
                return;

            case R.id.post:
                startActivity(new Intent(MainActivity.this, AllQuestionsActivity.class));
                return;
            case R.id.my_questions:
                startActivity(new Intent(MainActivity.this, MyQuestions.class));


        }

    }

    public void showAbout(View view) {

        new MaterialDialog.Builder(this)
                .title("About Professor")
                .content("This app is developed by Amsavarthan Lv with Love from India especially for students in order to enrich their knowledge with in-built Artificial Intelligence for clearing their doubts.")
                .positiveText("Close")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}
