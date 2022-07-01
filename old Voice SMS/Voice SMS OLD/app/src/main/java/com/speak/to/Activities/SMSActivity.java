package com.speak.to.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.speak.to.Adapters.Image_Preview_Adapter;
import com.speak.to.Adapters.Language_Adapter;
import com.speak.to.Adapters.myDatabaseAdapter;
import com.speak.to.Dialogs.DataClear_Dialog_Voice_SMS;
import com.speak.to.Dialogs.GeneralDialog_VoiceSMS;
import com.speak.to.Interfaces.ClearTextDialogInterface_Voice_SMS;
import com.speak.to.Interfaces.GeneralDialogInterface_voiceSMS;
import com.speak.to.Models.Lang_Item;
import com.speak.to.R;
import com.speak.to.Utils.Constants;
import com.speak.to.Utils.RateUs_Voice_SMS;
import com.speak.to.Utils.Shared_Pref_Helper_Voice_SMS;
import com.speak.to.databinding.ActivitySmsBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;
import static android.view.View.GONE;
import static com.speak.to.Utils.Constants.DEFAULT_LOCALE;
import static com.speak.to.Utils.Constants.KEY_OPEN_MESSAGE;
import static com.speak.to.Utils.Constants.KEY_TXT_MESSAGE;
import static com.speak.to.Utils.Constants.Max_files;
import static com.speak.to.Utils.Constants.NAV_BOTTOM_COPY_ID;
import static com.speak.to.Utils.Constants.NAV_BOTTOM_DOCS_ID;
import static com.speak.to.Utils.Constants.NAV_BOTTOM_IMAGE_ID;
import static com.speak.to.Utils.Constants.NAV_BOTTOM_SAVE_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_DELETE_ALL_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_NEW_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_OPEN_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_RATE_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_SAVE_ID;
import static com.speak.to.Utils.Constants.NAV_DRAWER_SHARE_ID;
import static com.speak.to.Utils.Constants.SELECTED_LOCALE;
import static com.speak.to.Utils.Constants.files_list;
import static com.speak.to.Utils.Constants.locale_array_list;

public class SMSActivity extends AppCompatActivity {
    private static int Temp_Remember_MODE;
    private final int IMAGE_PICKER = 2332, FILE_PICKER = 3223, VOICE_RECOGNIZER = 3232;
    private ActivitySmsBinding binding;
    private NavigationView nav_drawer;
    private BottomNavigationView bottom_nav_bar;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private EditText voice_input_result;
    private LottieAnimationView animationView;
    private ImageView btnSend, btnClearText, btnNavDrawer;
    private Spinner localeSpinner;
    private int spinnerCount;
    private Shared_Pref_Helper_Voice_SMS shared_pref_helper_voice_sms;
    private Context context;
    private String vInput = "-";
    private myDatabaseAdapter myDbAdapter;
    private boolean isComingBackFromOpenActivity;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(binding.getRoot());

        loadBannerAdd();

