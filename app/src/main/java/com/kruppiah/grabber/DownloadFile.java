package com.kruppiah.grabber;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class DownloadFile extends Thread{

    Handler mHandler;
    String link;
    String name;

    public DownloadFile(Handler mHandler, String link, String name) {

        this.mHandler = mHandler;
        this.link = link;
        this.name = name;
    }

    @Override
    public void run() {

        File f = null;

        try {

            Log.i("DOWNLOADFILE", "Entering run()");

            f = new File(Environment.getExternalStorageDirectory() + "/Grabber/" + name);

            FileOutputStream fileOutputStream = new FileOutputStream(f,true);

            Log.i("DOWNLOADFILE", "outputstream works!");

            BufferedInputStream bins = new BufferedInputStream(new URL(link).openStream());
            byte[] buffer = new byte[1024];
            int n;

            while ((n = bins.read(buffer,0,1024))!=-1){
                fileOutputStream.write(buffer,0,n);
            }

            fileOutputStream.close();
            bins.close();

            Log.i("DOWNLOADFILE", "Download over!");

            mHandler.obtainMessage(GetLinks.DONE,"").sendToTarget();

        }catch (Exception e){

            if(f!=null)
                f.delete();

            mHandler.obtainMessage(GetLinks.ERROR,e.toString()).sendToTarget();

            Log.i("DOWNLOADFILE","Error! : " + e.toString());
        }
    }
}
