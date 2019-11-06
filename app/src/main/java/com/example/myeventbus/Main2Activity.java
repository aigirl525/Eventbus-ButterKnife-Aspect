package com.example.myeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.butterknifelibrary.ButterKnife;
import com.example.butterknifelibrary.ContentView;
import com.example.butterknifelibrary.OnClick;
@ContentView(R.layout.activity_main2)
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inJect(this);

    }


    @OnClick({R.id.btn2})
    public void onClick(View view){
        EventBus.getDefault().post(new SenduoEvent("1","我是Main2Activity"));
        finish();
    }
}
