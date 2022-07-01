package com.speak.to.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.speak.to.Adapters.Language_Adapter;
import com.speak.to.Dialogs.GeneralDialog_VoiceSMS;
import com.speak.to.Interfaces.GeneralDialogInterface_voiceSMS;
import com.speak.to.Models.Lang_Item;
import com.speak.to.R;
import com.speak.to.Utils.Constants;
import com.speak.to.Utils.Shared_Pref_Helper_Voice_SMS;
import com.speak.to.databinding.ActivityVoiceSearchBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import static android.speech.RecognizerIntent.EXTRA_LANGUAGE;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;
import static android.view.View.GONE;
import static com.speak.to.Utils.Constants.ALIBABA;
import static com.speak.to.Utils.Constants.ALIEXPRESS;
import static com.speak.to.Utils.Constants.AMAZON;
import static com.speak.to.Utils.Constants.BING;
import static com.speak.to.Utils.Constants.DARAZ;
import static com.speak.to.Utils.Constants.DEFAULT_LOCALE;
import static com.speak.to.Utils.Constants.DUCK;
import static com.speak.to.Utils.Constants.EBAY;
import static com.speak.to.Utils.Constants.FACEBOOK;
import static com.speak.to.Utils.Constants.FLIPBOARD;
import static com.speak.to.Utils.Constants.GOOGLE;
import static com.speak.to.Utils.Constants.GOOGLE_MAPS;
import static com.speak.to.Utils.Constants.IMDB;
import static com.speak.to.Utils.Constants.INSTAGRAM;
import static com.speak.to.Utils.Constants.MEDIUM;
import static com.speak.to.Utils.Constants.MSN;
import static com.speak.to.Utils.Constants.OLX;
import static com.speak.to.Utils.Constants.PINTEREST;
import static com.speak.to.Utils.Constants.QUORA;
import static com.speak.to.Utils.Constants.REDDIT;
import static com.speak.to.Utils.Constants.SEARCH_QUERY;
import static com.speak.to.Utils.Constants.SEARCH_SOURCE_NAME;
import static com.speak.to.Utils.Constants.SELECTED_LOCALE;
import static com.speak.to.Utils.Constants.STACK_OVERFLOW;
import static com.speak.to.Utils.Constants.TIKTOK;
import static com.speak.to.Utils.Constants.TWITTER;
import static com.speak.to.Utils.Constants.VOICE_NAV_BOTTOM_CLEAR_ID;
import static com.speak.to.Utils.Constants.VOICE_NAV_BOTTOM_SEARCH_ID;
import static com.speak.to.Utils.Constants.WIKI;
import static com.speak.to.Utils.Constants.YAHOO;
import static com.speak.to.Utils.Constants.YOUTUBE;
import static com.speak.to.Utils.Constants.locale_array_list;

public class VoiceSearchActivity extends AppCompatActivity {
    private String vInput = "-";
    private ActivityVoiceSearchBinding binding;
    private Intent speechIntent;
    private Shared_Pref_Helper_Voice_SMS shared_pref_helper_voice_sms;
    private int web_source, spinnerCount;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceSearchBinding.inflate(getLayoutInflater());
        if (getIntent().hasExtra(SEARCH_SOURCE_NAME)) {
            web_source = getIntent().getIntExtra(SEARCH_SOURCE_NAME, GOOGLE);
        }

