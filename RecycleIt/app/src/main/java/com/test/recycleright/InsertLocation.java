package com.test.recycleright;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.Geoposition;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementCollection;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapHoldingEventArgs;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapStyleSheets;
import com.microsoft.maps.MapView;
import com.microsoft.maps.OnMapHoldingListener;


public class InsertLocation extends AppCompatActivity  {

    FusedLocationProviderClient mFusedLocationClient;
    boolean isLocationPicked=false;
    private MapElementLayer mPinLayer;

    private MapView mMapView;
    MapIcon pushpin;
    Bitmap pinBitmap;
    double zoom=10.0;
    private static final Geopoint LAKE_WASHINGTON = new Geopoint(47.609466, -122.265185);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_location);

        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);
        mPinLayer = new MapElementLayer();
        mMapView.zoomTo(zoom);
        mMapView.getLayers().add(mPinLayer);
         pinBitmap = bitmapFromDrawableRes(this,R.drawable.red_marker);
        mMapView.setMapStyleSheet(MapStyleSheets.roadLight());

        mMapView.addOnMapHoldingListener(new OnMapHoldingListener() {
            @Override
            public boolean onMapHolding(MapHoldingEventArgs mapHoldingEventArgs) {
                Geoposition geopoint=mapHoldingEventArgs.location.getPosition();
                isLocationPicked=true;
                addAnnotationToMap(geopoint.getLatitude(),geopoint.getLongitude());
                return false;
            }
        });

        findViewById(R.id.layout_ail).findViewById(R.id.iv_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(34);
                        finish();
                    }
                }
        );
        findViewById(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(getResources().getString(R.string.total_points),
                        getResources().getString(R.string.your_total_points_are)+
                                " "+MySharedPreferences.getInstance(InsertLocation.this).getScores());
            }
        });

        //handleLocation();

        Button btn = findViewById(R.id.btn_next_il);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleInfo.getObj().getLatitued() != null) {
                    startActivityForResult(new Intent(InsertLocation.this, GetTimeAndDate.class), 3464);
                } else {
                    Toast.makeText(InsertLocation.this, "Location Not Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void addAnnotationToMap(double lat, double longi) {
        Geopoint location = new Geopoint(lat,longi);
        String title ="PickUp Location";       // title to be shown next to the pin
          // your pin graphic (optional)

        if(pushpin!=null){
            mPinLayer.getElements().remove(pushpin);
        }

        mMapView.getLayers().remove(mPinLayer);
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        zoom=mMapView.getZoomLevel();
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(location, zoom),
                MapAnimationKind.LINEAR);

        pushpin = new MapIcon();
        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(new MapImage(pinBitmap));


        mPinLayer.getElements().add(pushpin);

        RecycleInfo.getObj().setLongitued(String.valueOf(longi));
        RecycleInfo.getObj().setLatitued(String.valueOf(lat));

    }


    private Bitmap bitmapFromDrawableRes(Context context, @DrawableRes int resourceId) {


        return convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId));
    }

    private Bitmap convertDrawableToBitmap(Drawable sourceDrawable) {
        if (sourceDrawable == null) {
            return null;
        }
        if (sourceDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) sourceDrawable).getBitmap();
        } else {
// copying drawable object to not manipulate on the same reference
            Drawable.ConstantState constantState = sourceDrawable.getConstantState() != null ? sourceDrawable.getConstantState() : null;
            Drawable drawable = constantState.newDrawable().mutate();
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public void handleLocation() {
        if (hasLocationPermission()) {
            if (!isGPSOn()) {
                enableLoc();
            } else {
                getMyLocation();
            }
        } else {
            requestLocationPermission();
        }
    }

    private void getMyLocation() {
        requestNewLocationData();
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            RecycleInfo.getObj().setLongitued(String.valueOf(mLastLocation.getLongitude()));
            RecycleInfo.getObj().setLatitued(String.valueOf(mLastLocation.getLatitude()));

            if(!isLocationPicked) {
                addAnnotationToMap(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                requestNewLocationData();
            }else {
                mFusedLocationClient.removeLocationUpdates(this);
            }
        }
    };

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                927
        );
    }

    public boolean hasLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
        ) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 927 && grantResults.length > 0) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                handleLocation();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(LAKE_WASHINGTON, zoom),
                MapAnimationKind.NONE);
        handleLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    public boolean isGPSOn() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                gps_enabled = lm.isLocationEnabled();
            } else
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            //show msg exception
        }
        return gps_enabled;
    }


    private void enableLoc() {
        GoogleApiClient googleApiClient = null;
        GoogleApiClient finalGoogleApiClient = googleApiClient;
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        finalGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                                                   @Override
                                                   public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                                   }
                                               }
                ).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000l);
        locationRequest.setFastestInterval(1000 / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback() {
            @Override
            public void onResult(@NonNull Result result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    InsertLocation.this,
                                    32
                            );
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 32) {
            handleLocation();
        } else if (resultCode == 3464) {
            setResult(3464);
            finish();
        }
    }
    private void showDialogBox(String title,String msg) {
        Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        TextView tvTitle=dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView text=dialog.findViewById(R.id.tv_msg);
        text.setText(msg);
        Button btnOk=dialog.findViewById(R.id.btn_done);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}