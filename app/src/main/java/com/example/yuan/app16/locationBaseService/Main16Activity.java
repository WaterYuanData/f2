package com.example.yuan.app16.locationBaseService;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.yuan.app16.R;

import java.util.ArrayList;
import java.util.List;

public class Main16Activity extends AppCompatActivity implements BaiduMap.OnMapStatusChangeListener {
    private static final String TAG = "Main16Activity";
    private LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;

    // 避免多次调用animateMapStatus() 方法
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        // 初始化操作，在 setContentView() 方法前调用
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main16);

        positionText = (TextView) findViewById(R.id.location);
        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        // 声明权限，将权限添加到list集合中再一次性申请
        List<String> permissionList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(Main16Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(Main16Activity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(Main16Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Main16Activity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 开始地理位置定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        // 创建LocationClientOption 对象
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);  //2秒钟更新下当前位置
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //需要获取当前位置的详细信息
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    /**
     * 把地图移动到当前位置
     *
     * @param location
     */
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            Log.d(TAG, "navigateTo: 1");
//            baiduMap.animateMapStatus(update);
            baiduMap.setMapStatus(update);
            Log.d(TAG, "navigateTo: 2");
            update = MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(update);
            baiduMap.setMapStatus(update);
            Log.d(TAG, "navigateTo: 3");
            isFirstLocate = false;
        }

        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
//        Log.d(TAG, "navigateTo: 4");
        baiduMap.setMyLocationData(locationData);
//        Log.d(TAG, "navigateTo: 5");
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        Log.d(TAG, "onMapStatusChangeStart: ");
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
        Log.d(TAG, "onMapStatusChangeStart: ");
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        Log.d(TAG, "onMapStatusChange: ");
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        Log.d(TAG, "onMapStatusChangeFinish: ");
    }


    // 监听器
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                    bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }

            StringBuilder currentLocation = new StringBuilder();
            currentLocation.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentLocation.append("经线：").append(bdLocation.getAltitude()).append("\n");
            currentLocation.append("国家：").append(bdLocation.getCountry()).append("\n");
            currentLocation.append("省：").append(bdLocation.getProvince()).append("\n");
            currentLocation.append("市：").append(bdLocation.getCity()).append("\n");
            currentLocation.append("区：").append(bdLocation.getDistrict()).append("\n");
            currentLocation.append("街道：").append(bdLocation.getStreet()).append("\n");
            currentLocation.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentLocation.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentLocation.append("网络");
            }
            positionText.setText(currentLocation);

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//停止定位
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
