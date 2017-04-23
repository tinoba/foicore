package eu.tinoba.androidarcitecturetemplate.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.MarkerModel;
import timber.log.Timber;

public class ShopMapActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                 GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;

    Location mLastLocation;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<MarkerModel> markerModelList = new ArrayList<>();
        markerModelList.add(
                new MarkerModel(new LatLng(46.315148, 16.343480), "Prodavaonica br 1.", "RUĐERA BOŠKOVIĆA 25", R.drawable.shop_icon));
        markerModelList.add(
                new MarkerModel(new LatLng(46.312312, 16.326887), "Prodavaonica br 2.", "GUSTAVA KRKLECA 1/A", R.drawable.shop_icon));
        markerModelList.add(
                new MarkerModel(new LatLng(46.308642, 16.339269), "Prodavaonica br 3.", "AUGUSTA ŠENOE 6", R.drawable.shop_icon));
        markerModelList.add(
                new MarkerModel(new LatLng(46.303736, 16.337074), "Prodavaonica br 4.", "ZAGREBAČKA 10", R.drawable.shop_icon));
        markerModelList.add(
                new MarkerModel(new LatLng(46.300734, 16.323599), "Prodavaonica br 5.", "MATKA LAGINJE 15", R.drawable.shop_icon));

        addMarkerToMap(markerModelList);
        mMap.setOnMarkerClickListener(marker -> {
            //TODO CALCULATE FASTEST WAY TO GET THERE
            Toast.makeText(this, "IZRACUNAJ PUT za: " + marker.getTitle() , Toast.LENGTH_SHORT).show();
            return true;
        });
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(zoom);
    }

    private void addMarkerToMap(List<MarkerModel> markerModelList) {
        for (final MarkerModel markerModel : markerModelList) {
            MarkerOptions markerOptions = new MarkerOptions().position(markerModel.getLatLng()).title(markerModel.getShopName());
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                    getMarkerBitmapFromView(getResources().getDrawable(markerModel.getShopImageId()), markerModel.getShopName(),
                                            markerModel.getShopAddress())));
            mMap.addMarker(markerOptions);
        }
    }

    private Bitmap getMarkerBitmapFromView(Drawable drawable1, String outletName, String outletProduct) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.landmark_image);
        TextView outletTitle = (TextView) customMarkerView.findViewById(R.id.landmark_title);
        TextView outletProductCustom = (TextView) customMarkerView.findViewById(R.id.landmard_info);

        markerImageView.setImageDrawable(drawable1);
        outletTitle.setText(outletName);
        outletProductCustom.setText(outletProduct);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                                                    Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) {
            drawable.draw(canvas);
        }
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                                                                                                                                                                          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (mLastLocation != null) {
            Timber.e("LAT", String.valueOf(mLastLocation.getLatitude()));
            Timber.e("LONG", String.valueOf(mLastLocation.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
            mMap.animateCamera(zoom);
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }
}
