package com.kruppiah.grabber;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class StartScreen extends AppCompatActivity {

    private Toolbar toolbar;

    EditText etURL;
    Button bGet;
    TextView tvStatus,tvNoLinks;
    ProgressBar progressBar;

    Intent i;
    public static final int LINKS = 1,STATUS = 2,DONE = 3;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case LINKS:

                    tvNoLinks.setText("Number of Links found : " + (String) msg.obj);
                    bGet.setEnabled(true);

                    break;

                case STATUS:

                    tvStatus.setText((String)msg.obj);

                    break;

                case DONE :

                    startActivity(i);
                    progressBar.setVisibility(View.GONE);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        etURL = (EditText) findViewById(R.id.etURL);
        bGet = (Button) findViewById(R.id.bGet);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvNoLinks = (TextView) findViewById(R.id.tvNoLinks);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        i = new Intent(this,AllDownloads.class);

        AllDownloads.AllLinks = new ArrayList<Link>();

        bGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = etURL.getText().toString();

                if(url.equals(""))
                    return;

                boolean b = Pattern.matches("^http://.*", url) ||  Pattern.matches("^https://.*", url) || Pattern.matches("^ftp://.*", url);

                if(!b)
                {
                    Toast.makeText(StartScreen.this,"Please add http/https/ftp to the link",Toast.LENGTH_SHORT).show();
                    return;
                }

                b = Pattern.matches(".*/$", url);

                if(b)
                {
                    String newurl = url.substring(0,url.length()-1);
                    url = newurl;
                }


                GetLinks getLinks = new GetLinks(mHandler,url);

                getLinks.start();

                progressBar.setVisibility(View.VISIBLE);

                bGet.setEnabled(false);

                tvStatus.setText("Working...");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
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
