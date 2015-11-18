package com.kruppiah.grabber;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetLinks extends Thread{

    String url;
    Handler mHandler;
    int count = 0;

    public GetLinks(Handler mHandler, String url)
    {
        this.url = url;
        this.mHandler = mHandler;
    }

    @Override
    public void run() {

        String exception = "";

        ArrayList<String> pdflinks = new ArrayList<String>();

        try {

            File f = new File(Environment.getExternalStorageDirectory() + "/Grabber/");

            f.mkdir();

            Document doc = Jsoup.connect(url).get();

            Log.i("GETLINKS","Connected to page!");

            Elements links = doc.select("a[href]");

            Log.i("GETLINKS","Total Number of Links : " + links.size());

            String hreflink = "";
            boolean b;

            for(int i=0;i<links.size();i++)
            {
                hreflink = links.get(i).attr("href");

                b = Pattern.matches(".*\\.pdf$", hreflink) || Pattern.matches(".*\\.PDF$", hreflink);
                Log.i("GETLINKS : ",hreflink);

                if(b)
                {
                    Log.i("GETLINKS", "pdf link");
                    pdflinks.add(hreflink);
                }
            }

            count = pdflinks.size();

            mHandler.obtainMessage(StartScreen.LINKS,count + "").sendToTarget();

            mHandler.obtainMessage(StartScreen.STATUS,"Gathering information about links...").sendToTarget();

            for(int i=0;i<pdflinks.size();i++)
            {
                hreflink = pdflinks.get(i);
                AllDownloads.AllLinks.add(new Link(url, hreflink));
            }

            mHandler.obtainMessage(StartScreen.DONE,"").sendToTarget();

        } catch (Exception e) {
            mHandler.obtainMessage(StartScreen.STATUS,e.toString()).sendToTarget();
        }

    }
}
