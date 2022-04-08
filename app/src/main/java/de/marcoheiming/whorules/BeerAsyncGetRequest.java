package de.marcoheiming.whorules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeerAsyncGetRequest extends AsyncTask<Integer, Void, List<Beer>> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECTION_TIMEOUT = 5000;

    public BeerAsyncGetRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected List<Beer> doInBackground(Integer... ints) {

        String response = null;
        List<Beer> result = new ArrayList<Beer>();
        try {
            URL url = new URL(BeersListActivity.BEER_API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            response = stringBuilder.toString();

            if (!response.isEmpty()) {
                JSONArray beersJsonArray = new JSONArray(response);
                if (beersJsonArray.length() > 0) {
                    for (int i = 0; i < beersJsonArray.length(); i++) {
                        JSONObject b = beersJsonArray.getJSONObject(i);
                        Beer beer = new Beer(this.context, b);
                        result.add(beer);
                    }
                }
            } else {
                resetApiURL();
            }

        } catch (IOException | JSONException e) {
            result = null;
            e.printStackTrace();
            resetApiURL();
        }

        return result;
    }

    private void resetApiURL() {
        // reset API URL
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", this.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BEER_API_URL", "");
        editor.apply();
    }

    @Override
    protected void onPostExecute(List<Beer> result) {
        super.onPostExecute(result);
    }


}
