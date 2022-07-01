package fr.colourz.mynotes;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */

public class SettingsActivity extends AppCompatPreferenceActivity {

    static boolean isMainFragment = false;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        isMainFragment = true;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DesignPreferenceFragment.class.getName().equals(fragmentName)
                || InfoPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
            if(preferences.getString("app_theme", "light").matches("dark")) {
                addPreferencesFromResource(R.xml.pref_general);
            } else {
                addPreferencesFromResource(R.xml.pref_general);
            }
            setHasOptionsMenu(true);

            isMainFragment = false;

            Preference btn_deleteNotes = findPreference("delete_notes");
            btn_deleteNotes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.settings_delete_confirm))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                DatabaseHandler db = new DatabaseHandler(getActivity());
                                List<Note> listNotes = db.getAllNotes();
                                for(Note note : listNotes)
                                    db.deleteNote(note);
                                Toast.makeText(getActivity(), getString(R.string.settings_delete_confirmation), Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                }
                            });
                    builder.show();
                    return true;
                }
            });

            Preference btn_generateNotes = findPreference("generate_notes");
            btn_generateNotes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DatabaseHandler db = new DatabaseHandler(getActivity());
                    FakeNotes fakeNotes = new FakeNotes();
                    List<Note> listNotes = fakeNotes.getFakeNotes();
                    for(Note note : listNotes)
                        db.addNote(note);
                    Toast.makeText(getActivity(), getString(R.string.settings_generate_confirmation), Toast.LENGTH_LONG).show();
                    return true;
                }
            });

        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class InfoPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_info);
            setHasOptionsMenu(true);

            isMainFragment = false;

            Preference nbNotes = findPreference("nbNotes_key");
            DatabaseHandler db = new DatabaseHandler(getActivity());
            List<Note> list_notes = db.getAllNotes();

             /* If you want to display words and characters count
             * It may takes some time

            int nbCharactersTotal = 0;
            int nbWordsTotal = 0;

            for(Note note : list_notes){
                boolean word = false;
                int endOfLine = Html.fromHtml(note.getContent()).length() - 1;
                for (int i = 0; i < Html.fromHtml(note.getContent()).length(); i++) {
                    nbCharactersTotal++;
                    if (Character.isLetter(Html.fromHtml(note.getContent()).charAt(i)) && i != endOfLine) {
                        word = true;
                    } else if (!Character.isLetter(Html.fromHtml(note.getContent()).charAt(i)) && word) {
                        nbWordsTotal++;
                        word = false;
                    } else if (Character.isLetter(Html.fromHtml(note.getContent()).charAt(i)) && i == endOfLine) {
                        nbWordsTotal++;
                    }
                }
            }

            String notes = "";
            String words = "";
            String characters = "";

            if (list_notes.size() == 0)
                notes = "No note saved";
            else if (list_notes.size() == 1)
                notes = "1 note";
            else
                notes = list_notes.size() + " notes";

            if (nbWordsTotal == 1)
                words = ", 1 word and ";
            else if (nbWordsTotal > 1)
                words = ", " + nbWordsTotal + " words and ";

            if (nbCharactersTotal == 1)
                characters = "1 character.";
            else if (nbCharactersTotal > 1)
                characters = nbCharactersTotal + " characters.";

            nbNotes.setSummary(notes + words + characters);
            */

            String notes;
            if (list_notes.size() == 0)
                notes = "No note saved";
            else if (list_notes.size() == 1)
                notes = "1 note";
            else
                notes = list_notes.size() + " notes";
            nbNotes.setSummary(notes);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DesignPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_design);
            setHasOptionsMenu(true);

            isMainFragment = false;

            bindPreferenceSummaryToValue(findPreference("date_format_list"));
            SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
            final ListPreference date_format_list = (ListPreference) findPreference("date_format_list");

            Calendar cal = Calendar.getInstance();
            String date = "";
            if(preferences.getString("date_format", "ddmmyyyy").matches("ddmmyyyy")){
                date_format_list.setValue("ddmmyyyy");
                date = DateFormat.format("dd/MM/yyyy", cal).toString();
            } else if (preferences.getString("date_format", "ddmmyyyy").matches("ddmmmyyyy")) {
                date_format_list.setValue("ddmmmyyyy");
                date = DateFormat.format("dd-MMM-yyyyy", cal).toString();
            } else {
                date_format_list.setValue("mmddyyyy");
                date = DateFormat.format("MM-dd-yyyy", cal).toString();
            }
            date_format_list.setSummary(date);

            date_format_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String date;
                    Calendar cal = Calendar.getInstance();
                    if (date_format_list.getValue().toString().matches("ddmmyyyy")) {
                        date = DateFormat.format("dd/MM/yyyy", cal).toString();
                    } else if(date_format_list.getValue().matches("mmddyyyy")){
                        date = DateFormat.format("MM-dd-yyyy", cal).toString();
                    } else {
                        date = DateFormat.format("MMM-dd-yyyy", cal).toString();
                    }
                    date_format_list.setSummary(date);
                    SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("date_format", newValue.toString());
                    editor.commit();
                    if(preferences.getString("date_format", "ddmmyyyy").matches("ddmmyyyy")){
                        date = DateFormat.format("dd/MM/yyyy", cal).toString();
                        date_format_list.setValue("ddmmyyyy");
                    } else if (preferences.getString("date_format", "ddmmyyyy").matches("ddmmmyyyy")) {
                        date = DateFormat.format("dd-MMM-yyyy", cal).toString();
                        date_format_list.setValue("ddmmmyyyy");
                    } else {
                        date = DateFormat.format("MM-dd-yyyy", cal).toString();
                        date_format_list.setValue("mmddyyyy");
                    }
                    date_format_list.setSummary(date);

                    return false;
                }
            });


            final SwitchPreference display_format_switch = (SwitchPreference) findPreference("display_format_switch");
            display_format_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if(!display_format_switch.isChecked()) {
                        editor.putString("display_mode", "compact");
                        display_format_switch.setChecked(true);
                    } else {
                        editor.putString("display_mode", "regular");
                        display_format_switch.setChecked(false);
                    }
                    editor.commit();
                    return false;
                }
            });

            final SwitchPreference dark_theme_switch = (SwitchPreference) findPreference("dark_theme_switch");
            dark_theme_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if(!dark_theme_switch.isChecked()) {
                        editor.putString("app_theme", "dark");
                        dark_theme_switch.setChecked(true);
                    } else {
                        editor.putString("app_theme", "light");
                        dark_theme_switch.setChecked(false);
                    }
                    editor.commit();
                    Toast.makeText(getActivity(), getString(R.string.settings_dark_theme_switch), Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            final SwitchPreference draw_lines_switch = (SwitchPreference) findPreference("draw_lines_switch");
            draw_lines_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("app_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if(!draw_lines_switch.isChecked()) {
                        editor.putString("draw_lines", "true");
                        draw_lines_switch.setChecked(true);
                    } else {
                        editor.putString("draw_lines", "false");
                        draw_lines_switch.setChecked(false);
                    }
                    editor.commit();
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(isMainFragment){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        isMainFragment = fragment.getClass() == GeneralPreferenceFragment.class;
    }
}
