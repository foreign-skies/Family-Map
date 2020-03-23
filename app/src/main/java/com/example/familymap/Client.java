package com.example.familymap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client
{
    private String server_host;
    private int server_port;

    public Client(String host, int port)
    {
        server_host = host;
        server_port = port;
    }

    public JSONObject makeRequest(String url_extension, String req_data)
    {
        JSONObject output = null;

        try {
            URL url = new URL(server_host + server_port + url_extension);
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

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
