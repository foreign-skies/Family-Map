package com.example.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    Switch life_story_switch;
    Switch family_tree_switch;
    Switch spouse_switch;
    Switch father_side_switch;
    Switch mother_side_switch;
    Switch male_events_switch;
    Switch female_events_switch;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        life_story_switch = (Switch) findViewById(R.id.life_story_switch);
        family_tree_switch = (Switch) findViewById(R.id.family_lines_switch);
        spouse_switch = (Switch) findViewById(R.id.spouse_lines_switch);
        father_side_switch = (Switch) findViewById(R.id.father_side_switch);
        mother_side_switch = (Switch) findViewById(R.id.mother_side_switch);
        male_events_switch = (Switch) findViewById(R.id.male_events_switch);
        female_events_switch = (Switch) findViewById(R.id.female_events_switch);
        logout = (TextView) findViewById(R.id.logout_text);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        if(SettingsBase.getLifeStoryLines() == true)
            life_story_switch.setChecked(true);
        if(SettingsBase.getFamilyTreeLines() == true)
            family_tree_switch.setChecked(true);
        if(SettingsBase.getSpouseLines() == true)
            spouse_switch.setChecked(true);
        if (SettingsBase.getFatherSide() == true)
            father_side_switch.setChecked(true);
        if (SettingsBase.getMotherSide() == true)
            mother_side_switch.setChecked(true);
        if (SettingsBase.getMaleEvents() == true)
            male_events_switch.setChecked(true);
        if (SettingsBase.getFemaleEvents() == true)
            female_events_switch.setChecked(true);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("FamilyMap: Settings");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            if (life_story_switch.isChecked())
                SettingsBase.setLifeStoryLines(true);
            else
                SettingsBase.setLifeStoryLines(false);
            if (family_tree_switch.isChecked())
                SettingsBase.setFamilyTreeLines(true);
            else
                SettingsBase.setFamilyTreeLines(false);
            if (spouse_switch.isChecked())
                SettingsBase.setSpouseLines(true);
            else
                SettingsBase.setSpouseLines(false);
            if (father_side_switch.isChecked())
                SettingsBase.setFatherSide(true);
            else
                SettingsBase.setFatherSide(false);
            if (mother_side_switch.isChecked())
                SettingsBase.setMotherSide(true);
            else
                SettingsBase.setMotherSide(false);
            if (male_events_switch.isChecked())
                SettingsBase.setMaleEvents(true);
            else
                SettingsBase.setMaleEvents(false);
            if (female_events_switch.isChecked())
                SettingsBase.setFemaleEvents(true);
            else
                SettingsBase.setFemaleEvents(false);

            finish();
        }
        return true;
    }
}
