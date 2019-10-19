package com.pddstudio.highlightjs.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */

public class FileUtils {

    private static String LOG = "FileUtilsHighlightjsLib";

    public interface Callback {
        void onDataLoaded(boolean success, String source);
    }

    public static String loadSourceFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file), 16384)) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            Log.e(LOG, Objects.requireNonNull(e.getMessage())); // TODO: really?
            return null;
        }
    }

    public static void loadSourceFromUrl(Callback callback, URL url) {
        new NetworkLoader(callback, url).execute();
    }

    private static class NetworkLoader extends AsyncTask<Void, Void, String> {

        private final Callback callback;
        private final URL url;

        private NetworkLoader(Callback callback, URL url) {
            this.callback = callback;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            URLConnection urlConnection;
            try {
                urlConnection = url.openConnection();
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 16384)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append(System.lineSeparator());
                    }
                    return stringBuilder.toString();
                } catch (IOException e) {
                    Log.e(LOG, Objects.requireNonNull(e.getMessage())); // TODO: really?
                    return null;
                }
            } catch (IOException e) {
                Log.e(LOG, Objects.requireNonNull(e.getMessage())); // TODO: really?
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            callback.onDataLoaded(false, null);
        }

        @Override
        protected void onPostExecute(String s) {
            callback.onDataLoaded(s != null, s);
        }
    }
}
