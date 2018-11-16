package com.amsavarthan.professorlv.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.amsavarthan.professorlv.R;
import com.amsavarthan.professorlv.fragment.CameraScan;
import com.amsavarthan.professorlv.fragment.Input;
import com.amsavarthan.professorlv.fragment.Math;
import com.amsavarthan.professorlv.fragment.Speech;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class FragmentContainer extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/bold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        String name=getIntent().getStringExtra("name");

        switch (name){

            case "input":
                loadfragment(new Input());
                return;
            case "camera":
                loadfragment(new CameraScan());
                return;
            case "speech":
                loadfragment(new Speech());
                return;
            case "math":
                loadfragment(new Math());
                return;     default:
                Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();

        }

    }

    public void loadfragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }


}
