package com.example.serialportdemo;

import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serialportdemo.serialport_util.LockerPortInterface;
import com.example.serialportdemo.serialport_util.LockerSerialportUtil;
import com.example.serialportdemo.serialport_util.PortPrinterBase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android_serialport_api.SerialPortFinder;

public class MainActivity extends AppCompatActivity implements LockerPortInterface,View.OnClickListener{


    private static final String TAG = "MainActivity";

    private static final int[] bytes = new int[]{0x1B,0x70,0x0,0x3C, 0xFF};

    private Button mBtnOpen,mBtnClose,mBtnSend;
    private EditText mEtSend;
    private TextView tv_log,tv_select_path,tv_select_baud;
    private StringBuffer stringBuffer;
    private SerialPortFinder mSerialPortFinder;
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
    private PortPrinterBase portPrinterBase;

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
        tv_log = (TextView) findViewById(R.id.tv_log);
        tv_select_path = (TextView) findViewById(R.id.tv_select_path);
        tv_select_baud = (TextView) findViewById(R.id.tv_select_baud);
        stringBuffer = new StringBuffer();
        mBtnOpen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        tv_select_path.setOnClickListener(this);
        tv_select_baud.setOnClickListener(this);
        mSerialPortFinder = new SerialPortFinder();
    }

    @Override
    public void onLockerDataReceived(byte[] buffer, int size, String path) {
        String result = new String(buffer,0,size);
        Log.e(TAG,"onLockerDataReceived===="+result);
        stringBuffer.append("result="+result);
        tv_log.setText(result);
    }

    @Override
    public void onLockerOutputStream(OutputStream outputStream) {
        this.mOutputStream = outputStream;
        portPrinterBase = new PortPrinterBase(outputStream,"1");
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
                stringBuffer = new StringBuffer();
                /*String cmd = mEtSend.getText().toString();
                if(TextUtils.isEmpty(cmd)){
                    Toast.makeText(this,"请输入指令！",Toast.LENGTH_SHORT).show();

                    return;
                }*/
                /*sendParams(bytes);*/
                String text = mEtSend.getText().toString();
                portPrinterBase.standardPrinterLine(text,PortPrinterBase.LEFT);
                break;
            case R.id.tv_select_path:
                List<String> list = getAllDevicesPath();
                if (list == null || list.size() <= 0) {
                    Toast.makeText(this, "木有串口设备哦", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "木有串口设备哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                OneColumDialog dialog = new OneColumDialog(this, list, new OneColumDialog.SelectListener() {
                    @Override
                    public void selected(int position, String value) {
                        tv_select_path.setText(value);
                    }
                });
                dialog.show();
                break;
            case R.id.tv_select_baud:

                break;
            case R.id.btn_openbox:
                int bytes[] = new int[]{0x1b,0x70,0x00,0x3c,0xc3,0xb0};
                sendParams(bytes);
                break;
            default:

                break;
        }
    }


    /**
     * 获取全部窗口地址
     *
     * @return
     */
    public List<String> getAllDevicesPath() {
        return Arrays.asList(mSerialPortFinder.getAllDevicesPath());
    }



    /**
     * 打开串口
     */
    private void openPort(){
        String path = tv_select_path.getText().toString();
        if("".equals(path)){
            Toast.makeText(this,"请选择串口号",Toast.LENGTH_SHORT).show();
            return;
        }
        LockerSerialportUtil.init(this,path,BAUDRATE,this);
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


    private void sendParams(byte[] bytes){
        if(mOutputStream == null){
            return;
        }
        // TODO 发送指令
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendParams(int ...params){
        if(mOutputStream == null){
            return;
        }
        // TODO 发送指令
        try {

            for (int param : params){
                stringBuffer.append(param+",");
                mOutputStream.write(param);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
