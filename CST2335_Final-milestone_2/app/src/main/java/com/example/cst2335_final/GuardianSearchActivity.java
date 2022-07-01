package com.example.cst2335_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335_final.adapters.SearchListAdapter;
import com.example.cst2335_final.api.GuardianNewsAPI;
import com.example.cst2335_final.beans.SearchItem;
import com.example.cst2335_final.observer.Observer;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Activity for making a search list
 */
public class GuardianSearchActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "MainActivity";
    private BaseAdapter searchListAdapter;
    private ArrayList<SearchItem> items = new ArrayList<>();
    private ProgressBar progressBar;
    private EditText searchFilter;
    private ListView searchItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_search);

        progressBar = findViewById(R.id.searchProgressBar);

        searchItems = findViewById(R.id.list);
        searchItems.setAdapter(searchListAdapter = new SearchListAdapter(this, items));

        searchFilter = findViewById(R.id.searchFilter);
        Button searchBtn = findViewById(R.id.search);
        searchBtn.setOnClickListener( (click) -> {
            guardianSearch();
            Toast.makeText(this, R.string.searchToast, Toast.LENGTH_SHORT).show();
        });

        searchItems.setOnItemClickListener((parent, view, position, id) ->{
            showMessage(position);
        });

        searchFilter.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                guardianSearch();
                handled = true;
            }
            return handled;
        });
    }

    /**
     * Adds search item to list
     */
    private void guardianSearch() {
        progressBar.setVisibility(View.VISIBLE);
        String searchString = searchFilter.getText().toString();

        GuardianNewsAPI api = new GuardianNewsAPI(this);
        api.execute(searchString);

        searchFilter.setText("", null);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * shows detailed alert of search item
     * @param position search item's position arraylist
     */
    private void showMessage(int position){
        SearchItem item = items.get(position);
        View searchAlertView = getLayoutInflater().inflate(R.layout.row_searchalert, null);

        TextView searchAlterTitle = searchAlertView.findViewById(R.id.searchAlertTitleView);
        searchAlterTitle.setText(item.getTitle());

        TextView searchAlterSection = searchAlertView.findViewById(R.id.searchAlertSectionView);
        searchAlterSection.setText(item.getSection());

        TextView searchAlterUrl = searchAlertView.findViewById(R.id.searchAlertUrlView);
        searchAlterUrl.setText(item.getUrl().toString());

        searchAlterUrl.setOnClickListener( (click) -> {
            Intent openUrl = new Intent(Intent.ACTION_VIEW);
            openUrl.setData(Uri.parse(item.getUrl().toString()));
            startActivity(openUrl);

        });

        AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
        deleteAlertDialog.setTitle(this.getString(R.string.searchAlertTitle))
                .setMessage(this.getString(R.string.searchAlertMessage))
                .setView(searchAlertView)
                .setPositiveButton(R.string.searchAlertDelete, (click, arg) -> {
                    items.remove(position);
                    searchListAdapter.notifyDataSetChanged();
                })
                .setNegativeButton( R.string.searchAlertDismiss, (click, arg) -> {
                } )
                .create().show();
    }

    @Override
    public void update(ArrayList<SearchItem> searchItems) {
        items.clear();

        for(SearchItem item: searchItems){
            items.add(item);
        }

        searchListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void update(Integer progress) {
        progressBar.setScrollBarSize(progress);
        if (progress == 100){
            progressBar.setVisibility(View.GONE);
        }
    }
}