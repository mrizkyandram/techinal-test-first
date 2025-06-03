package com.test_obs.inventoryms;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Common {
    public String sendRequestHTTPWithBody(String pUrl, JSONObject headers, JSONObject body) {
        StringBuffer response = new StringBuffer();
        int tTimeout = 60000;
        try {
            //log.info("Initiating Connection to: {}", pUrl);

            URL url = new URL(pUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(tTimeout);
            con.setConnectTimeout(tTimeout);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // Log headers
            //STREAM.info("Request Headers: {}", headers.toString());

            // Set custom headers
            for (String key : headers.keySet()) {
                con.setRequestProperty(key, headers.getString(key));
            }

           // STREAM.info("Connected to: {} with Path: {}", url.getHost(), url.getPath());

            // Log and write the request body
           // STREAM.info("Request Body: {}", body.toString());
            try (OutputStream os = con.getOutputStream()) {
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Get Response Code
            int responseCode = con.getResponseCode();
            //STREAM.info("Response Code for HTTP Connection: {}", responseCode);

            // Get Response Data
            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? con.getInputStream() : con.getErrorStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            //STREAM.info("Response: {}", response);

            return response.toString();

        } catch (SocketTimeoutException ste) {
          //  TRACE.info("Socket Timeout Exception: {}", ste.getMessage());  // Log only the message
        } catch (IOException e) {
          //  TRACE.info("IOException while getting response. Error: {}", e.getMessage());  // Log only the message
        } catch (NullPointerException e) {
          //  TRACE.info("NullPointerException while getting response code. Error: {}", e.getMessage());  // Log only the message
        }

        return response.toString();
    }
}
