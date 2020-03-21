package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private LoginFragment login_fragment;
    private FragmentManager fragment_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_fragment = new LoginFragment();
        fragment_manager = this.getSupportFragmentManager();
        fragment_manager.beginTransaction().add(R.id.container, login_fragment).commit();

    }
}
