package com.example.tempdictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button search;
    EditText wordsearch;
    TextView Result;
    String words,deff,examples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search=(Button)findViewById(R.id.Search);
        wordsearch=(EditText)findViewById(R.id.enterWord);
        Result=(TextView)findViewById(R.id.ResultShow);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CallbackTask t=new CallbackTask();
                new CallbackTask().execute(dictionaryEntries());
                words=t.definitionSender();



            }
        });

    }




    private String dictionaryEntries() {
        final String language = "en-gb";
        final String word =wordsearch.getText().toString() ;
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            final String app_id = "fa0631c4";
            final String app_key = "1fe91dd55df9f38a5f7a062c8e245e6";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();



                return e.toString();

            }




        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject js = new JSONObject(result);
                JSONArray results = new JSONArray("results");

                JSONObject lEntries =results.getJSONObject(0);
                JSONArray laArray =lEntries.getJSONArray("lexicalEntries");
                JSONObject entries =laArray.getJSONObject(0);
                JSONArray e= entries.getJSONArray("entries");
                JSONObject jsonObject= e.getJSONObject(0);
                JSONArray sensesArray=jsonObject.getJSONArray("senses");
                JSONObject defi= sensesArray.getJSONObject(0);
                JSONArray definition= defi.getJSONArray("definitions");
                deff=definition.getString(0);





                JSONObject example = sensesArray.getJSONObject(0);
                JSONArray defiExample =example.getJSONArray("examples");
                JSONObject examp= defiExample.getJSONObject(0);
                JSONArray example2 = examp.getJSONArray("definitions");
                examples=example2.getString(0);



            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }




        }

        public String definitionSender()
        {
            String p;
            p=deff;


            return (p);


        }
        public String exampleReturn()
        {
            String p2;
            p2=examples;
            return p2;
        }

}





}

