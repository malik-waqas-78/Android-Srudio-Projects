package fr.colourz.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView list_notes;
    private DatabaseHandler db;
    private static CustomAdapter adapter;
    private SearchView mSearchView;
    private boolean searching;
    private TextView tv_nothing_to_display;
    private Note lastDeletedNote;
    private Snackbar deleteSnackbar;
    private ConstraintLayout main_constraint_layout;

    private int filter;
    private int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("MY NOTES");

        db = new DatabaseHandler(this);

        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        if(preferences.getString("first_time", "true").matches("true")){
            FakeNotes fakeNotes = new FakeNotes();
            Note welcomeNote = fakeNotes.getWelcomeNote();
            db.addNote(welcomeNote);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("first_time", "false");
            editor.commit();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        lastDeletedNote = null;

        deleteSnackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.edit_note_deleted), Snackbar.LENGTH_LONG);
        deleteSnackbar.setAction(getString(R.string.undo), new MyUndoListener());

        filter = 0;
        order = 0;

        searching = false;
        tv_nothing_to_display = findViewById(R.id.tv_nothing_to_display);
        List<Note> notes = db.getAllNotes();
        list_notes = findViewById(R.id.list_notes);
        adapter = new CustomAdapter(notes, getApplicationContext());
        list_notes.setAdapter(adapter);

        main_constraint_layout = findViewById(R.id.main_constraint_layout);
        if (preferences.getString("app_theme", "light").matches("dark")) {
            main_constraint_layout.setBackgroundColor(getResources().getColor(R.color.grey));
            tv_nothing_to_display.setTextColor(getResources().getColor(R.color.white));
        } else {
            main_constraint_layout.setBackgroundColor(getResources().getColor(R.color.light_grey_2));
            tv_nothing_to_display.setTextColor(getResources().getColor(R.color.black));
        }

        if(notes.isEmpty()){
            tv_nothing_to_display.setVisibility(View.VISIBLE);
        } else {
            tv_nothing_to_display.setVisibility(View.INVISIBLE);
        }

        list_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("id", note.getId());
                startActivity(intent);
            }
        });

        list_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Note note = (Note) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.edit_delete_confirm))
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            lastDeletedNote = note;
                            Animation anim = AnimationUtils.loadAnimation(
                                    MainActivity.this, android.R.anim.slide_out_right
                            );
                            anim.setDuration(250);
                            list_notes.getChildAt(position).startAnimation(anim );

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    db.deleteNote(note);
                                    List<Note> notes = db.getAllNotes();
                                    list_notes = findViewById(R.id.list_notes);
                                    adapter = new CustomAdapter(notes, getApplicationContext());
                                    list_notes.setAdapter(adapter);
                                    deleteSnackbar.show();
                                    if(notes.isEmpty())
                                        tv_nothing_to_display.setVisibility(View.VISIBLE);
                                    else
                                        tv_nothing_to_display.setVisibility(View.INVISIBLE);
                                }
                            }, anim.getDuration());
                        }
                    })
                    .setNegativeButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MainActivity.this, EditActivity.class);
                            intent.putExtra("id", note.getId());
                            startActivity(intent);
                        }
                    });
                builder.show();
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_bar_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.main_search));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searching = true;
                List<Note> notes = db.getNotesByTitle(query);
                list_notes = findViewById(R.id.list_notes);
                adapter = new CustomAdapter(notes, getApplicationContext());
                list_notes.setAdapter(adapter);
                if(notes.isEmpty()){
                    tv_nothing_to_display.setVisibility(View.VISIBLE);
                } else {
                    tv_nothing_to_display.setVisibility(View.INVISIBLE);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searching = true;
                List<Note> notes = db.getNotesByTitle(newText);
                list_notes = findViewById(R.id.list_notes);
                adapter = new CustomAdapter(notes, getApplicationContext());
                list_notes.setAdapter(adapter);
                if(notes.isEmpty()){
                    tv_nothing_to_display.setVisibility(View.VISIBLE);
                } else {
                    tv_nothing_to_display.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_bar_order:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_sort, null))
                    .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            List<Note> notes = db.getNotesOrder(filter, order);
                            list_notes = findViewById(R.id.list_notes);
                            adapter = new CustomAdapter(notes, getApplicationContext());
                            list_notes.setAdapter(adapter);
                            deleteSnackbar.show();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                builder.show();
        }
        return false;
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_sort_title:
                if (checked)
                    filter = 0;
                break;
            case R.id.radio_sort_date_created:
                if (checked)
                    filter = 1;
                break;
            case R.id.radio_sort_date_modified:
                if (checked)
                    filter = 2;
                break;

            case R.id.radio_sort_asc:
                if (checked)
                    order = 0;
                break;
            case R.id.radio_sort_desc:
                if (checked)
                    order = 1;
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (searching){
                List<Note> notes = db.getAllNotes();
                list_notes = findViewById(R.id.list_notes);
                adapter = new CustomAdapter(notes, getApplicationContext());
                list_notes.setAdapter(adapter);
                mSearchView.onActionViewCollapsed();
                searching = false;
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            db.addNote(lastDeletedNote);
            List<Note> notes = db.getAllNotes();
            list_notes = findViewById(R.id.list_notes);
            adapter = new CustomAdapter(notes, getApplicationContext());
            list_notes.setAdapter(adapter);
            if(notes.isEmpty())
                tv_nothing_to_display.setVisibility(View.VISIBLE);
            else
                tv_nothing_to_display.setVisibility(View.INVISIBLE);
        }
    }
}
