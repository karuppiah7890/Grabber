package com.kruppiah.grabber;

import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class Link {
    private URL url;
    private String name;
    private long size = -10;
    private long downloaded;
    private int status;
    String error;

    public Link(String mainurl, String link) {
        String tokens[] = link.split("/");

        name = tokens[tokens.length - 1];

        name = name.replace("%20","_");

        error = "";

        boolean b = Pattern.matches("^http://.*", link) || Pattern.matches("^https://.*", link) || Pattern.matches("^ftp://.*", link);

        Log.i("LINK","Making a link!");

        try {
            if (b) {
                url = new URL(link);
                Log.i("LINK",link);
            } else {
                url = new URL(mainurl + "/" + link);
                Log.i("LINK",mainurl+"/"+link);
            }

            status = 0;

            URLConnection connection = this.url.openConnection();

            connection.connect();

            size = connection.getContentLength();

            Log.i("Link ","" + size);

        } catch (Exception e) {
            error = e.toString();
        }
    }

    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatus() {

        if(status == 0)
            return "Waiting";

        else if (status == 1)
        return "Downloading";

        else return "Downloaded";
    }

    public void setStatus(int i)
    {
        status = i;
    }
}
