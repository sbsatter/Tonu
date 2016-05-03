package me.argha.tonu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.argha.tonu.utils.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bitmap bmp;
    @Bind(R.id.mainHelpBtn)
    ImageView mainHelpBtn;
    String filename;
    String username;


    boolean clicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        username = getIntent().getStringExtra("userName");

    //    imageViewpropic.setImageBitmap(bitmap);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        ImageView nav_propic=(ImageView)hView.findViewById(R.id.profilePic);
        nav_user.setText(username);
        nav_propic.setImageBitmap(bitmap);
    //    Picasso.with(this).load("http://graph.facebook.com/" + "10153459358326496" + "/picture?type=small").into(nav_propic);
    //    nav_propic.setVisibility(View.VISIBLE);


    }

    @OnClick({R.id.mainReportBtn,R.id.mainDangerZoneBtn,R.id.mainHelpBtn,R.id.mainForumBtn,R.id.mainExpertHelpBtn})
    public void mainBtnClicks(View view){
        switch (view.getId()){
            case R.id.mainReportBtn:
                startActivity(new Intent(this,ReportActivity.class));
                break;
            case R.id.mainDangerZoneBtn:
                Toast.makeText(MainActivity.this, "Showing all incidents previously occured", Toast
                        .LENGTH_SHORT).show();
                startActivity(new Intent(this,DangerZone.class));
                break;
            case R.id.mainHelpBtn:
                if(!clicked){
                    String messageToSend = "Salma is asking for emergency help.\nAddress: PSC Convention center\nLocation: 23.803435, 90.378862\n" +
                            "http://maps.google.com/?q=23.803435,90.378862";
                    String number = "01621209959";

                    SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);

                    mainHelpBtn.setImageResource(R.drawable.ic_alaram);
                    clicked=true;
                    Util.showToast(this,"Alert has been sent to emergeny contacts and nearest police stations");
                }else {
                    clicked=false;
                    mainHelpBtn.setImageResource(R.drawable.ic_alarm2);
                }
                break;
            case R.id.mainExpertHelpBtn:
                showHelpOptionDialog();
                break;
            case R.id.mainForumBtn:
                startActivity(new Intent(this,ForumActivity.class));
                break;

        }
    }

    private void showHelpOptionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"CHAT","TALK"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                startActivity(new Intent(MainActivity.this,ExpertChatActivity.class));
                                break;
                            case 1:
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:1234"));
                                startActivity(callIntent);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setTitle("Please select - ");

        Dialog dialog=builder.create();
        dialog.show();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

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
        switch (item.getItemId()){
            case R.id.menu_item_help:
                startActivity(new Intent(this,IdentifyActivity.class));
                break;
            case R.id.menu_item_notification:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent=null;
        int id = item.getItemId();
        if (id == R.id.settings) {

        } else if (id == R.id.emergency_contacts) {
            intent= new Intent(MainActivity.this,EmergencyContactsActivity.class);
        } else if (id == R.id.profile) {
            intent= new Intent(MainActivity.this,ProfileActivity.class);
        } else if (id == R.id.faq) {
            intent= new Intent(MainActivity.this,FAQActivity.class);
        }
        else if (id == R.id.laws) {

        } else if (id == R.id.terms_and_conditions) {

        }
        if(intent!=null) startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}