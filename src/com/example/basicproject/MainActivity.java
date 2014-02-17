package com.example.basicproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.widget.EditText;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends Activity {
	public TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.stockOutput);
        final EditText input = (EditText) findViewById(R.id.stockInput);
        final Button findButton = (Button) findViewById(R.id.stockButton);
        
        final Button goButton = (Button) findViewById(R.id.goButton);
        
        goButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				try {
					String address = getResources().getString(R.string.text_uri);//text.getText().toString();
					Intent uriIntent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(address));
					startActivity(uriIntent);
				} catch (Exception e) {
					Log.e("Uri", e.toString());
				}
				
			}});
        
        findButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String stock = input.getText().toString();
					String uri = getResources().getString(R.string.stock_api)+stock;
					Log.i("URI value",uri);
					new Connect().execute(uri);
					
				} catch (Exception e) {
					Log.e("Uri", e.toString());
				}
			}
		});
        
    }

    private class Connect extends AsyncTask<String, Void, String>{
    	@Override
    	protected String doInBackground(String... url)
        {

            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(url[0]); 

            HttpResponse response;
            try {
                response = httpclient.execute(httpget);
                Log.i("HTTP Response",response.getStatusLine().toString());

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    InputStream instream = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                    	if (line.contains("\"l\""))
                        	sb.append(line + "\n");
                    }
                    instream.close();
                    String result = sb.toString();
                    result = result.substring(8, result.length()-2);
                    result = "Stock value : "+result;
                    return result;
                    
                }
                
            } catch (Exception e) {Log.e("HTTP Connection", e.toString());}
    		return "Invalid stock name!";
        }
    	
    	@Override
    	protected void onPostExecute(String result) {
    	output.setText(result);
        }

    }
    
}


