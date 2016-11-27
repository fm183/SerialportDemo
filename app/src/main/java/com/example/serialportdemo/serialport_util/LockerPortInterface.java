package com.example.serialportdemo.serialport_util;

import java.io.OutputStream;

/**
 * @author fanming
 */
public interface LockerPortInterface {

    /**
     *
     * @param buffer  返回的字节数据
     * @param size    返回的字节长度
     * @param path    串口名，如果有多个串口需要识别是哪个串口返回的数据（传或不传可以根据自己的编码习惯）
     */
    void onLockerDataReceived(final byte[] buffer, final int size, final String path);

    /**
     * 串口输出流，通过该输出流向串口发送指令
     * @param outputStream
     */
    void onLockerOutputStream(OutputStream outputStream);

}
