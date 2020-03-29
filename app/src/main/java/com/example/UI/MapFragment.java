package com.example.UI;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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

    private ImageView gender_image;
    private TextView event_text_view;

    public MapFragment(Person user_in, String auth_token_in) {
        user = user_in;
        auth_token = auth_token_in;
    }

    GoogleMap map;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gender_image = (ImageView)view.findViewById(R.id.gender_image);
        event_text_view = (TextView) view.findViewById(R.id.map_event_text);

        getEvents();
         SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);
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
            if (event_color_map.containsKey(event_list.get(i).getEventType()))
            {
                hue = event_color_map.get(event_list.get(i).getEventType());
            }
            else
            {
                event_color_map.put(event_list.get(i).getEventType(), hue_counter);
                hue = hue_counter;
                hue_counter += 60;
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
        output_string += selected_event.getPersonID();
        output_string += " ";
        output_string += selected_event.getEventID();
        output_string += " ";
        output_string += selected_event.getCity();
        output_string += " ";
        output_string += selected_event.getCountry();
        output_string += " ";
        output_string += selected_event.getYear();
        event_text_view.setText(output_string);


        return false;

    }

}
