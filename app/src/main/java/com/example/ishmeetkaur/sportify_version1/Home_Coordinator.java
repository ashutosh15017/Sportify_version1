package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home_Coordinator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__coordinator);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        mAuth = FirebaseAuth.getInstance();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

//        --------------------------------------------------------



//        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager2);
//
//        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter_coordinate adapter = new SimpleFragmentPagerAdapter_coordinate(getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
//
//        //tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs2);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


       if(id == R.id.action_logout2)
        {
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera2)
        {
            //home
            Intent i = new Intent(getApplicationContext(),Home_Coordinator.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery2)
        {
            //my team detail
            Intent i = new Intent(getApplicationContext(),TeamDetailsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage2)
        {
            //attendance
            Intent i = new Intent(getApplicationContext(),Attendance.class);
            startActivity(i);

        }
        else if (id == R.id.sss)
        {
            //suggestions
            Intent i = new Intent(getApplicationContext(),Suggestion_Coordinator.class);
            startActivity(i);
        }

        else if (id == R.id.nav_logout2)
        {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut()
    {
        mAuth.signOut();
        Intent i = new Intent(getApplicationContext(),Login.class);
        //finishActivity(1);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
    }
}
