package org.feup.cmov.acmecoffee.Utils;

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

    private static final String DOMAIN = "4fea0ef4.ngrok.io";

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

    public static String insertNewCustomer(String message) {

        String customerInfo = null;

        Log.d("REQUEST", message);

        try {
            url = new URL("http://" +DOMAIN + "/customer/save");
            System.out.println(url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(message);
            outputStream.flush();
            outputStream.close();

            // get response
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_CREATED) {
                customerInfo = readStream(urlConnection.getInputStream());
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return customerInfo;
    }

    public static String loginCustomer(String message) {
        String response = null;

        Log.d("REQUEST", message);

        try {
            url = new URL("http://" + DOMAIN + "/customer/login");
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

            if(responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                response = readStream(urlConnection.getInputStream());
                System.out.println(response);
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

    public static String getAllItems() {
        String response = null;

        try {
            url = new URL("http://" + DOMAIN + "/item/findall");
            System.out.println(url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            // get response
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
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

    public static String getCustomerVouchers(Long id) {
        String response = null;

        try {
            url = new URL("http://" + DOMAIN + "/customer/vouchers/" + id);
            System.out.println(url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            // get response
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
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
