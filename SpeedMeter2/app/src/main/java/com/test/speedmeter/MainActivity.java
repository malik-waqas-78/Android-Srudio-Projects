package com.test.speedmeter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.microsoft.maps.GeoboundingBox;
import com.microsoft.maps.Geopath;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.Geoposition;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapPolygon;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapStyleSheets;
import com.microsoft.maps.MapView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView txtLat;
    ConstraintLayout cardView;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    private static final long INTERVAL = 3000;
    private static final long FASTEST_INTERVAL = INTERVAL / 2;

    private MapView mMapView;
    MapIcon pushpin;
    Bitmap pinBitmap;
    double zoom = 13.0;
    private MapElementLayer mPinLayer;
    MapElementLayer layer=null;
    static AppDataBase appDataBase;

    Location location;

    ArrayList<MyLocationRect> locationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = (TextView) findViewById(R.id.et_speed);
        cardView = findViewById(R.id.back);

        appDataBase = Room.databaseBuilder(
                getApplicationContext(),
                AppDataBase.class, "location_database"
        ).allowMainThreadQueries()
                .build();

        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout) findViewById(R.id.map_view_area)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);
        mPinLayer = new MapElementLayer();
        mMapView.zoomTo(zoom);
        mMapView.getLayers().add(mPinLayer);
        pinBitmap = bitmapFromDrawableRes(this, R.drawable.red_marker);
        mMapView.setMapStyleSheet(MapStyleSheets.aerialWithOverlay());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Button btn = findViewById(R.id.btn_mark_area);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = location.getLatitude();
                double longi = location.getLongitude();
                Intent intent = new Intent(MainActivity.this, MarkArea.class);
                intent.putExtra("lat", lat);
                intent.putExtra("longi", longi);
                MainActivity.this.startActivity(intent);
            }
        });


    }


    private void getSpeed(double dSpeed, Location location) {
        this.location = location;
        double a = 3.6 * (dSpeed);
        int kmhSpeed = (int) (Math.round(a));
        txtLat.setText(kmhSpeed + "\nKm/h");

        if (kmhSpeed > 60) {
            if (contains(location)) {
                cardView.setBackgroundColor(getResources().getColor(R.color.red));
                vibrate();
            } else {
                cardView.setBackgroundColor(getResources().getColor(R.color.green));
            }
        } else {
            cardView.setBackgroundColor(getResources().getColor(R.color.green));
        }

        addPinToMap(location, "location");

    }

    private boolean contains(Location location) {
        for (MyLocationRect check : locationsList) {
            GeoboundingBox rect = new GeoboundingBox(check.getGeoPositionsArrayList());
            if (location.getLatitude() <= rect.getNorthwestCorner().getLatitude() &&
                    location.getLatitude() >= rect.getSoutheastCorner().getLatitude() &&
                    location.getLongitude() >= rect.getNorthwestCorner().getLongitude() &&
                    location.getLongitude() <= rect.getSoutheastCorner().getLongitude()) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            p3=(float)mLastLocation.getLongitude();
//            p4= (float) mLastLocation.getLatitude();
            double dSpeed = mLastLocation.getSpeed();
            getSpeed(dSpeed, mLastLocation);
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocationData();
            }
        }
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        markLocations();
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                requestNewLocationData();
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();

            }
        } else {
            requestPermissions();
        }

    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();

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

    void highlightArea(ArrayList<Geoposition> geopoints) {

        MapPolygon mapPolygon = new MapPolygon();
        mapPolygon.setPaths(Arrays.asList(new Geopath(geopoints)));
        mapPolygon.setFillColor(getResources().getColor(R.color.red_area, null));
        mapPolygon.setFillColor(getResources().getColor(R.color.red_area, null));
        mapPolygon.setStrokeColor(Color.BLUE);
        mapPolygon.setStrokeWidth(3);
        mapPolygon.setStrokeDashed(false);
        layer.setZIndex((float) zoom);
        layer.getElements().add(mapPolygon);
        mMapView.getLayers().add(layer);
    }

    private void markLocations() {

        ArrayList<DbLocationRect> list = new ArrayList<>();
        list.addAll(appDataBase.dbLocationRectDao().getAll());

        for (DbLocationRect location : list) {
            locationsList.add(getLocationRect(location));
        }
        if(layer!=null){
            mMapView.getLayers().remove(layer);
        }
        layer= new MapElementLayer();
        for (MyLocationRect myLocationRect : locationsList) {
            highlightArea(myLocationRect.getGeoPositionsArrayList());
        }

    }

    public MyLocationRect getLocationRect(DbLocationRect dbLocationRect) {
        MyLocationRect rect = new MyLocationRect(dbLocationRect);
        return rect;
    }

    private void addPinToMap(Location mLocation, String str) {
        Geopoint location = new Geopoint(mLocation.getLatitude(), mLocation.getLongitude());
        String title = str ; // your pin graphic (optional)
        mMapView.getLayers().remove(mPinLayer);
        zoom = mMapView.getZoomLevel();
        if (pushpin != null) {
            mPinLayer.getElements().remove(pushpin);
            mPinLayer.getElements().clear();
        } else {
            mMapView.setScene(
                    MapScene.createFromLocationAndZoomLevel(location, zoom),
                    MapAnimationKind.LINEAR);

        }

        pushpin = new MapIcon();
        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(new MapImage(pinBitmap));
        mPinLayer.getElements().add(pushpin);
        mMapView.getLayers().add(mPinLayer);
    }

}