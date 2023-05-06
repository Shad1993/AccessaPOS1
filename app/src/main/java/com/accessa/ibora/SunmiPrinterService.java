package com.accessa.ibora;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.InnerTaxCallback;
import com.sunmi.peripheral.printer.SystemPropertyUtil;
import com.sunmi.peripheral.printer.TransBean;

import java.util.Locale;

public interface SunmiPrinterService extends IInterface {
    void updateFirmware() throws RemoteException;

    int getFirmwareStatus() throws RemoteException;

    String getServiceVersion() throws RemoteException;

    void printerInit(InnerResultCallback callback) throws RemoteException;

    void printerSelfChecking(InnerResultCallback callback) throws RemoteException;

    String getPrinterSerialNo() throws RemoteException;

    String getPrinterVersion() throws RemoteException;

    String getPrinterModal() throws RemoteException;

    void getPrintedLength(InnerResultCallback callback) throws RemoteException;

    void lineWrap(int n, InnerResultCallback callback) throws RemoteException;

    void sendRAWData(byte[] data, InnerResultCallback callback) throws RemoteException;

    void setAlignment(int alignment, InnerResultCallback callback) throws RemoteException;

    void setFontName(String typeface, InnerResultCallback callback) throws RemoteException;

    void setFontSize(float fontsize, InnerResultCallback callback) throws RemoteException;

    void printText(String text, InnerResultCallback callback) throws RemoteException;

    void printTextWithFont(String text, String typeface, float fontsize, InnerResultCallback callback) throws RemoteException;

