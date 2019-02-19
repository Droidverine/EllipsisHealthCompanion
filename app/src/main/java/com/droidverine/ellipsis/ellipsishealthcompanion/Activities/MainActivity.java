package com.droidverine.ellipsis.ellipsishealthcompanion.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.droidverine.ellipsis.ellipsishealthcompanion.Fragments.HomeFragment;
import com.droidverine.ellipsis.ellipsishealthcompanion.Fragments.MessagesFragment;
import com.droidverine.ellipsis.ellipsishealthcompanion.Fragments.ProfileFragment;
import com.droidverine.ellipsis.ellipsishealthcompanion.R;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.Connmanager;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AHBottomNavigation bottomNavigation;
    MessagesFragment messagesFragment;
    ProfileFragment profileFragment;
    HomeFragment homeFragment;
    FragmentManager fragmentManager;
    private FirebaseDatabase database;
    FirebaseAuth mAuth;
    DrawerLayout drawer;
    ProgressDialog progressDialog;
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contextOfApplication = getApplicationContext();
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        messagesFragment = new MessagesFragment();
        Intent intent = getIntent();
        updateData();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main2cont, new HomeFragment()).commit();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#008577"));
// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home, R.color.bottomnavicon);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Messages", R.drawable.ic_chat, R.color.bottomnavicon);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.ic_user, R.color.bottomnavicon);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setBehaviorTranslationEnabled(false);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setAccentColor(Color.parseColor("#FFCC00"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));
//        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                Log.d("AHBottomNavigation", "position=" + position);
                switch (position) {
                    case 0:
                        fragment = homeFragment;
                        break;
                    case 1:
                        fragment = messagesFragment;
                        break;
                    case 2:
                        fragment = profileFragment;
                        break;

                }
                fragmentManager.beginTransaction().replace(R.id.main2cont, fragment).commit();
                return true;
            }
        });

        if (getIntent().getExtras() != null) {
            String badge = intent.getStringExtra("noti");

            bottomNavigation.setNotification("1", 1);
        }
    }
    ;


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            if (new Connmanager(MainActivity.this).checkNetworkConnection())
            {


                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Signing out");
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                mAuth.signOut();
                                Intent intent=new Intent(getApplicationContext(),SigninActivity.class);
                                startActivity(intent);


                                new DetailsManager(getApplicationContext()).setFoodCode("");
//            startActivity(new Intent(this, Login.class));
//            finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                drawer.closeDrawer(Gravity.START,false);

            }


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void updateData() {
        database = FirebaseDatabase.getInstance();

        DatabaseReference  myRef = database.getReference();
        myRef.child("patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                dataSnapshot.getRef().child("username").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }
}
