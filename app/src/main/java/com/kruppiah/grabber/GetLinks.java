package com.kruppiah.grabber;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetLinks extends Thread{

    String url;
    Handler startscreenHandler;
    int count = 0;
    int done = 0;
    int error = 0;
    int flag = 0;

    public static final int DONE = 1;
    public static final int ERROR = 2;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DONE:

                    done++;

                    Log.i("GETLINKS",done + " Download(s) Done");

                    startscreenHandler.obtainMessage(StartScreen.DONE,"So many done : " + done).sendToTarget();

                    if((done+error)==count)
                    {
                        startscreenHandler.obtainMessage(StartScreen.DONE,"Download Done!").sendToTarget();
                        flag=1;
                    }

                    break;

                case ERROR :

                        error++;

                        if((done+error)==count) {
                            startscreenHandler.obtainMessage(StartScreen.DONE,"Error : " + (String)msg.obj + "\n\nDownload Done!").sendToTarget();
                            flag=1;
                            break;
                        }

                        else
                        {
                            startscreenHandler.obtainMessage(StartScreen.DONE,"Error : " + (String)msg.obj).sendToTarget();
                            break;
                        }
            }
        }
    };

    public GetLinks(Handler startscreenHandler, String url)
    {
        this.url = url;
        this.startscreenHandler = startscreenHandler;
    }

    @Override
    public void run() {

        try {

            File f = new File(Environment.getExternalStorageDirectory() + "/Grabber/");

            f.mkdir();

            Document doc = Jsoup.connect(url).get();

            Elements links = doc.select("a[href]");

            ArrayList<String> filenames = new ArrayList<String>();

            for(int i=0;i<links.size();i++)
            {
                String link = links.get(i).attr("href");

                boolean b = Pattern.matches(".*\\.pdf$", link);

                if(b)
                filenames.add(link);
            }


            count = filenames.size();

            startscreenHandler.obtainMessage(StartScreen.LINKS,count + "").sendToTarget();

            for(int i=0;i<filenames.size();i++)
            {
                String link = filenames.get(i);

                String tokens[] = link.split("/");

                String name = tokens[tokens.length-1];

                Log.i("GETLINKS","Link : " + link);

                boolean b = Pattern.matches("^http://.*", link) ||  Pattern.matches("^https://.*", link) || Pattern.matches("^ftp://.*", link);

                DownloadFile df =  null;

                if (b)
                {
                    df = new DownloadFile(mHandler,link,name);
                }

                else
                {

                    df = new DownloadFile(mHandler,url+"/"+link,name);
                }

                df.start();

                df.join();
            }

            Log.i("GETLINKS","Waiting ...");

            //while(flag==0);

        } catch (Exception e) {
            startscreenHandler.obtainMessage(StartScreen.DONE,e.toString()).sendToTarget();
        }

    }
}
