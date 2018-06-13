package com.hello.handicab1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailCallActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    MapFragment mapFr; //
    GoogleMap map; //지도관련 모든 메소드를 가지고 있다.
    public Handler mHandler;
    GPSTracker gps =null;
    public static int RENEW_GPS = 1;
    UiSettings uiSettings; //구글맵의 Ui환경을 정한다.
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    MarkerOptions markerOptions;
    double latitude;//위도
    double longitude;//경도
    LatLng Loc;
    DatabaseReference db;
    SharedPreferences auto;
    boolean addFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_call);
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        db = FirebaseDatabase.getInstance().getReference("User");
        PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);

        mapFr = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
        mapFr.getMapAsync(this);
/////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////////////////////

        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                Log.v("yong", "Start Place: " + place.getName());
                Log.v("yong","lat: "+place.getLatLng().latitude);
                Log.v("yong","lon: "+place.getLatLng().longitude);

                Loc=new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
                Toast.makeText(getApplication(),"선택한 위치 \n"+"위도 : "+place.getLatLng().latitude+"\t"+"경도 : "+place.getLatLng().longitude,Toast.LENGTH_LONG).show();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc,13));
                markerOptions = new MarkerOptions();
                markerOptions.position(Loc);
                markerOptions.title("즐겨찾기 추가");
                //markerOptions.snippet("석원이 집있슴2");
                map.addMarker(markerOptions);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.v("yong", "An error occurred: " + status);
            }
        });


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        uiSettings =map.getUiSettings(); // 구글맵의 ui환경가져옴.
        uiSettings.setZoomControlsEnabled(true); //줌 기능가능하게만듬
        markerOptions = new MarkerOptions();//마커옵션 객체생성.
        /*latitude=37.542623;
        longitude=127.075886;
        Loc=new LatLng(latitude,longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc,13)); //시작하는 위치를 위에 위도,경도로둠.
        texiPoint(map); //택시위치 선언해줌.
        mapPoint(map);
        map.setOnInfoWindowLongClickListener(infoWindowLongClickListener);*/
        map.setOnInfoWindowLongClickListener(infoWindowLongClickListener);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
    GoogleMap.OnInfoWindowLongClickListener infoWindowLongClickListener =
            new GoogleMap.OnInfoWindowLongClickListener(){
                @Override
                public void onInfoWindowLongClick(Marker marker) {
                    final LatLng checkposition = marker.getPosition();
                    final String checkstr = "위도" + checkposition.latitude + "경도" + checkposition.longitude; //선택한곳 위도경도


                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                                if(userInformation.getUserPhone().equals(auto.getString("inputPhone",null))){
                                    Log.v("yong","favvvvv :    " + userInformation.getUserFavorite());
                                    if(addFavorite == false){
                                        db.child(userInformation.getUserPhone()).child("userFavorite").setValue(userInformation.getUserFavorite()+" "+checkposition.latitude+" "+checkposition.longitude);
                                        Toast.makeText(getApplication(), "즐겨찾기에 추가하였습니다.", Toast.LENGTH_LONG).show();//즐겨찾기 연락처db에 넣으면된다.
                                        addFavorite = true;
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            };
//    void callPlacePicker() {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
