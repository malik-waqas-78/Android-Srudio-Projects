package com.video.trimmer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.video.trimmer.R;
import com.video.trimmer.adapters.LocaleAdapter;
import com.video.trimmer.interfaces.LocaleSelectionInterface;
import com.video.trimmer.modelclasses.LocaleModel;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.utils.SharedPrefClass;

import java.util.ArrayList;
import java.util.Locale;

import static com.video.trimmer.utils.Constants.AFRICAN;
import static com.video.trimmer.utils.Constants.ARABIC;
import static com.video.trimmer.utils.Constants.ARMANIA;
import static com.video.trimmer.utils.Constants.AZERBAIJAAN;
import static com.video.trimmer.utils.Constants.BALRUSIAN;
import static com.video.trimmer.utils.Constants.BENGALI;
import static com.video.trimmer.utils.Constants.BULGARIAN;
import static com.video.trimmer.utils.Constants.CHINESE;
import static com.video.trimmer.utils.Constants.CROATIA;
import static com.video.trimmer.utils.Constants.DANNISH;
import static com.video.trimmer.utils.Constants.DUTCH;
import static com.video.trimmer.utils.Constants.ENGLISH;
import static com.video.trimmer.utils.Constants.FINNISH;
import static com.video.trimmer.utils.Constants.FRENCH;
import static com.video.trimmer.utils.Constants.HIBRU;
import static com.video.trimmer.utils.Constants.HINDI;
import static com.video.trimmer.utils.Constants.ICELANDIC;
import static com.video.trimmer.utils.Constants.INDONESIAN;
import static com.video.trimmer.utils.Constants.ITALIAN;
import static com.video.trimmer.utils.Constants.JAPANESE;
import static com.video.trimmer.utils.Constants.KAZAKISTAN;
import static com.video.trimmer.utils.Constants.KOREAN;
import static com.video.trimmer.utils.Constants.MALAYSIA;
import static com.video.trimmer.utils.Constants.NORWAY;
import static com.video.trimmer.utils.Constants.PHILIPINE;
import static com.video.trimmer.utils.Constants.POLISH;
import static com.video.trimmer.utils.Constants.PORTUGUESE;
import static com.video.trimmer.utils.Constants.ROMANIA;
import static com.video.trimmer.utils.Constants.RUSSIAN;
import static com.video.trimmer.utils.Constants.SERBIAN;
import static com.video.trimmer.utils.Constants.SLOVAK;
import static com.video.trimmer.utils.Constants.SPANISH;
import static com.video.trimmer.utils.Constants.SWEDISH;
import static com.video.trimmer.utils.Constants.THAI;
import static com.video.trimmer.utils.Constants.TURKISH;
import static com.video.trimmer.utils.Constants.URDU;
import static com.video.trimmer.utils.Constants.UZBIK;
import static com.video.trimmer.utils.Constants.VIETNAM;