    void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, InnerResultCallback callback) throws RemoteException;

    void printBitmap(Bitmap bitmap, InnerResultCallback callback) throws RemoteException;

    void printBarCode(String data, int symbology, int height, int width, int textposition, InnerResultCallback callback) throws RemoteException;

    void printQRCode(String data, int modulesize, int errorlevel, InnerResultCallback callback) throws RemoteException;

    void printOriginalText(String text, InnerResultCallback callback) throws RemoteException;

    void commitPrint(TransBean[] transbean, InnerResultCallback callback) throws RemoteException;

    void commitPrinterBuffer() throws RemoteException;

    void cutPaper(InnerResultCallback callback) throws RemoteException;

    int getCutPaperTimes() throws RemoteException;

    void openDrawer(InnerResultCallback callback) throws RemoteException;

    int getOpenDrawerTimes() throws RemoteException;

    void enterPrinterBuffer(boolean clean) throws RemoteException;

    void exitPrinterBuffer(boolean commit) throws RemoteException;

    void tax(byte[] data, InnerTaxCallback callback) throws RemoteException;

    void getPrinterFactory(InnerResultCallback callback) throws RemoteException;

    void clearBuffer() throws RemoteException;

    void commitPrinterBufferWithCallback(InnerResultCallback callback) throws RemoteException;

    void exitPrinterBufferWithCallback(boolean commit, InnerResultCallback callback) throws RemoteException;

    void printColumnsString(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, InnerResultCallback callback) throws RemoteException;

    int updatePrinterState() throws RemoteException;

    void sendLCDCommand(int flag) throws RemoteException;

    void sendLCDString(String string, InnerLcdCallback callback) throws RemoteException;

    void sendLCDBitmap(Bitmap bitmap, InnerLcdCallback callback) throws RemoteException;

    int getPrinterMode() throws RemoteException;

    int getPrinterBBMDistance() throws RemoteException;

    void printBitmapCustom(Bitmap bitmap, int type, InnerResultCallback callback) throws RemoteException;

    int getForcedDouble() throws RemoteException;

    boolean isForcedAntiWhite() throws RemoteException;

    boolean isForcedBold() throws RemoteException;

    boolean isForcedUnderline() throws RemoteException;

    int getForcedRowHeight() throws RemoteException;

    int getFontName() throws RemoteException;

    void sendLCDDoubleString(String topText, String bottomText, InnerLcdCallback callback) throws RemoteException;

    int getPrinterPaper() throws RemoteException;

    boolean getDrawerStatus() throws RemoteException;

    void sendLCDFillString(String string, int size, boolean fill, InnerLcdCallback callback) throws RemoteException;

    void sendLCDMultiString(String[] text, int[] align, InnerLcdCallback callback) throws RemoteException;

    int getPrinterDensity() throws RemoteException;

    void print2DCode(String data, int symbology, int modulesize, int errorlevel, InnerResultCallback callback) throws RemoteException;

    void autoOutPaper(InnerResultCallback callback) throws RemoteException;

    void setPrinterStyle(int key, int value) throws RemoteException;

    void labelLocate() throws RemoteException;

    void labelOutput() throws RemoteException;

    public abstract static class Stub extends Binder implements com.sunmi.peripheral.printer.SunmiPrinterService {
        private static final String DESCRIPTOR = "woyou.aidlservice.jiuiv5.IWoyouService";
        static final int[][] TRANSCTION_DATASHEET = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9527, 9527, 9527, 9527, 9527, 9527, 9527, 5, 9527, 0, -9, -9, -9, -9, -9, -9, -9, 9527, -8, 9527, 9527, 9527, -14, -14, -13, -13, -12, -12}, {-1, 3, 3, 3, 9527, 9527, 8, 8, 1, 1, -10, -10, -10, -10, -3, -3, -3, -10, -10, 9527, -2, 9527, 9527, 9527, 9527, 9527, 9527, -10, -4, -10, -10, -10, -10, -10, -9, -9, 9527, 9527}, {0, 0, 0, 9527, 9527, 9527, 9527, 9527, -5, 7, 9527, 9527, 9527, 9527, 9527, 9527, 9527, 9527, 9527, 0, -15, -15, -15, -15, -15, -15, -15, 9527, -14, 9527, 9527, 9527, -20, -20, -19, -19, 9527, 9527}, {-1, 3, 3, 3, 9527, 9527, 5, 5, 1, 1, -10, -10, -10, -10, 9527, 9527, 9527, -10, -10, 9527, -6, 9527, 9527, 9527, 9527, 9527, 9527, 9527, -13, -13, 9527, 9527, -15, -15, 9527, -16, 9527, 9527}};
        static final int TRANSACTION_STOP = 9527;
        static final int TRANSACTION_updateFirmware = 1;
        static final int TRANSACTION_getFirmwareStatus = 2;
        static final int TRANSACTION_getServiceVersion = 3;
        static final int TRANSACTION_printerInit = 4;
        static final int TRANSACTION_printerSelfChecking = 5;
        static final int TRANSACTION_getPrinterSerialNo = 6;
        static final int TRANSACTION_getPrinterVersion = 7;
        static final int TRANSACTION_getPrinterModal = 8;
        static final int TRANSACTION_getPrintedLength = 9;
        static final int TRANSACTION_lineWrap = 10;
        static final int TRANSACTION_sendRAWData = 11;
        static final int TRANSACTION_setAlignment = 12;
        static final int TRANSACTION_setFontName = 13;
        static final int TRANSACTION_setFontSize = 14;
        static final int TRANSACTION_printText = 15;
        static final int TRANSACTION_printTextWithFont = 16;
        static final int TRANSACTION_printColumnsText = 17;
        static final int TRANSACTION_printBitmap = 18;
        static final int TRANSACTION_printBarCode = 19;
        static final int TRANSACTION_printQRCode = 20;
        static final int TRANSACTION_printOriginalText = 21;
        static final int TRANSACTION_commitPrint = 22;
        static final int TRANSACTION_commitPrinterBuffer = 23;
        static final int TRANSACTION_enterPrinterBuffer = 24;
        static final int TRANSACTION_exitPrinterBuffer = 25;
        static final int TRANSACTION_tax = 26;
        static final int TRANSACTION_getPrinterFactory = 27;
        static final int TRANSACTION_clearBuffer = 28;
        static final int TRANSACTION_commitPrinterBufferWithCallback = 29;
        static final int TRANSACTION_exitPrinterBufferWithCallback = 30;
        static final int TRANSACTION_printColumnsString = 31;
        static final int TRANSACTION_updatePrinterState = 32;
        static final int TRANSACTION_cutPaper = 33;
        static final int TRANSACTION_getCutPaperTimes = 34;
        static final int TRANSACTION_openDrawer = 35;
        static final int TRANSACTION_getOpenDrawerTimes = 36;
        static final int TRANSACTION_sendLCDCommand = 37;
        static final int TRANSACTION_sendLCDString = 38;
        static final int TRANSACTION_sendLCDBitmap = 39;
        static final int TRANSACTION_getPrinterMode = 40;
        static final int TRANSACTION_getPrinterBBMDistance = 41;
        static final int TRANSACTION_printBitmapCustom = 42;
        static final int TRANSACTION_getForcedDouble = 43;
        static final int TRANSACTION_isForcedAntiWhite = 44;
        static final int TRANSACTION_isForcedBold = 45;
        static final int TRANSACTION_isForcedUnderline = 46;
        static final int TRANSACTION_getForcedRowHeight = 47;
        static final int TRANSACTION_getFontName = 48;
        static final int TRANSACTION_sendLCDDoubleString = 49;
        static final int TRANSACTION_getPrinterPaper = 50;
        static final int TRANSACTION_getDrawerStatus = 51;
        static final int TRANSACTION_sendLCDFillString = 52;
        static final int TRANSACTION_sendLCDMultiString = 53;
        static final int TRANSACTION_getPrinterDensity = 54;
        static final int TRANSACTION_print2DCode = 55;
        static final int TRANSACTION_autoOutPaper = 56;
        static final int TRANSACTION_setPrinterStyle = 57;
        static final int TRANSACTION_labelLocate = 58;
        static final int TRANSACTION_labelOutput = 59;}}

