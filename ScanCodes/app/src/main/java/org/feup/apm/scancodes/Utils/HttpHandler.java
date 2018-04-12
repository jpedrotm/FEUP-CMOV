package org.feup.apm.scancodes.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {

    private static URL url;
    private static HttpURLConnection urlConnection = null;

    private static final String DOMAIN = "50bd9bfe.ngrok.io";

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }

    public static String customerRequest(String message) {
        String response = null;

        try {
            url = new URL("http://" + DOMAIN + "/order/save");
            System.out.println(url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(message);
            outputStream.flush();
            outputStream.close();

            // get response
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("CREATED");
                response = readStream(urlConnection.getInputStream());
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

}

