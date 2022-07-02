package screen.lock.screenlock;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.AdRequest;



public class SplashActivity extends AppCompatActivity {
    TextView privacyText;
   // com.google.android.gms.ads.AdView  mAdView;
    private static final int PERMISSION_CODE = 1001;
    private static int splash_time_out = 2000;
    RelativeLayout relativeLayout;
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (Build.VERSION.SDK_INT < 23) {
                SplashActivity.this.NextActivity();
            } else if (SplashActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) ==PackageManager.PERMISSION_GRANTED) {
                SplashActivity.this.NextActivity();
            } else {
                Toast.makeText(SplashActivity.this, "Permission not granted Yet", Toast.LENGTH_SHORT).show();
            }
        }
    });
    ActivityResultLauncher<Intent> mGetContent2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (Build.VERSION.SDK_INT < 23) {
                SplashActivity.this.StartNextActivity();
            } else if (!Settings.canDrawOverlays(SplashActivity.this)) {
                StartNextActivity();
            } else {
                SplashActivity.this.StartNextActivity();
            }
        }
    });

    /* access modifiers changed from: private */
    public void StartNextActivity() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        relativeLayout=(RelativeLayout)findViewById(R.id.close);
        privacyText=(TextView)findViewById(R.id.textpolicy);
        getSupportActionBar().hide();
       // loadAd();
//        loadadtop();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(SplashActivity.this).inflate(R.layout.rationaledialog, viewGroup, false);
                        alertDialog.setView(dialogView);
                        RelativeLayout buttonAllowRel = (RelativeLayout) dialogView.findViewById(R.id.permissionAllowRel);
                        RelativeLayout buttonCancelRel = (RelativeLayout) dialogView.findViewById(R.id.permissionDenyRel);

                        AlertDialog dialog = alertDialog.create();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();

                        buttonAllowRel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (Build.VERSION.SDK_INT < 23) {
                                    NextActivity();
                                } else if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
                                } else {
                                    NextActivity();
                                }
                            }
                        });
                        buttonCancelRel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        NextActivity();
                    }
                }else{
                    NextActivity();
                }
            }
        });
        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_Dialog();
            }
        });
    }
    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(SplashActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.privacylayout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NextActivity();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= 23) {
                     if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
                                ViewGroup viewGroup=findViewById(android.R.id.content);
                                View dialogView= LayoutInflater.from(SplashActivity.this).inflate(R.layout.rationale2,viewGroup,false);
                                alertDialog.setView(dialogView);
                                RelativeLayout buttonAllowRel=(RelativeLayout)dialogView.findViewById(R.id.permissionAllowRel);
                                RelativeLayout buttonCancelRel=(RelativeLayout)dialogView.findViewById(R.id.permissionDenyRel);

                                AlertDialog dialog=alertDialog.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.show();

                                buttonAllowRel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        mGetContent.launch(intent);
                                    }
                                });
                                buttonCancelRel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                     }else {
                         Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                         return;
                     }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void NextActivity() {
        RelativeLayout buttonDeny,buttonAllow;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView= LayoutInflater.from(SplashActivity.this).inflate(R.layout.permission_custom_dialog,viewGroup,false);
               buttonDeny=dialogView.findViewById(R.id.denyrel);
               buttonAllow=dialogView.findViewById(R.id.allowrel);

                alertDialog.setView(dialogView);
                AlertDialog alertDialog1=alertDialog.create();
                alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog1.show();

                buttonAllow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                        SplashActivity.this.mGetContent2.launch(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + SplashActivity.this.getPackageName())));
                    }
                });
                buttonDeny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                        Toast.makeText(SplashActivity.this, "Permission Denied App Overlay cant work now", Toast.LENGTH_SHORT).show();
                        SplashActivity.this.StartNextActivity();
                    }
                });


            }
            else {
                StartNextActivity();
            }
        }
        else {
            StartNextActivity();
        }
    }
//    public void loadAd(){
//        AdView adView=new AdView(Splash.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.bannerAdContainer);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
//    public void loadadtop(){
//        AdView adView=new AdView(Splash.this, getApplicationContext().getResources().getString(R.string.fb_banner_ad), AdSize.BANNER_HEIGHT_50);
//        AdListener adListener=new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//        LinearLayout adContainer=(LinearLayout)findViewById(R.id.lineartopbanner);
//        adContainer.addView(adView);
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//    }
}
