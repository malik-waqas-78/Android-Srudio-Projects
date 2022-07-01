package fr.colourz.mynotes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    public ImageButton btn_bold;
    public ImageButton btn_italic;
    public ImageButton btn_underline;
    public TableLayout format_bar;
    private ImageButton btn_list_bulleted;
    private ImageButton btn_indent_increase;
    private ImageButton btn_indent_decrease;
    public ImageButton btn_url;
    public ImageButton btn_color;
    private EditText et_title;
    private DatabaseHandler db;
    private EditText et_url;
    private String url;
    private boolean editing;
    private String titleBefore;
    private String contentBefore;
    private Note editingNote;
    private static String tabSpace = "    ";
    private static String bulletSpace = " \u2022  ";
    private static LinedEditText et_content;
    private LinearLayout mainLayout;

    public boolean isURLEditing;
    public boolean isBoldEditing;
    public boolean isItalicEditing;
    public boolean isColorEditing;
    public boolean isUnderlineEditing;

    private boolean editChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        if (preferences.getString("app_theme", "light").matches("dark")) {
            setContentView(R.layout.activity_edit_dark);
        } else {
            setContentView(R.layout.activity_edit);
        }

        isBoldEditing = false;
        isItalicEditing = false;
        isUnderlineEditing = false;
        isURLEditing = false;
        isColorEditing = false;

        db = new DatabaseHandler(this);
        mainLayout = findViewById(R.id.mainLayout);
        format_bar = findViewById(R.id.format_bar);

        et_title = findViewById(R.id.et_add_title);

        et_content = new LinedEditText(this, null, EditActivity.this);
        et_content.setBackgroundColor(getResources().getColor(R.color.transparent));
        et_content.setHint(getResources().getString(R.string.content_hint));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (30*scale + 0.5f);
        et_content.setPadding(0, 0, 0, dpAsPixels);

        mainLayout.addView(et_content);
        btn_bold = findViewById(R.id.btn_add_bold);
        btn_italic = findViewById(R.id.btn_add_italic);
        btn_list_bulleted = findViewById(R.id.btn_add_list_bulleted);
        btn_indent_increase = findViewById(R.id.btn_add_indent_increase);
        btn_indent_decrease = findViewById(R.id.btn_add_indent_decrease);
        btn_underline = findViewById(R.id.btn_add_underline);
        btn_color = findViewById(R.id.btn_add_format);
        btn_url = findViewById(R.id.btn_add_url);
        url = null;
        editing = false;
        editingNote = null;

        try {
            Intent intent = getIntent();
            if (intent != null) {
                int id = intent.getIntExtra("id", 0);
                editingNote = db.getNote(id);
                if (editingNote != null) {
                    editing = true;
                    titleBefore = editingNote.getTitle();
                    contentBefore = editingNote.getContent();
                    et_title.setText(editingNote.getTitle());
                    et_content.setText(Html.fromHtml(editingNote.getContent()));
                    editChanges = false;
                }
            }
        } catch (Exception e) {
            Log.i("EXCEPTION", e.toString());
        }

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editing){
                    editChanges = true;
                }
                if(isBoldEditing && start + count > start + before)
                    et_content.getText().setSpan(new StyleSpan(Typeface.BOLD), start + before, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(isItalicEditing && start + count > start + before)
                    et_content.getText().setSpan(new StyleSpan(Typeface.ITALIC), start + before, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(isUnderlineEditing && start + count > start + before)
                    et_content.getText().setSpan(new UnderlineSpan(), start + before, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_content.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER /*|| KeyEvent.*/:
                            try {
                                int startSelection = et_content.getSelectionStart();
                                int endSelection = et_content.getSelectionEnd();
                                int tmpStart = startSelection - 1;
                                int tmpEnd = startSelection;
                                while (tmpStart >= 0 && et_content.getText().toString().charAt(tmpStart - 1) != '\n') {
                                    tmpStart--;
                                }
                                SpannableStringBuilder textBefore = new SpannableStringBuilder(et_content.getText().subSequence(0, tmpStart));
                                SpannableStringBuilder textSelected = new SpannableStringBuilder(et_content.getText().subSequence(tmpStart, tmpEnd));
                                SpannableStringBuilder textAfter = new SpannableStringBuilder(et_content.getText().subSequence(tmpEnd, et_content.getText().length()));
                                SpannableStringBuilder newText = new SpannableStringBuilder();
                                newText.append(textBefore);
                                Boolean isBulleted = false;
                                Boolean isIndented = false;
                                if (textSelected.length() > bulletSpace.length() && textSelected.subSequence(0, bulletSpace.length()).toString().equals(bulletSpace)) {
                                    isBulleted = true;
                                } else if (textSelected.length() > tabSpace.length() && textSelected.subSequence(0, tabSpace.length()).toString().equals(tabSpace)) {
                                    isIndented = true;
                                }
                                newText.append(textSelected);
                                if (isBulleted) {
                                    newText.append("\n" + bulletSpace);
                                    endSelection += bulletSpace.length();
                                } else if (isIndented) {
                                    newText.append("\n" + tabSpace);
                                    endSelection += tabSpace.length();
                                } else
                                    newText.append("\n");
                                newText.append(textAfter);

                                newText.setSpan(new StyleSpan(Typeface.NORMAL), 0, newText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                et_content.setText(newText, TextView.BufferType.SPANNABLE);

                                et_content.setSelection(endSelection + 1);
                                return true;
                            } catch (Exception e){
                                return false;
                            }
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                SpannableString contentSpan = new SpannableString(et_content.getText());

                URLSpan[] rt = contentSpan.getSpans(startSelection, endSelection, URLSpan.class);
                boolean isSet = false;
                for (int i = 0; i < rt.length; i++) {
                    if (rt[i] != null){
                        contentSpan.removeSpan(rt[i]);
                        isSet = true;
                    }
                }
                if(isSet){
                    contentSpan.setSpan(new StyleSpan(Typeface.NORMAL), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    et_content.setText(contentSpan, TextView.BufferType.SPANNABLE);
                    et_content.setSelection(startSelection, endSelection);
                } else {
                    url = null;
                    Context ctx = v.getContext();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    builder.setView(inflater.inflate(R.layout.dialog_url, null))
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog dialogView = (Dialog) dialog;
                                et_url = dialogView.findViewById(R.id.et_url_url);
                                url = et_url.getText().toString();
                                int ss = et_content.getSelectionStart();
                                int es = et_content.getSelectionEnd();
                                SpannableString cs = new SpannableString(et_content.getText());
                                cs.setSpan(new URLSpan(url), ss, es, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                et_content.setText(cs, TextView.BufferType.SPANNABLE);
                                et_content.setSelection(ss, es);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                    builder.show();
                }
            }

        });

        btn_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                SpannableString contentSpan = new SpannableString(et_content.getText());

                StyleSpan[] rt = contentSpan.getSpans(startSelection, endSelection, StyleSpan.class);
                boolean isSet = false;
                for (int i = 0; i < rt.length; i++) {
                    if (rt[i].getStyle() == Typeface.BOLD){
                        contentSpan.removeSpan(rt[i]);
                        isSet = true;
                    }
                }
                if(isSet){
                    contentSpan.setSpan(new StyleSpan(Typeface.NORMAL), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isBoldEditing = false;
                    btn_bold.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btn_bold.setColorFilter(getResources().getColor(R.color.black));
                } else {
                    contentSpan.setSpan(new StyleSpan(Typeface.BOLD), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isBoldEditing = true;
                    btn_bold.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btn_bold.setColorFilter(getResources().getColor(R.color.white));
                }
                et_content.setText(contentSpan, TextView.BufferType.SPANNABLE);
                et_content.setSelection(startSelection, endSelection);
            }
        });

        btn_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                SpannableString contentSpan = new SpannableString(et_content.getText());

                StyleSpan[] rt = contentSpan.getSpans(startSelection, endSelection, StyleSpan.class);
                boolean isSet = false;
                for (int i = 0; i < rt.length; i++) {
                    if (rt[i].getStyle() == Typeface.ITALIC){
                        contentSpan.removeSpan(rt[i]);
                        isSet = true;
                    }
                }
                if(isSet){
                    contentSpan.setSpan(new StyleSpan(Typeface.NORMAL), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isItalicEditing = false;
                    btn_italic.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btn_italic.setColorFilter(getResources().getColor(R.color.black));
                } else {
                    contentSpan.setSpan(new StyleSpan(Typeface.ITALIC), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isItalicEditing = true;
                    btn_italic.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btn_italic.setColorFilter(getResources().getColor(R.color.white));
                }
                et_content.setText(contentSpan, TextView.BufferType.SPANNABLE);
                et_content.setSelection(startSelection, endSelection);
            }
        });

        btn_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                SpannableString contentSpan = new SpannableString(et_content.getText());

                UnderlineSpan[] rt = contentSpan.getSpans(startSelection, endSelection, UnderlineSpan.class);
                boolean isSet = false;
                for (int i = 0; i < rt.length; i++) {
                    if (rt[i] != null){
                        contentSpan.removeSpan(rt[i]);
                        isSet = true;
                    }
                }
                if(isSet){
                    contentSpan.setSpan(new StyleSpan(Typeface.NORMAL), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isUnderlineEditing = true;
                    btn_underline.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btn_underline.setColorFilter(getResources().getColor(R.color.black));
                } else {
                    contentSpan.setSpan(new UnderlineSpan(), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isUnderlineEditing = true;
                    btn_underline.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btn_underline.setColorFilter(getResources().getColor(R.color.white));
                }
                et_content.setText(contentSpan, TextView.BufferType.SPANNABLE);
                et_content.setSelection(startSelection, endSelection);
            }
        });

        btn_list_bulleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                while(startSelection != 0 && et_content.getText().toString().charAt(startSelection - 1) != '\n'){
                    startSelection--;
                }
                while(endSelection != et_content.getText().length() && et_content.getText().toString().charAt(endSelection) != '\n'){
                    endSelection++;
                }

                SpannableStringBuilder textBefore = new SpannableStringBuilder(et_content.getText().subSequence(0, startSelection));
                SpannableStringBuilder textSelected = new SpannableStringBuilder(et_content.getText().subSequence(startSelection, endSelection));
                SpannableStringBuilder textAfter = new SpannableStringBuilder(et_content.getText().subSequence(endSelection, et_content.getText().length()));
                SpannableStringBuilder newText = new SpannableStringBuilder();
                newText.append(textBefore);
                if(textSelected.subSequence(0, bulletSpace.length()).toString().equals(bulletSpace)) {
                    textSelected.delete(0, bulletSpace.length());
                    endSelection -= bulletSpace.length();
                } else {
                    newText.append(bulletSpace);
                    endSelection += bulletSpace.length();
                }
                newText.append(textSelected);
                newText.append(textAfter);

                newText.setSpan(new StyleSpan(Typeface.NORMAL), 0, newText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                et_content.setText(newText, TextView.BufferType.SPANNABLE);
                et_content.setSelection(endSelection);
            }
        });

        btn_indent_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                while(startSelection != 0 && et_content.getText().toString().charAt(startSelection - 1) != '\n'){
                    startSelection--;
                }
                while(endSelection != et_content.getText().length() && et_content.getText().toString().charAt(endSelection) != '\n'){
                    endSelection++;
                }

                SpannableStringBuilder textBefore = new SpannableStringBuilder(et_content.getText().subSequence(0, startSelection));
                SpannableStringBuilder textSelected = new SpannableStringBuilder(et_content.getText().subSequence(startSelection, endSelection));
                SpannableStringBuilder textAfter = new SpannableStringBuilder(et_content.getText().subSequence(endSelection, et_content.getText().length()));
                SpannableStringBuilder newText = new SpannableStringBuilder();
                newText.append(textBefore);
                newText.append(tabSpace);
                endSelection += tabSpace.length();
                newText.append(textSelected);
                newText.append(textAfter);

                newText.setSpan(new StyleSpan(Typeface.NORMAL), 0, newText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                et_content.setText(newText, TextView.BufferType.SPANNABLE);
                et_content.setSelection(endSelection);
            }
        });

        btn_indent_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = et_content.getSelectionStart();
                int endSelection = et_content.getSelectionEnd();

                while(startSelection != 0 && et_content.getText().toString().charAt(startSelection - 1) != '\n'){
                    startSelection--;
                }
                while(endSelection != et_content.getText().length() && et_content.getText().toString().charAt(endSelection) != '\n'){
                    endSelection++;
                }

                SpannableStringBuilder textBefore = new SpannableStringBuilder(et_content.getText().subSequence(0, startSelection));
                SpannableStringBuilder textSelected = new SpannableStringBuilder(et_content.getText().subSequence(startSelection, endSelection));
                SpannableStringBuilder textAfter = new SpannableStringBuilder(et_content.getText().subSequence(endSelection, et_content.getText().length()));
                SpannableStringBuilder newText = new SpannableStringBuilder();

                if(textSelected.subSequence(0, tabSpace.length()).toString().equals(tabSpace)) {
                    textSelected.delete(0, tabSpace.length());
                    endSelection -= tabSpace.length();
                }
                newText.append(textBefore);
                newText.append(textSelected);
                newText.append(textAfter);

                newText.setSpan(new StyleSpan(Typeface.NORMAL), 0, newText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                et_content.setText(newText, TextView.BufferType.SPANNABLE);
                et_content.setSelection(endSelection);
            }
        });

        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Pick color")
                    .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                        int startSelection = et_content.getSelectionStart();
                        int endSelection = et_content.getSelectionEnd();
                        SpannableString contentSpan = new SpannableString(et_content.getText());
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0) {
                                ForegroundColorSpan[] fcs = contentSpan.getSpans(startSelection, endSelection, ForegroundColorSpan.class);
                                for (int i = 0; i < fcs.length; i++) {
                                    if (fcs[i] != null){
                                        contentSpan.removeSpan(fcs[i]);
                                    }
                                }                            } else if (which == 1)
                                contentSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_blue)), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            else if (which == 2)
                                contentSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_red)), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            else if (which == 3)
                                contentSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_green)), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            et_content.setText(contentSpan, TextView.BufferType.SPANNABLE);
                            et_content.setSelection(startSelection, endSelection);
                        }
                    });
                builder.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(editing && !editChanges && et_title.getText().toString().matches(titleBefore)){
                Intent intent = new Intent(EditActivity.this, DetailsActivity.class);
                intent.putExtra("id", editingNote.getId());
                startActivity(intent);
            } else if (!et_title.getText().toString().matches("")) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (editing) {
                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    String date = tsLong.toString();
                                    editingNote.setTitle(et_title.getText().toString());
                                    editingNote.setContent(Html.toHtml(et_content.getText()));
                                    editingNote.setDateModified(date);
                                    db.updateNote(editingNote);
                                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else if (!editing && !db.exists(et_title.getText().toString())) {
                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    String date = tsLong.toString();
                                    Note note = new Note(
                                            et_title.getText().toString(),
                                            Html.toHtml(et_content.getText()),
                                            date,
                                            date
                                    );
                                    db.addNote(note);
                                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(EditActivity.this, getString(R.string.edit_note_saved), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(EditActivity.this, getString(R.string.edit_note_exists), Toast.LENGTH_LONG).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditActivity.this, getString(R.string.edit_no_changes), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.edit_save_confirm)).setPositiveButton(getString(R.string.save), dialogClickListener)
                        .setNegativeButton(getString(R.string.ignore), dialogClickListener).show();

                return true;
            } else if(et_title.getText().toString().matches("") && et_content.getText().toString().matches("")) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            } else {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.cancel();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.edit_missing_title))
                        .setPositiveButton(getString(R.string.edit), dialogClickListener)
                        .setNegativeButton(getString(R.string.ignore), dialogClickListener).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
