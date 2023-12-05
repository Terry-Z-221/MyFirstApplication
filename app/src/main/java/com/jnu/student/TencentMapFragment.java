package com.jnu.student;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class TencentMapFragment extends Fragment {

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    public TencentMapFragment() {
        // Required empty public constructor
    }

    public static TencentMapFragment newInstance() {
        TencentMapFragment fragment = new TencentMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tencent_map, container, false);

        com.tencent.tencentmap.mapsdk.maps.MapView mapView = rootView.findViewById(R.id.mapView);
        TencentMap tencentMap = mapView.getMap();

        // 把暨南大学珠海校区设置为地图中心点，并把地图移动到这个中心点坐标处，并设置显示层级
        LatLng point = new LatLng(22.249942,113.534341);
        tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

        // 添加一个图标型Marker
        MarkerOptions markerOptions = new MarkerOptions(new LatLng(22.249942,113.534341))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.jnu));
        tencentMap.addMarker(markerOptions);

        // 添加一个文本型标记
        MarkerOptions textMarkerOptions = new MarkerOptions(point)
                .title("JNU")
                .snippet("This is a text marker.");
        Marker textMarker = tencentMap.addMarker(textMarkerOptions);

        // 设置文字标记在图片标记上方显示
        textMarker.setZIndex(1);
        // 显示标记的信息窗口
        textMarker.showInfoWindow();
        // 设置Marker支持点击
        textMarker.setClickable(true);
        // 给标记添加响应事件
        tencentMap.setOnMarkerClickListener(marker -> {
            if (marker.equals(textMarker)) {
                // 在此处处理文本型标记的点击事件
                Toast.makeText(requireActivity(), "暨南大学珠海校区", Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}