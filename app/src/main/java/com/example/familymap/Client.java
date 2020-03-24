package com.example.familymap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client
{
    private String base_url;
    private String auth_token;

    public Client(String url_in)
    {
        base_url = url_in;
    }

    public void setAuth(String auth_in)
    {
        auth_token = auth_in;
    }

    public JSONObject makePostRequest(String url_extension, String req_data)
    {
        JSONObject output = null;

        try {
            URL url = new URL(base_url + url_extension);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            writeString(req_data, reqBody);
            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                System.out.println("Route successfully claimed.");
                try
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(http.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                    {
                        content.append(inputLine);
                    }
                    in.close();
                    output = new JSONObject(content.toString());
                }

                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e)
        {
            e.getMessage();
        }


        return output;
    }


    public JSONObject makePersonRequest(String url_extension, String req_data)
    {
        JSONObject output = null;

        try {
            URL url = new URL(base_url + url_extension);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", auth_token);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                try {
                    output = new JSONObject(respData.toString());
                }
                catch(Exception e)
                {
                    e.getMessage();
                }
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return output;
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