public class ActivitySelectLanguage extends AppCompatActivity {
    RecyclerView recyclerView;
    SharedPrefClass sharedPrefClass;
    Toolbar toolbar;
    Context context;
    ArrayList<LocaleModel> localeList=new ArrayList<LocaleModel>();
    private boolean themeSwitchState;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        constants=new Constants(ActivitySelectLanguage.this);
        initComponents();
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back_icon);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        localeList=getLangList();
        LocaleAdapter localeAdapter=new LocaleAdapter(localeList, context, new LocaleSelectionInterface() {
            @Override
            public void OnClick(int adapterPosition) {
                OnLocaleClicked(adapterPosition);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(localeAdapter);

    }
    private void OnLocaleClicked(int adapterPosition) {
        LocaleModel model = localeList.get(adapterPosition);
        changeLocale(context, model.getLocaleCode());
    }

    public void changeLocale(Context context, String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.locale = locale;
        } else {
            config.setLocale(new Locale(localeCode));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        sharedPrefClass.saveLanguageCodeinShared(localeCode);
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
        finish();

    }
    private ArrayList<LocaleModel> getLangList() {
        ArrayList<LocaleModel> mData = new ArrayList<>();
        mData.add(new LocaleModel(getResources().getString(R.string.english), ENGLISH, R.drawable.ic_united_kingdom));
        mData.add(new LocaleModel(getResources().getString(R.string.urdu), URDU, R.drawable.ic_pakistan));
        mData.add(new LocaleModel(getResources().getString(R.string.arabic), ARABIC, R.drawable.ic_united_arab_emirates));
        mData.add(new LocaleModel(getResources().getString(R.string.turkish), TURKISH, R.drawable.ic_turkey));
        mData.add(new LocaleModel(getResources().getString(R.string.chinese), CHINESE, R.drawable.ic_china));
        mData.add(new LocaleModel(getResources().getString(R.string.hindi), HINDI, R.drawable.ic_india));
        mData.add(new LocaleModel(getResources().getString(R.string.serbian), SERBIAN, R.drawable.ic_serbia));
        mData.add(new LocaleModel(getResources().getString(R.string.japanese), JAPANESE, R.drawable.ic_japan));
        mData.add(new LocaleModel(getResources().getString(R.string.bengali), BENGALI, R.drawable.ic_bangladesh));
        mData.add(new LocaleModel(getResources().getString(R.string.russian), RUSSIAN, R.drawable.ic_russia));
        mData.add(new LocaleModel(getResources().getString(R.string.indonesian), INDONESIAN, R.drawable.ic_indonesia));
        mData.add(new LocaleModel(getResources().getString(R.string.italian), ITALIAN, R.drawable.ic_italy));
        mData.add(new LocaleModel(getResources().getString(R.string.spanish), SPANISH, R.drawable.ic_spain));
        mData.add(new LocaleModel(getResources().getString(R.string.french), FRENCH, R.drawable.ic_france));
        //mData.add(new LocaleModel(getResources().getString(R.string.german), GERMAN, R.drawable.ic_germany));
        mData.add(new LocaleModel(getResources().getString(R.string.portuguese), PORTUGUESE, R.drawable.ic_brazil));
        mData.add(new LocaleModel(getResources().getString(R.string.thai), THAI, R.drawable.ic_thailand));
        mData.add(new LocaleModel(getResources().getString(R.string.african), AFRICAN, R.drawable.ic_africa));
        mData.add(new LocaleModel(getResources().getString(R.string.armanian), ARMANIA, R.drawable.ic_armenia));
        mData.add(new LocaleModel(getResources().getString(R.string.azerbaijaan), AZERBAIJAAN, R.drawable.ic_azerbaijaan));
        mData.add(new LocaleModel(getResources().getString(R.string.balrusian), BALRUSIAN, R.drawable.ic_belarus));
        mData.add(new LocaleModel(getResources().getString(R.string.bulgarian), BULGARIAN, R.drawable.ic_bulgeria));
        mData.add(new LocaleModel(getResources().getString(R.string.krotian), CROATIA, R.drawable.ic_crotia));
        mData.add(new LocaleModel(getResources().getString(R.string.dannish), DANNISH, R.drawable.ic_denamrk));
        mData.add(new LocaleModel(getResources().getString(R.string.dutch), DUTCH, R.drawable.ic_dutch));
        mData.add(new LocaleModel(getResources().getString(R.string.philipine), PHILIPINE, R.drawable.ic_philippines));
        mData.add(new LocaleModel(getResources().getString(R.string.finissh), FINNISH, R.drawable.ic_finnsih));
        mData.add(new LocaleModel(getResources().getString(R.string.hibru), HIBRU, R.drawable.ic_israel_hibu));
        mData.add(new LocaleModel(getResources().getString(R.string.iceland), ICELANDIC, R.drawable.ic_iceland));
        mData.add(new LocaleModel(getResources().getString(R.string.kzakistan), KAZAKISTAN, R.drawable.ic_kzakistab));
        mData.add(new LocaleModel(getResources().getString(R.string.korean), KOREAN, R.drawable.ic_korea));
        mData.add(new LocaleModel(getResources().getString(R.string.malaysian), MALAYSIA, R.drawable.ic_malaysian));
        mData.add(new LocaleModel(getResources().getString(R.string.norogwain), NORWAY, R.drawable.ic_norwagian));
        mData.add(new LocaleModel(getResources().getString(R.string.polish), POLISH, R.drawable.ic_polish));
        mData.add(new LocaleModel(getResources().getString(R.string.romanian), ROMANIA, R.drawable.ic_romania));
        mData.add(new LocaleModel(getResources().getString(R.string.slovak), SLOVAK, R.drawable.ic_slovakia));
        mData.add(new LocaleModel(getResources().getString(R.string.swedish), SWEDISH, R.drawable.ic_swedish));
        mData.add(new LocaleModel(getResources().getString(R.string.uzbik), UZBIK, R.drawable.ic_uzbik));
        mData.add(new LocaleModel(getResources().getString(R.string.vietnam), VIETNAM, R.drawable.ic_vietnam));

        return mData;
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    public void initComponents(){

        context = ActivitySelectLanguage.this;
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.languagesRecycler);
        sharedPrefClass=new SharedPrefClass(getApplicationContext());
        themeSwitchState=sharedPrefClass.getThemeStatefromShared();
    }
}