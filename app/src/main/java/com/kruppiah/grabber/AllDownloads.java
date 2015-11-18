package com.kruppiah.grabber;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class AllDownloads extends AppCompatActivity {

    public static ArrayList<Link> AllLinks;

    Toolbar toolbar;
    RecyclerView rv;
    LinkAdapter linkAdapter;

    int done = 0;
    int error = 0;
    int flag = 0;
    int positon = -1;
    int i = 0;

    DownloadFile df = null;

    public static final int DONE = 1;
    public static final int ERROR = 2;
    public static final int UPDATE = 3;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case DONE:

                    done++;
                    i++;

                    if(i<AllLinks.size())
                    {
                        df = new DownloadFile(mHandler,i);
                        df.start();
                    }

                    Log.i("GETLINKS", done + " Download(s) Done");

                    break;

                case ERROR :

                    error++;
                    i++;

                    if(i<AllLinks.size())
                    {
                        df = new DownloadFile(mHandler,i);
                        df.start();
                    }

                case UPDATE :
                    positon = (int) msg.obj;
                    linkAdapter.notifyItemChanged(positon);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_downloads);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        linkAdapter = new LinkAdapter(AllLinks);
        rv.setAdapter(linkAdapter);

        if(AllLinks.size()>0)
        {
            df = new DownloadFile(mHandler,i);
            df.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_downloads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
