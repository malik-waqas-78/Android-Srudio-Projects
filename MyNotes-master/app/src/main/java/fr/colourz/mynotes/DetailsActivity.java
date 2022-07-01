package fr.colourz.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {

    private Note note;
    private DatabaseHandler db;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_dateModified;
    private ImageButton btn_delete;

    private ConstraintLayout details_constraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);
        Intent intent = getIntent();
        note = db.getNote(intent.getIntExtra("id", 0));

        tv_title = findViewById(R.id.tv_details_title);
        tv_title.setText(note.getTitle());
        tv_content = findViewById(R.id.tv_details_content);
        tv_content.setText(Html.fromHtml(note.getContent()), TextView.BufferType.SPANNABLE);
        tv_dateModified = findViewById(R.id.tv_details_dateModified);

        details_constraintlayout = findViewById(R.id.details_constraintlayout);
        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        if (preferences.getString("app_theme", "light").matches("dark")) {
            details_constraintlayout.setBackgroundColor(getResources().getColor(R.color.grey));
            tv_content.setTextColor(getResources().getColor(R.color.white));
            tv_title.setTextColor(getResources().getColor(R.color.white));
            tv_dateModified.setTextColor(getResources().getColor(R.color.white));
        } else {
            details_constraintlayout.setBackgroundColor(getResources().getColor(R.color.light_grey_2));
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(note.getDateModified()) * 1000L);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        tv_dateModified.setText(getString(R.string.date_last_modification) + " " + date);

        tv_content.setMovementMethod(LinkMovementMethod.getInstance());

        btn_delete = findViewById(R.id.toolbar_details_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                db.deleteNote(note);
                                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage(getString(R.string.edit_delete_confirm))
                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, EditActivity.class);
                intent.putExtra("id", note.getId());
                startActivity(intent);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
