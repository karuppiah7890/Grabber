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
    URL url;
    String name;
    Link l;
    int position;

    public DownloadFile(Handler mHandler, int position) {

        this.mHandler = mHandler;
        l = AllDownloads.AllLinks.get(position);
        url = l.getUrl();
        name = l.getName();
        this.position = position;
    }

    @Override
    public void run() {

        File f = null;

        try {

            Log.i("DOWNLOADFILE", "Entering run()");

            f = new File(Environment.getExternalStorageDirectory() + "/Grabber/" + name);

            FileOutputStream fileOutputStream = new FileOutputStream(f);

            Log.i("DOWNLOADFILE", "outputstream works!");

            BufferedInputStream bins = new BufferedInputStream(url.openStream(),8192);

            byte[] buffer = new byte[1024];
            int n;

            l.setStatus(1);

            mHandler.obtainMessage(AllDownloads.UPDATE,position).sendToTarget();

            while ((n = bins.read(buffer,0,1024))!=-1){
                fileOutputStream.write(buffer,0,n);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            bins.close();

            l.setSize(f.length());
            l.setStatus(2);

            mHandler.obtainMessage(AllDownloads.UPDATE,position).sendToTarget();

            mHandler.obtainMessage(AllDownloads.DONE,"").sendToTarget();

            Log.i("DOWNLOADFILE", "Download over!");

        }catch (Exception e){

            if(f!=null)
                f.delete();

            l.error += e.toString();

            mHandler.obtainMessage(AllDownloads.ERROR,"").sendToTarget();

            Log.i("DOWNLOADFILE","Error! : " + e.toString());
        }
    }
}