package com.example.mydish;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geometry;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkCreatedCallback;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

public class MapActivity extends LowerPanel {
    boolean shopsShow = false;
    MapView mapView;
    MapKit mapKit;
    TrafficLayer traffic;
    ImageButton trafficButton;
    ImageButton shopButton;
    UserLocationLayer location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("9de8eaff-b01a-414d-93d0-adaad266a78f");
        setContentView(R.layout.map);
        ImageButton favoriteButton = findViewById(R.id.map_button);
        favoriteButton.setImageResource(R.mipmap.chosen_map);
        mapView = findViewById(R.id.map_view);
        mapKit = MapKitFactory.getInstance();
        trafficButton = findViewById(R.id.traffic_button);
        traffic = mapKit.createTrafficLayer(mapView.getMapWindow());
        traffic.setTrafficVisible(false);
        trafficButton.setImageResource(R.mipmap.traffic_light);
        shopButton = findViewById(R.id.shop_button);
        shopButton.setImageResource(R.mipmap.shop);
        requestLocation();
    }

    public void showTraffic(View view){
        if (!traffic.isTrafficVisible()) {
            traffic.setTrafficVisible(true);
            trafficButton.setImageResource(R.mipmap.traffic_light_used);
        } else {
            traffic.setTrafficVisible(false);
            trafficButton.setImageResource(R.mipmap.traffic_light);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9999) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserOnMap();
            } else {
                mapView.getMapWindow().getMap().move(new CameraPosition(
                        new Point(56.010548, 92.852571),
                        11,
                        0,
                        0)
                );
            }
        }
    }

    private void showUserOnMap(){
        location = mapKit.createUserLocationLayer(mapView.getMapWindow());
        location.setObjectListener(new UserLocationObjectListener() {
            @Override
            public void onObjectAdded(@NonNull UserLocationView userLocationView) {
                userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                        MapActivity.this, R.mipmap.navigator));
            }
            @Override
            public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
            }
            @Override
            public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
                userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                        MapActivity.this, R.mipmap.navigator));
            }
        });
        LocationManager locationManager = mapKit.createLocationManager();
        locationManager.requestSingleUpdate(new LocationListener() {

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }

            @Override
            public void onLocationUpdated(@NonNull Location location) {
                mapView.getMapWindow().getMap().move(new CameraPosition(
                               location.getPosition(),
                               11,
                               0,
                               0)
                );
            }
        });
        location.setVisible(true);
    }
    private void requestLocation(){
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    9999);
        } else {
            showUserOnMap();
        }
    }

    public void showShops(View view) {
        if (!shopsShow) {
            SearchManager searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
            SearchOptions searchOptions = new SearchOptions();
            searchOptions.setSearchTypes(SearchType.BIZ.value);
            searchOptions.setResultPageSize(100);
            searchManager.submit(
                    "Спортивное питание",
                    VisibleRegionUtils.toPolygon(mapView.getMapWindow().getMap().getVisibleRegion()),
                    searchOptions,
                    new Session.SearchListener() {
                        @Override
                        public void onSearchResponse(@NonNull Response response) {
                            Log.d("Shops",String.valueOf(response.getCollection().getChildren().size()));
                            for (GeoObjectCollection.Item geoItem : response.getCollection().getChildren()) {
                                GeoObject geoObj = geoItem.getObj();
                                if (geoObj.getGeometry() != null) {
                                    for (Geometry geometry : geoObj.getGeometry()) {
                                        addPointToMap(geometry.getPoint(), geoObj.getName());
                                    }
                                }
                            }
                        }


                        @Override
                        public void onSearchError(@NonNull Error error) {
                            Log.d("ErrorSearch",error.toString());
                        }
                    }
            );
            shopsShow = true;
            shopButton.setImageResource(R.mipmap.shop_used);
        } else {
            clearMap();
            shopsShow = false;
            shopButton.setImageResource(R.mipmap.shop);
        }
    }

    private void clearMap() {
        mapView.getMapWindow().getMap().getMapObjects().clear();
    }
    private void addPointToMap(Point point, String name) {
        PlacemarkCreatedCallback placemark = placemarkMapObject -> {
            placemarkMapObject.setGeometry(point);
            placemarkMapObject.setIcon(ImageProvider.fromResource(getApplicationContext(), R.mipmap.placemark));
            placemarkMapObject.addTapListener((mapObject, point1) -> {
                double zoom = mapView.getMapWindow().getMap().getCameraPosition().getZoom();
                if (zoom >= 14) {
                    placemarkMapObject.setText(name);
                } else {
                    placemarkMapObject.setText("");
                }
                return true;
            });
        };
        mapView.getMapWindow().getMap().getMapObjects()
                .addPlacemark(placemark);
    }



    @Override
    protected void onStop() {
        mapView.onStop();
        mapKit.onStop();
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapKit.onStart();
        mapView.onStart();
    }
}
