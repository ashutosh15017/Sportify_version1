package com.example.ishmeetkaur.sportify_version1;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private final int WRITE_EXTERNAL_STORAGE_CODE=1;
    private final int READ_EXTERNAL_STORAGE_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        askPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_CODE);
        askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE_CODE);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        //tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void askPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),permission)!= PackageManager.PERMISSION_GRANTED)
        {
            // we dont have permission
            ActivityCompat.requestPermissions(Login.this, new String[]{permission},requestCode);
        }
        else
        {
            //we have permission

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //granted
                }
                break;
            case READ_EXTERNAL_STORAGE_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //granted
                }
                break;

        }
    }
}
