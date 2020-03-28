package com.example.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.*;

import Client.Client;
import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private String input_url;
    private String person_id;
    private String auth_token;

    private Button sign_in;
    private Button register;

    private RadioButton male;
    private RadioButton female;
    private EditText server_host;
    private EditText server_port;
    private EditText user_name;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private EditText email;

    boolean male_selected;
    boolean female_selected;

    public LoginFragment() {
        // Required empty public constructor
        input_url = "";
        male_selected = false;
        female_selected = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        server_host = (EditText) view.findViewById(R.id.server_host_edit_text);
        server_port = (EditText) view.findViewById(R.id.server_port_edit_text);
        user_name = (EditText) view.findViewById(R.id.username_edit_text);
        password = (EditText) view.findViewById(R.id.password_edit_text);
        first_name = (EditText) view.findViewById(R.id.first_name_edit_text);
        last_name = (EditText) view.findViewById(R.id.last_name_edit_text);
        email = (EditText) view.findViewById(R.id.email_edit_text);
        sign_in = (Button) view.findViewById(R.id.sign_in_button);
        register = (Button) view.findViewById(R.id.register_button);
        register.setEnabled(false);
        male = (RadioButton)view.findViewById(R.id.radio_male);
        female = (RadioButton)view.findViewById(R.id.radio_female);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               male_selected = true;
               female_selected = false;
               if (checkRegButton())
                   register.setEnabled(true);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female_selected = true;
                male_selected = false;
                if (checkRegButton())
                    register.setEnabled(true);
            }
        });

        return view;
    }

    private boolean checkRegButton()
    {
        if (!TextUtils.isEmpty(server_port.getText()) && !TextUtils.isEmpty(server_host.getText()) && !TextUtils.isEmpty(user_name.getText()) &&
                !TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(first_name.getText()) && !TextUtils.isEmpty(last_name.getText()) && !TextUtils.isEmpty(email.getText()))
        {
            return true;
        }
        return false;
    }

    private void attemptSignIn() {

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

    private void attemptRegister()
    {
        if (checkRegButton())
        {
            try
            {
                input_url = "http://" + server_host.getText().toString() + ":" + server_port.getText().toString();
                RegisterTask task = new RegisterTask();
                task.execute(new URL(input_url));
            }
            catch (MalformedURLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private class LoginTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            JSONObject output = null;
            Client client = new Client(input_url);
            String reqData = "{" + "\"userName\": \"" + user_name.getText().toString() + "\",\"password\": \"" + password.getText().toString() + "\"" + "}";
            output = client.makePostRequest("/user/login", reqData);
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
            client.setAuth(auth_token);
            output = client.makePersonRequest("/person/" + person_id);
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result)
        {
            String associated_username = "";
            String person_id = "";
            String first_name = "";
            String last_name = "";
            String gender = "";
            String father_id = "";
            String mother_id = "";
            String spouse_id = "";
            try
            {
                associated_username = result.get("associatedUsername").toString();
                person_id = result.get("personID").toString();
                first_name = result.get("firstName").toString();
                last_name = result.get("lastName").toString();
                gender = result.get("gender").toString();
                father_id = result.get("fatherID").toString();
                mother_id = result.get("motherID").toString();
                spouse_id = result.get("spouseID").toString();

                Person person = new Person();
                person.setAssociatedUsername(associated_username);
                person.setPersonID(person_id);
                person.setFirstName(first_name);
                person.setLastName(last_name);
                person.setGender(gender);
                person.setFatherID(father_id);
                person.setMotherID(mother_id);
                person.setSpouseID(spouse_id);

                Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Welcome " + first_name + " " + last_name, Toast.LENGTH_LONG);
                toast.show();
                MapFragment map_fragment = new MapFragment(person,auth_token);
                FragmentManager fragment_manager = getChildFragmentManager();
                fragment_manager.beginTransaction().add(R.id.container, map_fragment).commit();
            }
            catch(Exception  e)
            {
                e.getMessage();
            }
        }
    }

    private class RegisterTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            String gender = "";
            if (male_selected)
            {
                gender = "m";
            }
            else
            {
                gender = "f";
            }
            JSONObject output = null;
            Client client = new Client(input_url);
            String reqData = "{" + "\"userName\": \"" + user_name.getText().toString() + "\",\"password\": \"" + password.getText().toString() + "\",\"email\": \"" + email.getText().toString() + "\",\"firstName\": \"" + first_name.getText().toString() + "\",\"lastName\": \"" + last_name.getText().toString() + "\",\"gender\": \"" + gender + "\"}";
            output = client.makePostRequest("/user/register", reqData);
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result)
        {
            String success = "";
            try
            {
                success = result.get("success").toString();
            }
            catch(Exception e)
            {
                e.getMessage();
            }
            if (success == "true")
            {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Welcome " + first_name.getText().toString() + " " + last_name.getText().toString(), Toast.LENGTH_LONG);
                toast.show();
                try
                {
                    LoginTask login_task = new LoginTask();
                    input_url = "http://" + server_host.getText().toString() + ":" + server_port.getText().toString();
                    login_task.execute(new URL(input_url));
                }
                catch(Exception e)
                {
                    e.getMessage();
                }

            }
            else
            {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Failed. Username already exists", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
