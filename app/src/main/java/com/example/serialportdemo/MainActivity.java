package com.example.serialportdemo;

import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.serialportdemo.serialport_util.LockerPortInterface;
import com.example.serialportdemo.serialport_util.LockerSerialportUtil;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements LockerPortInterface,View.OnClickListener{


    private static final String TAG = "MainActivity";
    private Button mBtnOpen,mBtnClose,mBtnSend;
    private EditText mEtSend;
    /**
     * 串口名称
     */
    private String PATH = "/dev/ttyS2";
    /**
     * 波特率
     */
    private int BAUDRATE = 9600;
    /**
     * 输出流，向串口发送指令
     */
    private OutputStream mOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnOpen = (Button) findViewById(R.id.btn_open);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtSend = (EditText) findViewById(R.id.et_send);
        mBtnOpen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onLockerDataReceived(byte[] buffer, int size, String path) {
        String result = new String(buffer,0,size);
        Log.e(TAG,"onLockerDataReceived===="+result);
    }

    @Override
    public void onLockerOutputStream(OutputStream outputStream) {
        this.mOutputStream = outputStream;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open:
                openPort();
                break;
            case R.id.btn_close:
                closePort();
                break;
            case R.id.btn_send:
                String cmd = mEtSend.getText().toString();
                if(TextUtils.isEmpty(cmd)){
                    Toast.makeText(this,"请输入指令！",Toast.LENGTH_SHORT).show();

                    return;
                }
                sendParams(cmd);
                break;
        }
    }


    /**
     * 打开串口
     */
    private void openPort(){
        LockerSerialportUtil.init(this,PATH,BAUDRATE,this);
    }

    /**
     * 关闭串口
     */
    private void closePort(){
        LockerSerialportUtil.getInstance().closeSerialPort();
    }

    /**
     * 关闭串口
     * @param cmd   指令，具体的指令是根据具体的协议来决定的
     */
    private void sendParams(String cmd){
        if(mOutputStream == null){
            return;
        }
        // TODO 发送指令
        try {
            mOutputStream.write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
