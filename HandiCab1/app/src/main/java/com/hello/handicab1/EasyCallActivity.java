package com.hello.handicab1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EasyCallActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    MapFragment mapFr; //
    GoogleMap map; //지도관련 모든 메소드를 가지고 있다.
    UiSettings uiSettings; //구글맵의 Ui환경을 정한다.
    LatLng Loc,Loc_taxi; //위도,경도 좌표표현 / 택시좌표찍으려고 선언
    CameraUpdateFactory cameraUpdateFactory; //지도를 변환하는 CameraUpdate객체 생성.
    double latitude;//위도
    double longitude;//경도
    Marker marker;//특정위치에 위치하는 아이콘
    MarkerOptions markerOptions;//marker설정을 위한 옵션을 정의
    Button btn_gps; boolean gps_flag=true; //gps껏다/켯다.
    public static int RENEW_GPS = 1;
    public Handler mHandler;
    GPSTracker gps =null;
    int pointcheck=0; // 택시마크인지 위치체크마크인지 구분
    int message_check=0;//메세지를 한 택시에만 보냈는지 체크하기위해서.

    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_call);
        setPermission();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        init();
    }

    public void init(){
        mapFr = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFr.getMapAsync(this);
        btn_gps = (Button)findViewById(R.id.btn_gps);
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==RENEW_GPS){
                    makeNewGpsService();
                }
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        uiSettings =map.getUiSettings(); // 구글맵의 ui환경가져옴.
        uiSettings.setZoomControlsEnabled(true); //줌 기능가능하게만듬
        markerOptions = new MarkerOptions();//마커옵션 객체생성.
        latitude=37.542623;
        longitude=127.075886;
        Loc=new LatLng(latitude,longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc,13)); //시작하는 위치를 위에 위도,경도로둠.
        texiPoint(map); //택시위치 선언해줌.
        mapPoint(map);
        map.setOnInfoWindowLongClickListener(infoWindowLongClickListener);


    }
    //권한 확인 함수
    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
    }
    //Gps
    public void btn_gps(View view) {

        if (gps == null) {
            gps = new GPSTracker(EasyCallActivity.this, mHandler);
        } else {
            gps.Update();
        }

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getApplicationContext(), "현재위치는 - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        if(gps_flag) {
            gps_flag=false;
            LatLng SEOUL2 = new LatLng(gps.getLatitude(), gps.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEOUL2);
            markerOptions.title("현위치");
            //markerOptions.snippet("석원이 집있슴2");
            this.map.addMarker(markerOptions);


            map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL2));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }else{//gps버튼 다시누르면 저장해둔거 빼고 다 지우면된다. 아래는 석원집만 저장해둔거.
            //즐겨찾기 할때 저장해놓은 정보들 여기다 넣어서 for문돌려서 addMarker시키면될것같다.
            gps_flag=true;
            map.clear();
            texiPoint(this.map);
        }
    }
    //gps
    public void makeNewGpsService() {
        if (gps == null) {
            gps = new GPSTracker(EasyCallActivity.this, mHandler);
        } else {
            gps.Update();
        }

    }
    //택시위치찍어줌 -1.
    public void texiPoint(GoogleMap map){
        db = FirebaseDatabase.getInstance().getReference("TaxiDriver");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TaxiDriver taxiDriver = snapshot.getValue(TaxiDriver.class);
                    if(taxiDriver.driverAvailable.equals("possible")){
                        LatLng loc;
                        loc = new LatLng(taxiDriver.driverLatitude,taxiDriver.driverLongitude);textPointcheck(loc);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        double lat;//위도
//        double lon;//경도
//        LatLng loc;
//        loc = new LatLng(37.543271,127.072286);textPointcheck(loc);
//        loc = new LatLng(37.525271,127.075086);textPointcheck(loc);
//        loc = new LatLng(37.557271,127.080286);textPointcheck(loc);
//        loc = new LatLng(37.537271,127.077086);textPointcheck(loc);
//        loc = new LatLng(37.568024,127.070489);textPointcheck(loc);
//        loc = new LatLng(37.548186,127.051629);textPointcheck(loc);
//        loc = new LatLng(37.517259,127.050826);textPointcheck(loc);
//        loc = new LatLng(37.514834,127.079935);textPointcheck(loc);
//        loc = new LatLng(37.544454,127.080194);textPointcheck(loc);
    }
    public void textPointcheck(LatLng loc){
        markerOptions.position(loc)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.call_taximark))
                .title("호출하기\n");
        map.addMarker(markerOptions);
        map.setOnMarkerClickListener(this);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng g=marker.getPosition();
        double a =g.latitude;
        double b =g.longitude;
        if(pointcheck!=0){//위치나타내는 마커일경우 같은곳 또 못보니까 사라지게해야함
            pointcheck=pointcheck-1;
            Toast.makeText(getApplication(),"위치선택 취소 \n"+"위도:"+a+"\t경도:"+b,Toast.LENGTH_LONG).show();
            marker.remove();
            return false;
        }
        Toast.makeText(getApplication(),"택시선택 \n"+"위도:"+a+"\t경도:"+b,Toast.LENGTH_LONG).show();
        return false;
    }
    //지도에 터치하면 마크생김
    public void mapPoint(final GoogleMap map) {

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double a= latLng.latitude;
                double b= latLng.longitude;
                MarkerOptions options = new MarkerOptions();
                options.position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.call_taximark))
                        .alpha(0.5f);
                map.addMarker(options);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                Toast.makeText(getApplication(),"위치체크\n"+"위도:"+a+"\t경도:"+b,Toast.LENGTH_LONG).show();
                pointcheck=pointcheck+1;
            }
        });
    }
    //택시연결눌러서 택시호출합니다.
    GoogleMap.OnInfoWindowLongClickListener infoWindowLongClickListener =
            new GoogleMap.OnInfoWindowLongClickListener(){
                @Override
                public void onInfoWindowLongClick(Marker marker) {
//                    if(message_check>0){
//                        Toast.makeText(getApplication(), "이미 예약한 택시가 있습니다..", Toast.LENGTH_LONG).show();
//                    }
//                    else if(message_check==0) {
                    LatLng checkposition = marker.getPosition();
                    String checkstr = "위도" + checkposition.latitude + "경도" + checkposition.longitude;
                    Toast.makeText(getApplication(), "택시에 문자를 전송하였습니다.", Toast.LENGTH_LONG).show();//택시기사 연락처db에넣고 문자 보내면됨.
//                        message_check++;
//                    }

                }
            };
}

