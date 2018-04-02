package com.example.serialportdemo.serialport_util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by WNI10 on 2018/3/31.
 */

public class PortPrinterBase {
    private OutputStream out;
    protected int lineCount = 40;
    private String printType="0";
    public PortPrinterBase(OutputStream out, String printType){
        this.out = out;
        this.printType = printType;
        initPrinter();
        this.lineCount = 20;
    }
    public static final String LEFT = "LEFT";
    public static final String CENTER = "CENTER";
    public static final String RIGHT = "RIGHT";
//    public static final String
    public static final byte HT = 0x9;
    public static final byte LF = 0x0A;
    public static final byte CR = 0x0D;
    public static final byte ESC = 0x1B;
    public static final byte DLE = 0x10;
    public static final byte GS = 0x1D;
    public static final byte FS = 0x1C;
    public static final byte STX = 0x02;
    public static final byte US = 0x1F;
    public static final byte CAN = 0x18;
    public static final byte CLR = 0x0C;
    public static final byte EOT = 0x04;

    /* 初始化打印机 */
    public static final byte[] ESC_INIT = new byte[] {ESC, '@'};
    /* 设置标准模式 */
    public static final byte[] ESC_STANDARD = new byte[] {ESC, 'S'};
    /* 设置汉字打印模式 */
    public static final byte[] ESC_CN_FONT = new byte[] {FS, '&'};
    /* 选择字符集 */
    public static final byte[] ESC_SELECT_CHARACTER = new byte[] {ESC, 'R', 9};
    /* 设置用户自定义汉字字体 焗7118 */
    public static final byte[] ESC_FS_2 = new byte[] {FS, 0x32, 0x71, 0x18};
    /* 取消用户自定义字体 */
    public static final byte[] ESC_CANCEL_DEFINE_FONT = new byte[]{ESC, '%', 0};
    /* 打开钱箱指令 */
    public static final byte[] ESC_OPEN_DRAWER = new byte[]{ESC, 'p', 0x00, 0x10, (byte) 0xff};
    /* 切纸指令GS V m
    * m  0,48 Executes a full cut(cuts the paper completely)
    *    1,49 Excutes a partilal cut(one point left uncut)
    */
    public static final byte[] POS_CUT_MODE_FULL = new byte[]{GS, 'V', 0x00};
    public static final byte[] POS_CUT_MODE_PARTIAL = new byte[]{GS, 'V', 0x01};
    /* 西文字符 （半宽）字体A (6 ×12)，汉字字符 （全宽）字体A （12×12） */
    public static final byte[] ESC_FONT_A = new byte[]{ESC, '!', 0};
    /* 西文字符 （半宽）字体B (8×16)，汉字字符 （全宽）字体B （16×16） */
    public static final byte[] ESC_FONT_B = new byte[]{ESC, '!', 1};
    /* 12*24   0/48*/
    public static final byte[] ESC_FONTA= new byte[]{ESC, 'M', 48};
    /* 9*17    1/49*/
    public static final byte[] ESC_FONTB= new byte[]{ESC, 'M', 1};
    /* 默认颜色字体指令 */
    public static final byte[] ESC_FONT_COLOR_DEFAULT = new byte[] {ESC, 'r', 0x00};
    /* 红色字体指令 */
    public static final byte[] ESC_FONT_COLOR_RED = new byte[] {ESC, 'r', 0x01 };
    /* 标准大小 */
    public static final byte[] FS_FONT_ALIGN = new byte[]{FS, 0x21, 1, ESC, 0x21, 1};
    /* 横向放大一倍 */
    public static final byte[] FS_FONT_ALIGN_DOUBLE = new byte[]{FS, 0x21, 4, ESC, 0x21, 4};
    /* 纵向放大一倍 */
    public static final byte[] FS_FONT_VERTICAL_DOUBLE = new byte[]{FS, 0x21, 8, ESC, 0x21, 8, GS, '!', 0x01};
    /* 横向纵向都放大一倍 */
    public static final byte[] FS_FONT_DOUBLE = new byte[]{FS, 0x21, 12, ESC, 0x21, 48};
    /* 靠左打印命令 */
    public static final byte[] ESC_ALIGN_LEFT = new byte[]{0x1b,'a', 0x00};
    /* 居中打印命令 */
    public static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b,'a', 0x01};
    /* 靠右打印命令 */
    public static final byte[] ESC_ALIGN_RIGHT = new byte[]{0x1b,'a', 0x02};
    /* 字体加粗 */
    public static final byte[] ESC_SETTING_BOLD = new byte[]{ESC, 0x45, 1};
    /* 取消字体加粗 */
    public static final byte[] ESC_CANCEL_BOLD = new byte[]{ESC, 0x45, 0};
    //DLE EOT n 实时状态传送
    //如果返回结果为22
    /**
     * 、DLE EOT n 实时状态传送
     [格式] ASCII码 DLE EOT n
     十六进制码 10 04 n
     十进制码 16 4 n
     [范围] 1 ≤ n ≤ 4
     [描述] 根据下列参数，实时传送打印机状态，参数 n 用来指定所要传送的打印机状态：
     n = 1：传送打印机状态
     n = 2：传送脱机状态
     n = 3：传送错误状态
     n = 4：传送纸传感器状态
     [注释] 打印机收到该命令后立即返回相关状态
     该命令尽量不要插在2个或更多字节的命令序列中。
     即使打印机被ESC =(选择外设)命令设置为禁止，该命令依然有效。
     打印机传送当前状态，每一状态用1个字节数据表示。
     打印机传送状态时并不确认主机是否收到。
     打印机收到该命令立即执行。
     该命令只对串口打印机有效。打印机在任何状态下收到该命令都立即执行。
     */
    public static final byte[] PRINT_STATE_DLE_EOT = new byte[] {DLE, EOT,0x01};

    public void initPrinter(){
        try {
            //modify by gongqiyi 20090917
            //ESC_INIT 将在清空缓存区的数据
            //out.write(ESC_INIT);
            //自定义字体
            //out.write(ESC_FS_2);
            out.write(ESC_STANDARD);
            out.write(ESC_CANCEL_DEFINE_FONT);
            out.write(ESC_FONTA);
            out.write(ESC_SELECT_CHARACTER);
            //进入汉字模式打印
            //out.write(ESC_CN_FONT);


            //out.write(ESC_FONT_B);
            //out.write(ESC_FONTA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 走纸到切纸位置并切纸
     */
    /*public void executeLineFeedAndPaperCut(){
        try {
            out.write(PrinterParameterConf.printerParameterConf.getProperty
                    (PrinterParameterConf.PRINTCUTLINE_NAME).getBytes());
            out.write(POS_CUT_MODE_PARTIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    /**
     * 单据头打印
     * @param str
     */
    public void billHeaderPrinter(String str){
        try {
            out.write(ESC_ALIGN_CENTER);
            out.write(FS_FONT_DOUBLE);
            out.write((str+"\n").getBytes());
            out.write(LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 叫单号打印
     * @param str
     */
    public void callNumPrinter(String str){
        try {
            out.write(ESC_ALIGN_LEFT);
            out.write(FS_FONT_DOUBLE);
            out.write((str+"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 双倍大小字体
     * @param str
     */
    public void doubleSizePrinter(String str, String align){
        try {
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_LEFT);
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
            }else{
                out.write(ESC_ALIGN_LEFT);
            }
            out.write(FS_FONT_DOUBLE);
            out.write((str+"\n").getBytes());
            //out.write(LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 标准字体打印一行
     * @param str 需打印的字符串
     * @param align 打印的位置 LEFT/CENTER/RIGHT 其他为默认居左打印
     */
    public void standardPrinterLine(String str, String align){
        try{
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_CENTER);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_CANCEL_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_CANCEL_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }else{
                out.write(ESC_ALIGN_LEFT);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_CANCEL_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }
            out.write("\n".getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 标准粗体字体打印一行
     * @param str 需打印的字符串
     * @param align 打印的位置 LEFT/CENTER/RIGHT 其他为默认居左打印
     */
    public void standardBoldPrinterLine(String str, String align){
        try{
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_CENTER);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_SETTING_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_SETTING_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }else{
                out.write(ESC_ALIGN_LEFT);
                out.write(FS_FONT_ALIGN);
                out.write(ESC_CN_FONT);
                out.write(ESC_SETTING_BOLD);
                if("1".equals(printType)){
                    out.write(ESC_FONTA);
                }else{
                    out.write(ESC_FONT_B);
                }
                out.write(str.getBytes());
            }
            out.write("\n".getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双倍宽字体按行打印
     * @param str
     * @param align
     */
    public void largeSizePrinterLine(String str, String align){
        try{
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_CENTER);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(str.getBytes());
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(str.getBytes());
            }else{
                out.write(ESC_ALIGN_LEFT);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(str.getBytes());
            }
            out.write("\n".getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双倍高字体按行打印
     * @param str
     * @param align
     */
    public void largeHSizePrinterLine(String str, String align){
        try{
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_CENTER);
                out.write(FS_FONT_VERTICAL_DOUBLE);
                out.write(str.getBytes());
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
                out.write(FS_FONT_VERTICAL_DOUBLE);
                out.write(str.getBytes());
            }else{
                out.write(ESC_ALIGN_LEFT);
                out.write(FS_FONT_VERTICAL_DOUBLE);
                out.write(str.getBytes());
            }
            out.write("\n".getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }    /**
     * 大号字体红色按行打印
     * @param str
     * @param align
     */
    public void largeSizeRedPrinterLine(String str, String align){
        try{
            if(CENTER.equals(align)){
                out.write(ESC_ALIGN_CENTER);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(ESC_FONT_COLOR_RED);
                out.write(str.getBytes());
            }else if(RIGHT.equals(align)){
                out.write(ESC_ALIGN_RIGHT);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(ESC_FONT_COLOR_RED);
                out.write(str.getBytes());
            }else{
                out.write(ESC_ALIGN_LEFT);
                out.write(FS_FONT_ALIGN_DOUBLE);
                out.write(ESC_FONT_COLOR_RED);
                out.write(str.getBytes());
            }
            out.write("\n".getBytes());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void openDrawer(){
        try {
            out.write(ESC_OPEN_DRAWER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String makePrintString(int lineChars, String txt1, String txt2){
        if(txt1 == null){
            txt1 = "";
        }
        if(txt2 == null){
            txt2 = "";
        }
        int spaces = 0;
        String tab = "";
        try{
            spaces = lineChars - (txt1.getBytes().length + txt2.getBytes().length);
            for (int j = 0 ; j < spaces ; j++){
                tab += " ";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return txt1 + tab + txt2;
    }
    public String makePrintString(int lineChars, String txt1, String txt2, String txt3){
        if(txt1 == null){
            txt1 = "";
        }
        if(txt2 == null){
            txt2 = "";
        }
        if(txt3 == null){
            txt3 = "";
        }
        int spaces = 0;
        int lineChars1 = lineChars*2/3;
        String tab = "";
        String returnStr = txt1;
        try{
            spaces = lineChars1 - (returnStr.getBytes().length + txt2.getBytes().length);
            for (int j = 0 ; j < spaces ; j++){
                tab += " ";
            }
            returnStr = txt1 + tab + txt2;
            spaces = lineChars - (returnStr.getBytes().length + txt3.getBytes().length);
            tab = "";
            for (int j = 0 ; j < spaces ; j++){
                tab += " ";
            }
            returnStr = returnStr + tab + txt3;
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnStr;
    }
}
