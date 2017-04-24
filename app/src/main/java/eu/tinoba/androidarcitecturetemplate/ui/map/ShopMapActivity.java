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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.MarkerModel;
import timber.log.Timber;

public class ShopMapActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                 GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                                                  Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

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
            LatLng origin;
            LatLng dest;
            String url;
            FetchUrl fetchUrl;
            switch (marker.getTitle()) {
                case "Prodavaonica br 1.":
                    mMap.clear();
                    addMarkerToMap(markerModelList);
                    Timber.e("1");
                    origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    dest = new LatLng(46.315148, 16.343480);

                    url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    break;
                case "Prodavaonica br 2.":
                    mMap.clear();
                    addMarkerToMap(markerModelList);
                    origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    dest = new LatLng(46.312312, 16.326887);

                    url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    Timber.e("2");
                    break;
                case "Prodavaonica br 3.":
                    mMap.clear();
                    addMarkerToMap(markerModelList);
                    Timber.e("3");
                    origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    dest = new LatLng(46.308642, 16.339269);

                    url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    break;
                case "Prodavaonica br 4.":
                    mMap.clear();
                    addMarkerToMap(markerModelList);
                    Timber.e("4");
                    origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    dest = new LatLng(46.303736, 16.337074);

                    url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    break;
                case "Prodavaonica br 5.":
                    mMap.clear();
                    addMarkerToMap(markerModelList);
                    Timber.e("5");
                    origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    dest = new LatLng(46.300734, 16.323599);

                    url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    break;
            }
            return true;
        });
    }

    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        MarkerPoints = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());
            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
            }

            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Timber.e("LAT", String.valueOf(mLastLocation.getLatitude()));
            Timber.e("LONG", String.valueOf(mLastLocation.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
            mMap.animateCamera(zoom);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                  MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                  MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                                                          Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
