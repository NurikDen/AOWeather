package aoweather.aocompany.aoweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private TextView citynametext,temperaturetext,visibilitytext,windspeedtext,feelsliketext,descriptiontext;
    private ImageView weather,visibilityimage,windspeedimage,aoweatherimage;
    private CardView card;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        button1 = findViewById(R.id.button2);
        edittext2 = findViewById(R.id.vvod);
        citynametext = findViewById(R.id.cityname);
        temperaturetext = findViewById(R.id.temperature);
        visibilitytext= findViewById(R.id.visibilitytext);
        windspeedtext = findViewById(R.id.windspeedtext);
        feelsliketext = findViewById(R.id.feelslike);
        weather = findViewById(R.id.weather);
        visibilityimage = findViewById(R.id.visibility);
        windspeedimage = findViewById(R.id.windspeed);
        descriptiontext = findViewById(R.id.description);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittext2.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this,R.string.userinputnothing,Toast.LENGTH_LONG).show();
                }
                else{
                    String cityname = edittext2.getText().toString().toLowerCase().replaceAll(" ", "");
                    String APIkey = "9f0f27754d14646e7a5e490ebbb595eb";
                    String URL = "https://api.openweathermap.org/data/2.5/weather?q=" +cityname+ "&appid=" +APIkey+ "&units=metric"+"&"+getString(R.string.langname);

                    new APIData().execute(URL);}}});
    }
    @SuppressLint("StaticFieldLeak")
    private class APIData extends AsyncTask<String,String,String> {

        protected void onPreExecute(){
            super.onPreExecute();
            windspeedtext.setText(R.string.Wait);
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

                if (description.equals(getString(R.string.oblachno))) {
                    weather.setImageResource(R.drawable.overcast_clouds);
                }
                else if(description.equals(getString(R.string.sneg)) || description.equals(getString(R.string.sneg1))) {
                    weather.setImageResource(R.drawable.snow);
                }
                else if(description.equals(getString(R.string.nebo1))||description.equals(getString(R.string.nebo2)) || description.equals(getString(R.string.nebo3))) {
                    weather.setImageResource(R.drawable.sun);
                }
                else if (description.equals(getString(R.string.pasmurno1)) || description.equals(getString(R.string.pasmurno2)) || description.equals(getString(R.string.pasmurno3))) {
                    weather.setImageResource(R.drawable.few_clouds);
                }
                else if (description.equals(getString(R.string.tuman1)) || description.equals(getString(R.string.tuman2)) || description.equals(getString(R.string.tuman3))) {
                    weather.setImageResource(R.drawable.mist);
                }
                else {
                    weather.setImageResource(R.drawable.sun);
                }
                temperaturetext.setText(Integer.toString(temp)+"°C");
                weather.setVisibility(View.VISIBLE);
                visibilityimage.setVisibility(View.VISIBLE);
                windspeedimage.setVisibility(View.VISIBLE);
                feelsliketext.setText("("+Integer.toString(feels)+"°C"+")");
                windspeedtext.setText(Integer.toString(wind)+"m/s");
                visibilitytext.setText(Integer.toString(visibility)+"m");
                citynametext.setText(cityname);
                descriptiontext.setText(description);


            } catch (JSONException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                citynametext.setText(R.string.notfound);
            }
        }
    }

}
