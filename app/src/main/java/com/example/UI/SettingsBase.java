package com.example.UI;

public class SettingsBase
{
    private static boolean life_story_lines;
    private static boolean family_tree_lines;
    private static boolean spouse_lines;
    private static boolean father_side;
    private static boolean mother_side;
    private static boolean male_events;
    private static boolean female_events;

    private SettingsBase()
    {
        life_story_lines = false;
        family_tree_lines = false;
        spouse_lines = false;
        father_side = false;
        mother_side = false;
        male_events = false;
        female_events = false;
    }
    private static SettingsBase my_settings_base;
    public static boolean getLifeStoryLines()
    {
        return life_story_lines;
    }
    public static void setLifeStoryLines(boolean input)
    {
        life_story_lines = input;
    }

    public static boolean getFamilyTreeLines()
    {
        return family_tree_lines;
    }
    public static void setFamilyTreeLines(boolean input)
    {
        family_tree_lines = input;
    }

    public static boolean getSpouseLines()
    {
        return spouse_lines;
    }
    public static void setSpouseLines(boolean input)
    {
        spouse_lines = input;
    }

    public static boolean getFatherSide()
    {
        return father_side;
    }
    public static void setFatherSide(boolean input)
    {
        father_side = input;
    }

    public static boolean getMotherSide()
    {
        return mother_side;
    }
    public static void setMotherSide(boolean input)
    {
        mother_side = input;
    }

    public static boolean getMaleEvents()
    {
        return male_events;
    }
    public static void setMaleEvents(boolean input)
    {
        male_events = input;
    }

    public static boolean getFemaleEvents()
    {
        return female_events;
    }
    public static void setFemaleEvents(boolean input)
    {
        female_events = input;
    }
}
