package com.accessa.ibora;


import android.content.Intent;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;



public class BarcodeReader extends AppCompatActivity {
    private static final int START_SCAN = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);





        /*
Create an Intent where you want to start scanning; Call the scanning module by
startActiityForResult().
*/



        Intent intent = new Intent("com.summi.scan");
        intent.setClassName("com.sunmi.sunmiqrcodescanner",
                "com.sunmi.sunmiqrcodescanner.activity.ScanActivity");

        intent.putExtra("CURRENT_PPI", 0X0003);// The current resolution

        intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
        intent.putExtra("PLAY_VIBRATE", false);
//扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
        intent.putExtra("IDENTIFY_MORE_CODE", false);// Identify multiple QR codes in the screen, the default is false
        intent.putExtra("IS_SHOW_SETTING", true);// Whether to display the setting button in the upper right corner, the default is true
        intent.putExtra("IS_SHOW_ALBUM", true);// Whether to display the button to select a picture from the album, the default is true
        intent.putExtra("IDENTIFY_INVERSE", true);// Allow to read inverse QR code, default true
        intent.putExtra("IS_EAN_8_ENABLE", true);//Allow to read EAN-8 code, default true: allow
        intent.putExtra("IS_UPC_E_ENABLE", true);//Allow to read UPC-E code, default true: allow
        intent.putExtra("IS_ISBN_10_ENABLE", true);//Allow to read ISBN-10 (from EAN-13) code, default true: allow
        intent.putExtra("IS_CODE_11_ENABLE", true);//Allow to read CODE-11 code, default false: not allowed
        intent.putExtra("IS_UPC_A_ENABLE", true);//Allow to read UPC-A code, default true: allow
        intent.putExtra("IS_EAN_13_ENABLE", true);//Allow to read AN-13 code, default true: allow
        intent.putExtra("IS_ISBN_13_ENABLE", true);//Allow to read ISBN-13 (from EAN-13) code, default true: allow
        intent.putExtra("IS_INTERLEAVED_2_OF_5_ENABLE", true);//Allow to read Interleaved 2 of 5 codes, default false: not allowed
        intent.putExtra("IS_CODE_128_ENABLE", true);//Allow to read ECode 128 code, default true: allow
        intent.putExtra("IS_CODABAR_ENABLE", true);//Allow to read Codabar code, default true: allow
        intent.putExtra("IS_CODE_39_ENABLE", true);//Allow to read Code 39 code, default true: allow
        intent.putExtra("IS_CODE_93_ENABLE", true);//Allow to read Code 93 code, default true: allow
        intent.putExtra("IS_DATABAR_ENABLE", true);//Allow to read DataBar (RSS-14) code, default true: allow
        intent.putExtra("IS_DATABAR_EXP_ENABLE", true);//Allow to read DataBar Expanded code, default true: allow
        intent.putExtra("IS_Micro_PDF417_ENABLE", true);//Allow to read Micro PDF417 code, default true: allow
        intent.putExtra("IS_MicroQR_ENABLE", true);//Allow to read Micro QR Code code, default true: allow
        intent.putExtra("IS_Hanxin_ENABLE", true);//Allow reading Hanxin Code code, default true: allow
        intent.putExtra("IS_OPEN_LIGHT", true);// Whether to display the flash, the default is true
        intent.putExtra("SCAN_MODE", false);// Whether it is a loop mode, the default is false
        intent.putExtra("IS_QR_CODE_ENABLE", true);// Allow to read QR code, default true
        intent.putExtra("IS_PDF417_ENABLE", true);// Allow to read PDF417 code, default true
        intent.putExtra("IS_DATA_MATRIX_ENABLE", true);// Allow to read DataMatrix code, default true
        intent.putExtra("IS_AZTEC_ENABLE", true);// Allow to read AZTEC code, default true

        startActivityForResult(intent, 1);

    }


}