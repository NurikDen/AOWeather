package com.example.aoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aocompany.aoweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private EditText edittext2;
    private TextView textview1;
    private TextView textview2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button);
        edittext2 = findViewById(R.id.vvod);
        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittext2.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this,R.string.userinputnothing,Toast.LENGTH_LONG).show();
                }
                else{
                    String cityname = edittext2.getText().toString().toLowerCase().replaceAll(" ", "");
                    String APIkey = "9f0f27754d14646e7a5e490ebbb595eb";
                    String URL = "https://api.openweathermap.org/data/2.5/weather?q=" +cityname+ "&appid=" +APIkey+ "&units=metric";

                    new APIData().execute(URL);}}});
    }
    private class APIData extends AsyncTask<String,String,String> {

        protected void onPreExecute(){
            super.onPreExecute();
            textview2.setText(R.string.Wait);

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connect = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connect =(HttpURLConnection) url.openConnection();
                connect.connect();
                InputStream stream = connect.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String l ="";
                while ((l= reader.readLine())!= null){
                    buffer.append(l).append("\n");
                }
              return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connect != null)
                      connect.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){

        super.onPostExecute(result);

            try {
                JSONObject json = new JSONObject(result);

                double c = json.getJSONObject("main").getDouble("temp");
                String cityname = edittext2.getText().toString().replaceAll(" ", "");
                int temp = (int) Math.round(c);

                double feelslike = json.getJSONObject("main").getDouble("feels_like");
                int feels = (int) Math.round(feelslike);

                double windspeed = json.getJSONObject("wind").getDouble("speed");
                int wind = (int) Math.round(windspeed);

                String description = String.valueOf(json.getJSONArray("weather").getJSONObject(0).getString("description"));

                int visibility = json.getInt("visibility");



                textview2.setText(getString(R.string.Temperaturein) + cityname +": "+ temp +"°C;" + "\n" + getString(R.string.feelslike)+ feels +"°C;"+"\n"+ getString(R.string.windspeed) + wind+" m/s;" +
                        "\n" + getString(R.string.visibility)+ visibility+ "m;"+ "\n" +description+".");
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                textview2.setText(R.string.notfound);

            }




        }
    }
}
