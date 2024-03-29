package com.example.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.UI.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Client.Client;
import Model.Event;
import Model.Person;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Person user;
    private ArrayList<Event> event_list = new ArrayList<Event>();
    private String auth_token;
    private HashMap<String, Integer> event_color_map = new HashMap<String,Integer>();
    private String passing_person_id;
    private Person current_person;
    private Event current_earliest_event;
    private List<Polyline> polyline_list;
    List<String> father_side;
    List<String> mother_side;

    private ImageView gender_image;
    private TextView event_text_view;


    public MapFragment(Person user_in, String auth_token_in) {
        user = user_in;
        auth_token = auth_token_in;
        passing_person_id = null;
        current_person = new Person();
        current_earliest_event = new Event();
        polyline_list = new ArrayList<Polyline>();

        father_side = new ArrayList<String>();
        mother_side = new ArrayList<String>();
        father_side.add(user.getPersonID());
        mother_side.add(user.getPersonID());
        passing_person_id = user.getPersonID();
        getPerson();
        Person local_person = new Person();
        local_person.setSpouseID(current_person.getSpouseID());
        local_person.setMotherID(current_person.getMotherID());
        local_person.setFatherID(current_person.getFatherID());
        local_person.setGender(current_person.getGender());
        local_person.setLastName(current_person.getLastName());
        local_person.setFirstName(current_person.getFirstName());
        local_person.setPersonID(current_person.getPersonID());

        while (current_person.getFatherID() != null)
        {
            passing_person_id = current_person.getFatherID();
            getPerson();
            father_side.add(current_person.getPersonID());
        }
        current_person = local_person;
        while (current_person.getMotherID() != null)
        {
            passing_person_id = current_person.getMotherID();
            getPerson();
            mother_side.add(current_person.getPersonID());
        }
        System.out.println(father_side.size());
        System.out.println(mother_side.size());

    }

    GoogleMap map;

    private class GetPersonTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            JSONObject output = null;
            Client client = new Client("http://192.168.1.142:8080");
            client.setAuth(auth_token);
            output = client.makePersonRequest("/person/" + passing_person_id);
            String associated_username = "";
            String person_id = "";
            String first_name = "";
            String last_name = "";
            String gender = "";
            String father_id = "";
            String mother_id = "";
            String spouse_id = "";
            try {
                associated_username = output.get("associatedUsername").toString();
                person_id = output.get("personID").toString();
                first_name = output.get("firstName").toString();
                last_name = output.get("lastName").toString();
                gender = output.get("gender").toString();
                try{father_id = output.get("fatherID").toString();}
                catch(Exception e)
                {
                    father_id = null;
                }
                try{mother_id = output.get("motherID").toString();}
                catch(Exception e)
                {
                    mother_id = null;
                }
                try{spouse_id = output.get("spouseID").toString();}
                catch(Exception e)
                {
                    spouse_id = null;
                }

                current_person.setAssociatedUsername(associated_username);
                current_person.setPersonID(person_id);
                current_person.setFirstName(first_name);
                current_person.setLastName(last_name);
                current_person.setGender(gender);
                current_person.setFatherID(father_id);
                current_person.setMotherID(mother_id);
                current_person.setSpouseID(spouse_id);

            }
            catch (Exception e) {
                e.getMessage();
            }
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result) {

        }

    }

    private void getEvents()
    {
        try
        {
            GetEventsTask task = new GetEventsTask();
            task.execute(new URL("http://192.168.1.142:8080")).get();
        }
        catch(Exception e)
        {
            e.getMessage();
        }

    }

    private void getPerson()
    {
        try
        {
            GetPersonTask task = new GetPersonTask();
            task.execute(new URL("http://192.168.1.142:8080")).get();
        }
        catch(Exception e)
        {
            e.getMessage();
        }
    }



    private class GetEventsTask extends AsyncTask<URL, Integer, JSONObject> {

        protected JSONObject doInBackground(URL... urls)
        {
            JSONObject output = null;
            Client client = new Client("http://192.168.1.142:8080");
            client.setAuth(auth_token);

            output = client.makePersonRequest("/event");

            return output;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                JSONArray array = result.getJSONArray("data");
                for(int i = 0 ; i < array.length() ; i++){
                    Event add_event = new Event();
                    add_event.setEventID(array.getJSONObject(i).getString("eventID"));
                    add_event.setAssociatedUsername(array.getJSONObject(i).getString("associatedUsername"));
                    add_event.setPersonID(array.getJSONObject(i).getString("personID"));
                    add_event.setLatitude(array.getJSONObject(i).getDouble("latitude"));
                    add_event.setLongitude(array.getJSONObject(i).getDouble("longitude"));
                    add_event.setCountry(array.getJSONObject(i).getString("country"));
                    add_event.setCity(array.getJSONObject(i).getString("city"));
                    add_event.setEventType(array.getJSONObject(i).getString("eventType"));
                    add_event.setYear(array.getJSONObject(i).getInt("year"));

                    //begin filters
                    boolean dont_add = false;
                    if (SettingsBase.getFatherSide() == true)
                    {
                        for (int j = 0; j < father_side.size(); j++)
                        {
                            if (father_side.get(j).equals(add_event.getPersonID()))
                            {
                                dont_add = false;
                                break;
                            }
                            dont_add = true;
                        }
                    }
                    if (SettingsBase.getMotherSide() == true)
                    {
                        for (int j = 0; j < mother_side.size(); j++)
                        {
                            if (mother_side.get(j).equals(add_event.getPersonID()))
                            {
                                dont_add = false;
                                break;
                            }
                            dont_add = true;
                        }
                    }
                    if (SettingsBase.getFemaleEvents() == true)
                    {
                        passing_person_id = add_event.getPersonID();
                        getPerson();
                        if (current_person.getGender().equals("m"))
                        {
                            dont_add = true;
                        }
                    }

                    if (SettingsBase.getMaleEvents() == true)
                    {
                        passing_person_id = add_event.getPersonID();
                        getPerson();
                        if (current_person.getGender().equals("f"))
                        {
                            dont_add = true;
                        }
                    }

                    if (dont_add == false)
                        event_list.add(add_event);
                }
            }
            catch(Exception  e)
            {
                e.getMessage();
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gender_image = (ImageView)view.findViewById(R.id.gender_image);
        event_text_view = (TextView) view.findViewById(R.id.map_event_text);
        event_text_view.setText("Click on a marker to see event details");
        event_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), PersonActivity.class);
                startActivity(i);
            }
        });
        Person local_person = new Person();
        local_person.setSpouseID(current_person.getSpouseID());
        local_person.setMotherID(current_person.getMotherID());
        local_person.setFatherID(current_person.getFatherID());
        local_person.setGender(current_person.getGender());
        local_person.setLastName(current_person.getLastName());
        local_person.setFirstName(current_person.getFirstName());
        local_person.setPersonID(current_person.getPersonID());
        local_person.setAssociatedUsername(current_person.getAssociatedUsername());
        getEvents();
        current_person = local_person;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        int hue_counter = 0;
        for (int i = 0; i < event_list.size(); i++)
        {
            float hue = 120;  //(Range: 0 to 360)
            if (hue + hue_counter > 359)
            {
                hue_counter = 0;
            }
            if (event_color_map.containsKey(event_list.get(i).getEventType().toLowerCase()))
            {
                    hue = event_color_map.get(event_list.get(i).getEventType().toLowerCase());
            }
            else
            {
                event_color_map.put(event_list.get(i).getEventType().toLowerCase(), hue_counter);
                hue = hue_counter;
                hue_counter += 24;
            }

            LatLng event_loc = new LatLng(event_list.get(i).getLatitude(),event_list.get(i).getLongitude());

            map.addMarker(new MarkerOptions().position(event_loc).title(event_list.get(i).getEventID()).icon(BitmapDescriptorFactory
                        .defaultMarker(hue)));

            map.moveCamera(CameraUpdateFactory.newLatLng(event_loc));

           map.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);


        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(Polyline line : polyline_list)
        {
            line.remove();
        }
        polyline_list.clear();

        String marker_name = marker.getTitle();
        String output_string = "";
        Event selected_event = null;
        for(int i = 0 ; i< event_list.size(); i++)
        {
            if (event_list.get(i).getEventID().equals(marker_name))
            {
                selected_event = event_list.get(i);
                break;
            }
        }
        passing_person_id = selected_event.getPersonID();
        getPerson();
        output_string += current_person.getFirstName();
        output_string += " ";
        output_string += current_person.getLastName();
        output_string += "\n";
        output_string += selected_event.getEventType();
        output_string += ": ";
        output_string += selected_event.getCity();
        output_string += ", ";
        output_string += selected_event.getCountry();
        output_string += " (";
        output_string += selected_event.getYear();
        output_string += " )";
        event_text_view.setText(output_string);
        event_text_view.setVisibility(View.VISIBLE);
        gender_image.setVisibility(View.VISIBLE);

        if (current_person.getGender().equals("f"))
        {
            gender_image.setColorFilter(getContext().getResources().getColor(R.color.pink));
        }
        if (current_person.getGender().equals("m"))
        {
            gender_image.setColorFilter(getContext().getResources().getColor(R.color.blue));
        }

        //draw lines
        Person local_person = new Person();
        local_person.setSpouseID(current_person.getSpouseID());
        local_person.setMotherID(current_person.getMotherID());
        local_person.setFatherID(current_person.getFatherID());
        local_person.setGender(current_person.getGender());
        local_person.setLastName(current_person.getLastName());
        local_person.setFirstName(current_person.getFirstName());
        local_person.setPersonID(current_person.getPersonID());
        local_person.setAssociatedUsername(current_person.getAssociatedUsername());
        if (SettingsBase.getSpouseLines() == true)
        {
            SpouseLines(selected_event);
        }
        current_person = local_person;
        if (SettingsBase.getFamilyTreeLines() == true)
        {
            FamilyTreeLines(selected_event,10);
        }
        current_person = local_person;
        if (SettingsBase.getLifeStoryLines() == true)
        {
            LifeStoryLines(selected_event);
        }

        return false;

    }

    private void LifeStoryLines(Event selected_event)
    {
        boolean got_events = false;
        boolean once = false;
        while (got_events == false)
        {
            getEarliestPersonEvent(true);
            LatLng selected_event_loc = new LatLng(selected_event.getLatitude(), selected_event.getLongitude());
            LatLng earliest_event_loc = new LatLng(current_earliest_event.getLatitude(), current_earliest_event.getLongitude());
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(selected_event_loc, earliest_event_loc)
                    .width(10)
                    .color(Color.GREEN));
            polyline_list.add(line);
            selected_event = current_earliest_event;
            if (once == true) {
                if (selected_event_loc.latitude == earliest_event_loc.latitude && selected_event_loc.longitude == earliest_event_loc.longitude)
                    got_events = true;
            }
            once = true;
        }
    }

    private void FamilyTreeLines(Event selected_event, int line_size)
    {
        Person local_person = new Person();
        local_person.setSpouseID(current_person.getSpouseID());
        local_person.setMotherID(current_person.getMotherID());
        local_person.setFatherID(current_person.getFatherID());
        local_person.setGender(current_person.getGender());
        local_person.setLastName(current_person.getLastName());
        local_person.setFirstName(current_person.getFirstName());
        local_person.setPersonID(current_person.getPersonID());
        local_person.setAssociatedUsername(current_person.getAssociatedUsername());
            if (current_person.getFatherID() != null) {
                try {
                    passing_person_id = current_person.getFatherID();
                    getEarliestPersonEvent(false);
                    LatLng selected_event_loc = new LatLng(selected_event.getLatitude(), selected_event.getLongitude());
                    LatLng spouse_event_loc = new LatLng(current_earliest_event.getLatitude(), current_earliest_event.getLongitude());
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(selected_event_loc, spouse_event_loc)
                            .width(line_size)
                            .color(Color.BLUE));
                    polyline_list.add(line);
                    getPerson();
                    FamilyTreeLines(current_earliest_event, line_size - 4);

                } catch (Exception e) {
                    e.getMessage();
                }
            }

            current_person = local_person;
        if (current_person.getMotherID() != null)
        {
            try {
                passing_person_id = current_person.getMotherID();
                getEarliestPersonEvent(false);
                LatLng selected_event_loc = new LatLng(selected_event.getLatitude(), selected_event.getLongitude());
                LatLng spouse_event_loc = new LatLng(current_earliest_event.getLatitude(), current_earliest_event.getLongitude());
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(selected_event_loc, spouse_event_loc)
                        .width(line_size)
                        .color(Color.BLUE));
                polyline_list.add(line);
                getPerson();
                FamilyTreeLines(current_earliest_event, line_size - 4);

            }
            catch(Exception e)
            {
                e.getMessage();
            }
        }

    }

    private void SpouseLines(Event selected_event)
    {
        try {
            passing_person_id = current_person.getSpouseID();
            getEarliestPersonEvent(false);
            LatLng selected_event_loc = new LatLng(selected_event.getLatitude(),selected_event.getLongitude());
            LatLng spouse_event_loc = new LatLng(current_earliest_event.getLatitude(),current_earliest_event.getLongitude());
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(selected_event_loc, spouse_event_loc)
                    .width(10)
                    .color(Color.RED));
            polyline_list.add(line);
        }
        catch(Exception e)
        {
            e.getMessage();
        }
    }

    private void getEarliestPersonEvent(boolean remove)
    {
        getPerson();
        int earliest_year = 3000;
        for (int i = 0; i < event_list.size(); i++)
        {
            if (event_list.get(i).getPersonID().equals(current_person.getPersonID()))
            {
                if (earliest_year > event_list.get(i).getYear())
                {
                    earliest_year = event_list.get(i).getYear();
                    current_earliest_event = event_list.get(i);
                    if (remove == true)
                        event_list.remove(i);
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (map != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            map.clear();
            event_list.clear();
            Person local_person = new Person();
            local_person.setSpouseID(current_person.getSpouseID());
            local_person.setMotherID(current_person.getMotherID());
            local_person.setFatherID(current_person.getFatherID());
            local_person.setGender(current_person.getGender());
            local_person.setLastName(current_person.getLastName());
            local_person.setFirstName(current_person.getFirstName());
            local_person.setPersonID(current_person.getPersonID());
            local_person.setAssociatedUsername(current_person.getAssociatedUsername());
            getEvents();
            current_person = local_person;
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }
}

