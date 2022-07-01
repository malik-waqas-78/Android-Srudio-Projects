package com.test.speedmeter;

import static com.test.speedmeter.MainActivity.appDataBase;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapHoldingEventArgs;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapStyleSheets;
import com.microsoft.maps.MapView;
import com.microsoft.maps.OnMapHoldingListener;

public class MarkArea extends AppCompatActivity {

    private MapView mMapView;
    MapIcon pushpin;
    Bitmap pinBitmap;
    double zoom=15.0;
    private MapElementLayer mPinLayer;

    String str="p";
    int i=1;

    MyLocationRect myLocationRect =new MyLocationRect();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_area);

        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view_area)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);
        mPinLayer = new MapElementLayer();
        mMapView.zoomTo(zoom);
        mMapView.getLayers().add(mPinLayer);
        pinBitmap = bitmapFromDrawableRes(this,R.drawable.red_marker);
        mMapView.setMapStyleSheet(MapStyleSheets.aerialWithOverlay());
        TextView tv=findViewById(R.id.tv_hint);

            double lat=getIntent().getDoubleExtra("lat",0.0);
            double longi=getIntent().getDoubleExtra("longi",0.0);
            Geopoint location = new Geopoint(lat,longi);
            mMapView.setScene(
                    MapScene.createFromLocationAndZoomLevel(location, zoom),
                    MapAnimationKind.LINEAR);




        mMapView.addOnMapHoldingListener(new OnMapHoldingListener() {
            @Override
            public boolean onMapHolding(MapHoldingEventArgs mapHoldingEventArgs) {
                str="p"+i;
                Geopoint location=mapHoldingEventArgs.location;
                switch (i){
                    case 1:{
                        myLocationRect.setPoint1(location.getPosition());
                        break;
                    }
                    case 2:{
                        myLocationRect.setPoint2(location.getPosition());
                        break;
                    }
                    case 3:{
                        myLocationRect.setPoint3(location.getPosition());
                        break;
                    }
                    case 4:{
                        myLocationRect.setPoint4(location.getPosition());
                        break;
                    }
                    default:{

                    }
                }
                addPinToMap(location.getPosition().getLatitude(),
                        location.getPosition().getLongitude());
                i++;
                if(i<=4){

                    tv.setText("Mark Point "+i);
                }else{

                    tv.setText("All points Marked");
                    mMapView.removeOnMapHoldingListener(this);
                }
                return true;
            }
        });

        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocationRect.isCompleted()){
                    appDataBase.dbLocationRectDao().insert(myLocationRect.getDbLocationRect());
                    finish();
                }else{
                    finish();
                }
            }
        });



    }

    private void addPinToMap(double lat,double longi) {
        Geopoint location = new Geopoint(lat,longi);
        String title =str;       // title to be shown next to the pin
        // your pin graphic (optional)


        if(i!=1){
            zoom=mMapView.getZoomLevel();
        }

        pushpin = new MapIcon();

        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(new MapImage(pinBitmap));
        mPinLayer.getElements().add(pushpin);


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


}