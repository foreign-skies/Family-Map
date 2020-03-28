package com.example.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LoginFragment login_fragment;
    private MapFragment map_fragment;
    private FragmentManager fragment_manager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search)
        {
            Toast.makeText(this,"Search Called", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_settings)
        {
            Toast.makeText(this,"Settings Called", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        login_fragment = new LoginFragment();
//        fragment_manager = this.getSupportFragmentManager();
//        fragment_manager.beginTransaction().add(R.id.container, login_fragment).commit();

        map_fragment = new MapFragment();
        fragment_manager = this.getSupportFragmentManager();
        fragment_manager.beginTransaction().add(R.id.container, map_fragment).commit();

    }
}

