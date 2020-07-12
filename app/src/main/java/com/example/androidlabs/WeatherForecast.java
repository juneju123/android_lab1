package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private TextView currentTempTxt;
    private TextView minTxt;
    private TextView maxTxt;
    private TextView uvTxt;
    private ImageView imageView;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        pb = findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        ForecastQuery req = new ForecastQuery();
        req.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
        currentTempTxt = findViewById(R.id.currentTemp);
        minTxt = findViewById(R.id.minTemp);
        maxTxt = findViewById(R.id.maxTemp);
        uvTxt = findViewById(R.id.uv);
        imageView = findViewById(R.id.currentWeatherImg);
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String min = null;
        private String max = null;
        private String currentTemp = null;
        private String iconName = null;
        private String uv;
        private Bitmap image = null;
        private String urlString = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(strings[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }else if(xpp.getName().equals("weather")){
                            iconName = xpp.getAttributeValue(null, "icon");
                            urlString = "http://openweathermap.org/img/w/" + iconName + ".png";
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
                URL imgUrl = new URL(urlString);
                urlConnection = (HttpURLConnection) imgUrl.openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                String fileName = iconName + ".png";
                if(fileExistance(fileName)){
                    FileInputStream fis = null;
                    try {fis = openFileInput(fileName);   }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();  }
                    image = BitmapFactory.decodeStream(fis);

                }else if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    publishProgress(100);
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                Log.i("File name: "+fileName, "if exists locally: " + fileExistance(fileName));
                URL uvUrl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                urlConnection = (HttpURLConnection) uvUrl.openConnection();
                // urlConnection.connect();
                InputStream uvResponse = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                float uvRating = (float)uvReport.getDouble("value");
                uv = Float.toString(uvRating);
            }
            catch (Exception e)
            {

            }

            return "Done";
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

        @Override
        public void onProgressUpdate(Integer...value){
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(value[0]);

        }
        @Override
        public void onPostExecute(String fromDoInBackground)
        {

            imageView.setImageBitmap(image);
//            currentTempTxt.setText(R.string.temp +currentTemp +"℃");
//            maxTxt.setText(R.string.max + max +"℃");
//            minTxt.setText(R.string.min + min +"℃");

//            uvTxt.setText(R.string.uv + uv);
            // currentTempTxt.setText(R.string.temp +currentTemp +"℃");
            maxTxt.setText("Max temperature is " + max +"℃");
            minTxt.setText("Minmum temperature is "+ min +"℃");

            uvTxt.setText("uv is " + uv);
            pb.setVisibility(View.INVISIBLE);


        }
    }
}