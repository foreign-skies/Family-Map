package com.example.familymap;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
import java.net.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button sign_in;
    private EditText server_host;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        server_host = (EditText) view.findViewById(R.id.server_host_edit_text);
        sign_in = (Button) view.findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        return view;
    }

    public void attemptSignIn() {
        System.out.println("this happened");
        // System.out.println(server_host.getText().toString());
        try
        {
            RequestTask task = new RequestTask();
            task.execute(new URL("https://localhost:8080/user/login"));
        }
        catch (MalformedURLException e)
        {
            System.out.println(e.getMessage());
        }

//        try {
//            URL url = new URL("https://localhost:8080/user/login");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod("POST");
//            http.setDoOutput(true);
//            http.addRequestProperty("Accept", "application/json");
//            http.connect();
//            String reqData =
//                    "{" + "\"userName\": \"susan\"password\": \"mysecret\"" + "}";
//            OutputStream reqBody = http.getOutputStream();
//            writeString(reqData, reqBody);
//            reqBody.close();
//            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                System.out.println("Route successfully claimed.");
//            } else {
//                System.out.println("ERROR: " + http.getResponseMessage());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private class RequestTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls)
        {
            long output = 0;
            try {
                URL url = new URL("http://192.168.1.142:8080/user/login");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.addRequestProperty("Accept", "application/json");
                http.connect();
                String reqData =
                        "{" + "\"userName\": \"mbrann\",\"password\": \"leonandada\"" + "}";
                OutputStream reqBody = http.getOutputStream();
                writeString(reqData, reqBody);
                reqBody.close();
                if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    System.out.println("Route successfully claimed.");
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

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {


        }
    }
}