        initViews();
        init_Constants_Variables();
    }

    private void initViews() {
        // Floating Action button
        fab = findViewById(R.id.fab_voice_search);

        btnClearText = findViewById(R.id.btn_clear);
        btnSend = findViewById(R.id.btn_send);
        btnNavDrawer = findViewById(R.id.nav_icon_back);

        animationView = findViewById(R.id.animation_view_search_activity);

        bottom_nav_bar = findViewById(R.id.bottomNavigationView_voice_search);
        bottom_nav_bar.setSelectedItemId(R.id.placeholder);
        drawer = binding.drawerLayout;
        nav_drawer = findViewById(R.id.nav_view);

        voice_input_result = findViewById(R.id.result_textview_search_activity);
        localeSpinner = findViewById(R.id.lang_spinner);
    }

    private void initListeners() {
        fab.setOnClickListener(view -> {
            if (!vInput.equals("-")) {
                voice_input_result.setText(vInput);
            }
            checkPermissionAndStartActivity(VOICE_RECOGNIZER);
        });

        btnNavDrawer.setOnClickListener(view -> {
            drawer.closeDrawer(Gravity.LEFT);
            drawer.openDrawer(Gravity.LEFT);
        });

        btnClearText.setOnClickListener(view -> {
            if (voice_input_result.getText().length() > 0 || files_list.size() > 0) {
                DataClear_Dialog_Voice_SMS.CreateClearDataDialog(
                        this,
                        new ClearTextDialogInterface_Voice_SMS() {
                            @Override
                            public void ClearImages() {
                                files_list = new ArrayList<>();
                                Constants.Files_Count = 0;
                                Max_files = 10;
                                initRecyclerView();
                            }

                            @Override
                            public void ClearText() {
                                voice_input_result.setText("");
                                vInput = "-";
                            }

                            @Override
                            public void ClearBoth() {
                                files_list = new ArrayList<>();
                                Constants.Files_Count = 0;
                                initRecyclerView();
                                Max_files = 10;
                                voice_input_result.setText("");
                                vInput = "";
                            }
                        }
                );
            } else if (voice_input_result.getText().length() == 0) {
                Toast.makeText(this, "Please Enter a message first.", Toast.LENGTH_SHORT).show();
            } else if (files_list.size() == 0) {
                Toast.makeText(this, "Please select Images/Files first.", Toast.LENGTH_SHORT).show();
            }
        });

        nav_drawer.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case NAV_DRAWER_NEW_ID:
                    GeneralDialog_VoiceSMS.CreateGeneralDialog(this
                            , getResources().getString(R.string.nav_new_title)
                            , getResources().getString(R.string.nav_new_desc)
                            , getResources().getString(R.string.yes)
                            , getResources().getString(R.string.no)
                            , new GeneralDialogInterface_voiceSMS() {
                                @Override
                                public void Positive(Dialog dialog) {
                                    startActivity(new Intent(SMSActivity.this, SMSActivity.class));
                                    finish();
                                }

                                @Override
                                public void Negative(Dialog dialog) {
                                    collapseDrawer();
                                }
                            }
                    );
                    break;
                case NAV_DRAWER_OPEN_ID:
                    startActivityForResult(new Intent(this, OpenMessagesActivity.class)
                            , Constants.OPEN_MESSAGE_ACTIVITY_REQUEST_CODE);
                    break;
                case NAV_DRAWER_SAVE_ID:
                    if (voice_input_result.length() > 0) {
                        copyText();
                        Toast.makeText(SMSActivity.this, "Message Copied.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SMSActivity.this, "Please Enter a message first", Toast.LENGTH_SHORT).show();
                    }
                    collapseDrawer();
                    break;
                case NAV_DRAWER_DELETE_ALL_ID:
                    GeneralDialog_VoiceSMS.CreateGeneralDialog(this
                            , getResources().getString(R.string.nav_delete_title)
                            , getResources().getString(R.string.nav_delete_desc)
                            , getResources().getString(R.string.yes)
                            , getResources().getString(R.string.no)
                            , new GeneralDialogInterface_voiceSMS() {
                                @Override
                                public void Positive(Dialog dialog) {
                                    myDbAdapter.deleteAll();
                                }

                                @Override
                                public void Negative(Dialog dialog) {
                                    collapseDrawer();
                                }
                            }
                    );
                    break;
                case NAV_DRAWER_SHARE_ID:
                    startSharing(SMSActivity.this);
                    break;
                case NAV_DRAWER_RATE_ID:
                    new RateUs_Voice_SMS(this).rateApp();
                    break;
            }
            return false;
        });

        bottom_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case NAV_BOTTOM_COPY_ID: {
                        if (voice_input_result.length() > 0) {
                            copyText();
                            Toast.makeText(SMSActivity.this, "Message Copied.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SMSActivity.this, "Please Enter a message first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case NAV_BOTTOM_SAVE_ID: {
                        if (voice_input_result.length() > 0) {
                            if (!myDbAdapter.searchInDb(voice_input_result.getText().toString())) {
                                myDbAdapter.insertData(voice_input_result.getText().toString());
                                Toast.makeText(SMSActivity.this, "Message saved.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SMSActivity.this, "Message Already Exists.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SMSActivity.this, "Please Enter a message first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case NAV_BOTTOM_DOCS_ID: {
                        checkPermissionAndStartActivity(FILE_PICKER);
                        break;
                    }
                    case NAV_BOTTOM_IMAGE_ID: {
                        checkPermissionAndStartActivity(IMAGE_PICKER);
                        break;
                    }
                }
                return false;
            }
        });

        ArrayList<Lang_Item> mLangList = new ArrayList<>(Arrays.asList(locale_array_list));
        Language_Adapter mLangAdapter = new Language_Adapter(this, mLangList);
        int index = shared_pref_helper_voice_sms.Get_Int_VSMS(SELECTED_LOCALE, DEFAULT_LOCALE);
        localeSpinner.setAdapter(mLangAdapter);
        localeSpinner.setSelection(index);

        localeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCount > 0) {
                    // Prevent first click due to OnCreateView() by using a counter
                    shared_pref_helper_voice_sms.Set_Int_VSMS(SELECTED_LOCALE, localeSpinner.getSelectedItemPosition());
                    if (!isComingBackFromOpenActivity) {
                        if (!vInput.equals("-")) {
                            voice_input_result.setText(vInput);
                            vInput = "-";
                        } else {
                            voice_input_result.setText("");
                        }
                    } else {
                        vInput = "-";
                        isComingBackFromOpenActivity = false;
                    }
                } else {
                    spinnerCount++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSend.setOnClickListener(view -> {
            String textInput = voice_input_result.getText().toString();
            if (textInput.length() > 0 || files_list.size() > 0) {
                Intent intent = new Intent(SMSActivity.this, SendMessageActivity.class);
                intent.putExtra(KEY_TXT_MESSAGE, textInput);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No Messages/Media Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermissionAndStartActivity(int MODE) {
        switch (MODE) {
            case VOICE_RECOGNIZER: {
                if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
                    setupRecognizer();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Temp_Remember_MODE = MODE;
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                    }
                }
                break;
            }
            case FILE_PICKER: {
                if (isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivity(new Intent(this, FileViewActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Temp_Remember_MODE = MODE;
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                    }
                }
                break;
            }
            case IMAGE_PICKER: {
                if (isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivity(new Intent(this, ImageViewActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Temp_Remember_MODE = MODE;
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case Constants.REQUEST_RECORD_AUDIO_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkPermissionAndStartActivity(VOICE_RECOGNIZER);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access to microphone to record your audio",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
                case Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                                break;
                            } else {
                                checkPermissionAndStartActivity(Temp_Remember_MODE);
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow read access to storage to Access media files",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
                case Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkPermissionAndStartActivity(Temp_Remember_MODE);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow write access to storage to Access media files",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }

            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SMSActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void startSharing(SMSActivity smsActivity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareText = getResources().getString(R.string.initialText)
                + "\nhttps://play.google.com/store/apps/details?id="
                + Objects.requireNonNull(context).getPackageName();
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, getString(R.string.share_subject)));
    }

    private void init_Constants_Variables() {
        spinnerCount = 0;
        Constants.Files_Count = 0;
        isComingBackFromOpenActivity = false;
        shared_pref_helper_voice_sms = new Shared_Pref_Helper_Voice_SMS(this);
        context = SMSActivity.this;
        files_list = new ArrayList<>();
        myDbAdapter = new myDatabaseAdapter(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.img_preview_recycler_view);
        LinearLayoutManager layout_Mgr = new LinearLayoutManager(getApplicationContext());
        layout_Mgr.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layout_Mgr);
        Image_Preview_Adapter adapter = new Image_Preview_Adapter(SMSActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void collapseDrawer() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void copyText() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String textInput = voice_input_result.getText().toString();
        if (textInput != null) {
            ClipData clip = ClipData.newPlainText("voice_input", textInput);
            clipboard.setPrimaryClip(clip);
        } else {
            Toast.makeText(this, "Error:: No Text to Copy", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecognizer() {
        String locale = locale_array_list[shared_pref_helper_voice_sms.Get_Int_VSMS(SELECTED_LOCALE, DEFAULT_LOCALE)].getCountryCode();
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(EXTRA_LANGUAGE, locale);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBeginningOfSpeech() {
                voice_input_result.setSelection(voice_input_result.getText().toString().length());
                Log.v("speech", "Beginning for speech");
            }

            @Override
            public void onRmsChanged(float rms) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                Toast.makeText(context, getErrorText(error), Toast.LENGTH_SHORT).show();
                animationView.setVisibility(GONE);
                speechRecognizer.stopListening();
                speechRecognizer.destroy();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(RESULTS_RECOGNITION);
                if (vInput.equals("-")) {
                    vInput = data.get(0) + " ";
                } else {
                    vInput = vInput + data.get(0) + " ";
                }
                voice_input_result.setText(vInput);
                voice_input_result.setSelection(voice_input_result.getText().toString().length());
                animationView.setVisibility(GONE);
                speechRecognizer.stopListening();
                speechRecognizer.destroy();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                if (partialResults != null) {
                    ArrayList<String> data = partialResults.getStringArrayList(RESULTS_RECOGNITION);
                    if (vInput.equals("-")) {
                        voice_input_result.setText(data.get(0));
                    } else {
                        voice_input_result.setText(vInput + " " + data.get(0));
                    }
                    voice_input_result.setSelection(voice_input_result.getText().toString().length());
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
        speechRecognizer.startListening(speechIntent);
    }

    public boolean isPermissionAllowed(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spinnerCount = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListeners();
        initRecyclerView();
    }

    @Override
    protected void onPause() {
        if (voice_input_result.getText() != null && voice_input_result.getText().toString().length() > 0) {
            vInput = voice_input_result.getText().toString();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.OPEN_MESSAGE_ACTIVITY_REQUEST_CODE) {
                String selectedText = data.getStringExtra(KEY_OPEN_MESSAGE);
                voice_input_result.setText(selectedText);
                vInput = selectedText;
                isComingBackFromOpenActivity = true;
                collapseDrawer();
            }
        }
        switch (requestCode) {
            case Constants.REQUEST_RECORD_AUDIO_PERMISSION: {
                if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
                    checkPermissionAndStartActivity(VOICE_RECOGNIZER);
                }
                break;
            }
            case Constants.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (isPermissionAllowed(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (!isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                        }
                    } else {
                        checkPermissionAndStartActivity(Temp_Remember_MODE);
                    }
                }
                break;
            }
            case Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION: {
                checkPermissionAndStartActivity(Temp_Remember_MODE);
                break;
            }
        }
    }

    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error, Pleas try again";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    public void loadBannerAdd() {
        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAG", "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
        relativeLayout.addView(adView);
    }
}