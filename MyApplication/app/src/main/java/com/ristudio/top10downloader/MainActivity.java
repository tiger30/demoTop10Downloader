package com.ristudio.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //    private TextView xmlTextView;
    private String mFileContent;

    private Button btnParse;
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        xmlTextView = (TextView) findViewById(R.id.xmlTextView);
        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseApplication parseApplication = new ParseApplication(mFileContent);
                parseApplication.process();
            }
        });
        listApps = (ListView) findViewById(R.id.listApps);

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast toast = Toast.makeText(getApplicationContext(), "Why are you clicking setting?", Toast.LENGTH_LONG);//4fun
            toast.show();//4fun
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickButtonParse() {
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            mFileContent = downloadXMLFile(params[0]);
            if (mFileContent == null) {
                Log.d("DownloadData", "error downloading");
            }
            return mFileContent;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was " + result);
//            xmlTextView.setText(mFileContent);
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //
                int respone = connection.getResponseCode(); // 404 error or whatever
                Log.d("DownloadData", "The respone code was: " + respone);
                // define the input stream which is connected to the above 'connection'
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];//temporary buffer, allocating space at this stage to read 500 bytes at time
                while (true) {
                    charRead = isr.read(inputBuffer);
                    if (charRead <= 0) {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            } catch (IOException e) {
                Log.d("DownloadData", "IO Exception reading data" + e.getMessage());
//                e.printStackTrace();
            } catch (SecurityException e) {
                Log.d("DownloadData", "Security exception. Need permission?" + e.getMessage());
            }

            return null;
        }
    }

}
