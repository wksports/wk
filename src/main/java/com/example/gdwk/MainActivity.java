package com.example.gdwk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Runnable {
//在点击了button之后每间隔一秒钟刷新一下textView中的数值，每次减一，减到0为止，实现倒计时显示的功能
    private Handler mainHandler;
    private TextView mTextView;
    private Thread mThread;
    private boolean mflag;
    private int mCount=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                switch (what){
                    case 1:
                        int arg1 = msg.arg1;
                        Log.d("Kodulf","Handler arg1="+arg1);
                        Log.d("Kodulf", "TestView  =" + mTextView.getText().toString());
                        mTextView.setText(String.valueOf(arg1));
                        break;
                }
            }
        };

        mTextView=(TextView)findViewById(R.id.textView);

        //子线程的初始化
        mThread = new Thread((Runnable) this);
    }


    public void btnDaoJiShi(View view) {
        Log.d("Kodulf","mThread state"+mThread.getState());
        Log.d("Kodulf","mThread toString"+mThread.toString());

        if(!mThread.isAlive()){
            //开始计时器或者是重启计时器，设置标记为true
            mflag=true;

            if(mThread.getState()==Thread.State.TERMINATED){
                mThread = new Thread((Runnable) this);
                if(mCount==-1) mCount=10;
                mThread.start();
            }else{
                mThread.start();
            }

        }else {

            mflag=false;

        }
    }

    @Override
    public void run() {

        while(mflag&&mCount>=0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //每间隔 一秒钟 发送 一个Message 给主线程的 handler让主线程的hanlder 来修改UI

            Message message = Message.obtain();
            message.what=1;
            message.arg1=mCount;
            mainHandler.sendMessage(message);
            Log.d("Kodulf","mCount="+mCount--);
        }
    }

    //在onDestroy方法里停止线程
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mflag=false;
    }
}