package com.example.familymap;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.*;
import java.net.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private String input_url;
    private String person_id;
    private String auth_token;

    private Button sign_in;
    private EditText server_host;
    private EditText server_port;
    private EditText user_name;
    private EditText password;

    public LoginFragment() {
        // Required empty public constructor
        input_url = "";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        server_host = (EditText) view.findViewById(R.id.server_host_edit_text);
        server_port = (EditText) view.findViewById(R.id.server_port_edit_text);
        user_name = (EditText) view.findViewById(R.id.username_edit_text);
        password = (EditText) view.findViewById(R.id.password_edit_text);
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
            input_url = "http://" + server_host.getText().toString() + ":" + server_port.getText().toString();
            LoginTask task = new LoginTask();
            task.execute(new URL(input_url));
        }
        catch (MalformedURLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    private class LoginTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            JSONObject output = null;
            Client client = new Client(input_url);
            String reqData = "{" + "\"userName\": \"" + user_name.getText().toString() + "\",\"password\": \"" + password.getText().toString() + "\"" + "}";
            output = client.makeLoginRequest("/user/login", reqData);
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result)
        {
            String thing = "";
            String success = "";
            String first_name = "";
            String last_name = "";
            try
            {
                thing = result.get("userName").toString();
                person_id = result.get("personID").toString();
                auth_token = result.get("authToken").toString();
                success = result.get("success").toString();
            }
            catch(Exception  e)
            {
                e.getMessage();
            }

            if (success == "true")
            {
                try {
                    GetUserTask user_task = new GetUserTask();
                    input_url = "http://" + server_host.getText().toString() + ":" + server_port.getText().toString();
                    user_task.execute(new URL(input_url));
                }
                catch(Exception e)
                {
                    e.getMessage();
                }
            }
            else
            {
                Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Login Failed", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private class GetUserTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            JSONObject output = null;
            Client client = new Client(input_url);
            String reqData = "{" + "\"userName\": \"" + user_name.getText().toString() + "\",\"password\": \"" + password.getText().toString() + "\"" + "}";
            client.setAuth(auth_token);
            output = client.makePersonRequest("/person/" + person_id, reqData);
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result)
        {
            String first_name = "";
            String last_name = "";
            try
            {
                first_name = result.get("firstName").toString();
                last_name = result.get("lastName").toString();
                Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Welcome " + first_name + " " + last_name, Toast.LENGTH_LONG);
                toast.show();
            }
            catch(Exception  e)
            {
                e.getMessage();
            }
        }
    }
}
