package com.example.myeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aspectlibrary.CheckLogin;
import com.example.aspectlibrary.CheckNet;
import com.example.butterknifelibrary.BindView;
import com.example.butterknifelibrary.ButterKnife;
import com.example.butterknifelibrary.ContentView;
import com.example.butterknifelibrary.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inJect(this);
        EventBus.getDefault().register(this);

    }


    @OnClick({R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            default:
                Log.e("MainActivity", "view not found");
                break;
        }
    }


    @CheckNet
    public void check(View view) {
        startActivity(new Intent(this, Main2Activity.class));
    }

    @CheckLogin(MyApplication.isLogin)
    public void checklogin(View view) {
        startActivity(new Intent(this, Main2Activity.class));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void showTextView(SenduoEvent senduoEvent) {
        tv.setText(senduoEvent.toString());
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void showLog(SenduoEvent senduoEvent) {
        Log.e("MainActivity", senduoEvent.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
