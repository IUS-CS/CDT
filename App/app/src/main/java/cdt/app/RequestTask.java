package cdt.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is used for sending server requests asynchronously
 * To use this class, inherit and override the onFinish method.
 * to start the asynch task, call the execute method with the method as a parameter
 *
 */

public abstract class RequestTask extends AsyncTask<String, Integer, Long> {

    protected Long doInBackground(String... urls) {
        int responseCode;
        try {
            responseCode =  request(urls[0], urls[1]);
        } catch (IOException e) {
            responseCode = 400; // indicate generic response for error
        }
        return Long.valueOf(responseCode);
    }

    // make a request to the server. returns the response code of the request
    private int request(String method, String url) throws IOException{

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod(method);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "";

        // Send post request
        if(method.equals("POST") || method.equals("DELETE")) {
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        }

        return con.getResponseCode();
    }

    // executes on finish send the resulting bitmap
    // override to get the result
    @Override
    public void onPostExecute(Long result){
        return;
    }


}


