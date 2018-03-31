package cdt.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is used for downloading photos asynchronously
 * To use this class, inherit and override the onFinish method.
 * to start the asynch task, call the execute method with the url as a parameter
 */

public abstract class DownloadPhoto extends AsyncTask<String, Integer, Long> {

    protected Bitmap bm;

    protected Long doInBackground(String... urls) {
        bm = getImageBitmap(urls[0]);
        return Long.valueOf(1);
    }

    private static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("PHOTO_DOWNLOAD", "Error getting bitmap", e);
        }
        return bm;
    }

    // executes on finish send the resulting bitmap
    @Override
    public void onPostExecute(Long result){
        onFinish(bm);
    }

    // must implement this method to use the bm
    // bm is null if download was unsuccessful
    public abstract void onFinish(Bitmap result);

}