        binding.toolbarSearchActivity.setTitle(getString(web_source));
        setSupportActionBar(binding.toolbarSearchActivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.bottomNavigationViewVoiceSearch.setSelectedItemId(R.id.placeholder);

        binding.toolbarSearchActivity.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
        setContentView(binding.getRoot());

        loadBannerAdd();
//        InterstitialAddsVoiceSMS.showAdd();

        shared_pref_helper_voice_sms = new Shared_Pref_Helper_Voice_SMS(this);

        binding.fabVoiceSearch.setOnClickListener(view -> {
            if (!vInput.equals("-")) {
                binding.resultTextviewSearchActivity.setText(vInput);
            }
            checkPermissionAndStartActivity();
        });

        binding.resultTextviewSearchActivity.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        binding.bottomNavigationViewVoiceSearch.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        switch (item.getItemId()) {
                            case VOICE_NAV_BOTTOM_CLEAR_ID: {
                                if (binding.resultTextviewSearchActivity.getText().toString().length() > 0) {
                                    GeneralDialog_VoiceSMS.CreateGeneralDialog(VoiceSearchActivity.this,
                                            getString(R.string.clear_text_search)
                                            , getString(R.string.main_clear_text_desc_search)
                                            , getString(R.string.yes)
                                            , getString(R.string.no),
                                            new GeneralDialogInterface_voiceSMS() {
                                                @Override
                                                public void Positive(Dialog dialog) {
                                                    binding.resultTextviewSearchActivity.setText("");
                                                    vInput = "-";
                                                }

                                                @Override
                                                public void Negative(Dialog dialog) {
                                                }
                                            });
                                } else {
                                    Toast.makeText(VoiceSearchActivity.this, "Text field is empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            break;
                            case VOICE_NAV_BOTTOM_SEARCH_ID:
                                String textInput = binding.resultTextviewSearchActivity.getText().toString();
                                if (textInput.length() > 0) {
                                    SearchQuery(textInput);
                                } else {
                                    Toast.makeText(VoiceSearchActivity.this, "No Messages Found", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        return false;
                    }
                }
        );

        ArrayList<Lang_Item> mLangList = new ArrayList<>(Arrays.asList(locale_array_list));
        Language_Adapter mLangAdapter = new Language_Adapter(this, mLangList);
        int index = shared_pref_helper_voice_sms.Get_Int_VSMS(SELECTED_LOCALE, DEFAULT_LOCALE);
        binding.langSpinnerSearchActivity.setAdapter(mLangAdapter);
        binding.langSpinnerSearchActivity.setSelection(index);

        binding.langSpinnerSearchActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerCount > 0) {
                    // Prevent first click due to OnCreateView() by using a counter
                    shared_pref_helper_voice_sms.Set_Int_VSMS(SELECTED_LOCALE, binding.langSpinnerSearchActivity.getSelectedItemPosition());
                    binding.resultTextviewSearchActivity.setText("");
                    vInput = "-";
                    binding.animationViewSearchActivity.setVisibility(View.INVISIBLE);
                } else {
                    spinnerCount++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SearchQuery(String query) {
        switch (web_source) {
            case AMAZON: {
                String url = "https://www.amazon.com/s?k=" + query;
                start_activity(url);
                break;
            }
            case ALIBABA: {
                String url = "https://www.alibaba.com/trade/search?" + query;
                start_activity(url);
                break;
            }
            case DARAZ: {
                String url = "https://www.daraz.pk/catalog/?q=" + query;
                start_activity(url);
                break;
            }
            case OLX: {
                String url = "https://www.olx.com.pk/items/q-" + query;
                start_activity(url);
                break;
            }
            case EBAY: {
                String url = "https://www.ebay.com/sch/" + query;
                start_activity(url);
                break;
            }
            case ALIEXPRESS: {
                String url = "https://www.aliexpress.com/wholesale?SearchText=" + query;
                start_activity(url);
                break;
            }

            case FACEBOOK: {
                String url = "https://www.facebook.com/search/top?q=" + query;
                start_activity(url);
                break;
            }
            case INSTAGRAM: {
                String url = "https://www.instagram.com/explore/tags/" + query.replace(" ", "");
                start_activity(url);
                break;
            }
            case TWITTER: {
                String url = "https://twitter.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case TIKTOK: {
                String url = "https://www.tiktok.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case YOUTUBE: {
                String url = "https://www.youtube.com/results?search_query=" + query;
                start_activity(url);
                break;
            }
            case PINTEREST: {
                String url = "https://www.pinterest.com/search/pins/?q=" + query;
                start_activity(url);
                break;
            }

            case GOOGLE: {
                String url = "https://www.google.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case BING: {
                String url = "https://www.bing.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case DUCK: {
                String url = "https://duckduckgo.com/?q=" + query;
                start_activity(url);
                break;
            }
            case YAHOO: {
                String url = "https://search.yahoo.com/search?p=" + query;
                start_activity(url);
                break;
            }
            case WIKI: {
                String url = "https://en.wikipedia.org/w/index.php?search=" + query;
                start_activity(url);
                break;
            }

            case MEDIUM: {
                String url = "https://medium.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case STACK_OVERFLOW: {
                String url = "https://stackoverflow.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case IMDB: {
                String url = "https://www.imdb.com/find?q=" + query;
                start_activity(url);
                break;
            }
            case GOOGLE_MAPS: {
                String url = "https://www.google.com/maps/search/" + query;
                start_activity(url);
                break;
            }

            case REDDIT: {
                String url = "https://www.reddit.com/search/?q=" + query;
                start_activity(url);
                break;
            }
            case QUORA: {
                String url = "https://www.quora.com/search?q=" + query;
                start_activity(url);
                break;
            }
            case FLIPBOARD: {
                String url = "https://flipboard.com/search/" + query;
                start_activity(url);
                break;
            }
            case MSN: {
                String url = "https://www.bing.com/search?q=" + query;
                start_activity(url);
                break;
            }
        }
    }

    private void start_activity(String url) {
        startActivity(new Intent(this, WebViewActivity.class)
                .putExtra(SEARCH_QUERY, url));
    }

    private void checkPermissionAndStartActivity() {
        if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
            setupRecognizer();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}
                        , Constants.REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionAndStartActivity();
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_RECORD_AUDIO_PERMISSION) {
                if (isPermissionAllowed(Manifest.permission.RECORD_AUDIO)) {
                    checkPermissionAndStartActivity();
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(VoiceSearchActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void setupRecognizer() {
        binding.animationViewSearchActivity.setVisibility(View.VISIBLE);
        String locale = locale_array_list[shared_pref_helper_voice_sms.Get_Int_VSMS(SELECTED_LOCALE, DEFAULT_LOCALE)].getCountryCode();
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(EXTRA_LANGUAGE, locale);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                binding.resultTextviewSearchActivity.setSelection(binding.resultTextviewSearchActivity.getText().toString().length());
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
                Toast.makeText(VoiceSearchActivity.this, getErrorText(error), Toast.LENGTH_SHORT).show();
                binding.animationViewSearchActivity.setVisibility(View.INVISIBLE);
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
                binding.resultTextviewSearchActivity.setText(vInput);
                binding.resultTextviewSearchActivity.setSelection(binding.resultTextviewSearchActivity.getText().toString().length());
                binding.animationViewSearchActivity.setVisibility(GONE);
                speechRecognizer.stopListening();
                speechRecognizer.destroy();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                if (partialResults != null) {
                    ArrayList<String> data = partialResults.getStringArrayList(RESULTS_RECOGNITION);
                    if (vInput.equals("-")) {
                        binding.resultTextviewSearchActivity.setText(data.get(0));
                    } else {
                        binding.resultTextviewSearchActivity.setText(vInput + " " + data.get(0));
                    }
                    binding.resultTextviewSearchActivity.setSelection(binding.resultTextviewSearchActivity.getText().toString().length());
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
        speechRecognizer.startListening(speechIntent);
    }

    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
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

    private boolean isPermissionAllowed(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
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