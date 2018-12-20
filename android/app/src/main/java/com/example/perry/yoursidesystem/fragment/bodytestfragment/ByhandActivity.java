package com.example.perry.yoursidesystem.fragment.bodytestfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.test.LogUtil;

public class ByhandActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView cacelView;
    private Button inputBtn;
    private EditText systolicET;
    private EditText distolicET;
    private EditText temperatureET;
    private EditText heartRateET;
    public static final String SYSTOLIC_TAG="SYSTOLIC";
    public static final String DIASTOLIC_TAG="dia";
    public static final String TEMPERATURE_TAG="tem";
    public static final String HEART_RATE_TAG="heart";
    public static final int RESULT_CODE=520;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_byhand);
        cacelView= (ImageView) findViewById(R.id.cancel_input);
        inputBtn= (Button) findViewById(R.id.input_btn);
        systolicET= (EditText) findViewById(R.id.systolicPre);
        distolicET= (EditText) findViewById(R.id.diastolicPre);
        temperatureET= (EditText) findViewById(R.id.temperature);
        heartRateET= (EditText) findViewById(R.id.heartRate);
        cacelView.setOnClickListener(this);
        inputBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.input_btn:
                if(!handleInputData())
                    break;
            case R.id.cancel_input:
                finish();
                break;
        }
    }
    
    private boolean  handleInputData(){
        String systolic=systolicET.getText().toString().trim();
        String distolic=distolicET.getText().toString().trim();
        String temperature=temperatureET.getText().toString().trim();
        String heatRate=heartRateET.getText().toString().trim();
        if("".equals(systolic)){
            Toast.makeText(this,"低压未输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        if("".equals(distolic)){
            Toast.makeText(this,"高压未输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        if("".equals(temperature)||Float.valueOf(temperature)<32){
            Toast.makeText(this,"体温未输入或输入不正常体温",Toast.LENGTH_SHORT).show();
            return false;
        }
        if("".equals(heatRate)){
            Toast.makeText(this,"心率未输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        LogUtil.i("tag3", "低压2：" + systolic + "，高压2：" + distolic + "，体温2：" + temperature +
                "，心率2：" + heatRate);
        Bundle bundle=new Bundle();
        bundle.putString(SYSTOLIC_TAG,systolic);
        bundle.putString(DIASTOLIC_TAG,distolic);
        bundle.putString(TEMPERATURE_TAG,temperature);
        bundle.putString(HEART_RATE_TAG,heatRate);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        this.setResult(RESULT_CODE,intent);
        return true;
    }
    
}
