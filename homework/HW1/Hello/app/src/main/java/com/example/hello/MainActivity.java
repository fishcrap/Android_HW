package com.example.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    boolean isCheck = false;
    boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //checkbox listener
        CheckBox cb = findViewById(R.id.box_1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean Check) {
                isCheck = Check;
                Log.i("checkbox","click checkbox");
            }
        });

        //button listener
        //connect button with textview
        Button btn = findViewById(R.id.btn_click_1);
        final TextView tv = findViewById(R.id.text_content);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheck) //only checkbox is checked can button functions
                {
                    if(isClick)
                        tv.setText("what a surprise");
                    else
                        tv.setText("Hello World!");
                    isClick = !isClick;
                }

                Log.i("textChange","button click");
            }
        });

        //ToggleButton lister
        ToggleButton tb = findViewById(R.id.togbtn_1);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean Check) {
                Log.i("Toggle",":Toggle button click");
            }
        });

        //spinner listener
        Spinner sp = findViewById(R.id.spin_array);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] week = getResources().getStringArray(R.array.week);
                Toast.makeText(MainActivity.this, "choice:" + week[i], Toast.LENGTH_SHORT).show();
                Log.i("select","choose " + week[i]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.i("myFirstAppUI", "onCreate: Hello");
    }
}
