package com.example.perry.yoursidesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Created by perry on 2018/3/11.
 */

public class InputActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView cancelBtn, inputBtn;
    private TextView usernameView;
    private RadioGroup sexGroup;
    private EditText heightText;
    private EditText weightText;
    private String sex;
    public static final String SEX_TAG = "SEx_tag";
    public static final String WEIGHT_TAG = "WEIGHT_tag";
    public static final String HEIGHT_TAG = "HEIGHT_tag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        cancelBtn = (ImageView) findViewById(R.id.cancel_input);
        inputBtn = (ImageView) findViewById(R.id.input_btn);
        usernameView = (TextView) findViewById(R.id.user_name);
        sexGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
        heightText = (EditText) findViewById(R.id.height);
        weightText = (EditText) findViewById(R.id.weight);
        cancelBtn.setOnClickListener(this);
        inputBtn.setOnClickListener(this);
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.manRadio) {
                    sex = "男";
                } else if (checkedId == R.id.womamRadio) {
                    sex = "女";
                }
            }
        });
        Intent intent = getIntent();
        usernameView.setText("用户：" + intent.getStringExtra(MainActivity.USER_NAME_TAG));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_input:
                finish();
                break;
            case R.id.input_btn:
                Intent intent = new Intent();
                intent.putExtra(SEX_TAG, sex);
                intent.putExtra(WEIGHT_TAG, weightText.getText().toString());
                intent.putExtra(HEIGHT_TAG, heightText.getText().toString());
                this.setResult(999, intent);
                finish();
                break;

        }
    }
}
