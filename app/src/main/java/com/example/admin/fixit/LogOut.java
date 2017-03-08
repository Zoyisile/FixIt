package com.example.admin.fixit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Declaration
    private Button LogOutBtn;
    private FirebaseAuth mFirebase;
    private Button Elect, next;
    private Button Plumbing;
    private Button Infrastructure;
    private BottomNavigationView mBottomNav;
    private FirebaseAuth.AuthStateListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(LogOut.this,"Welcome To FixIt" ,Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_log_out);

        //Populate
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        Elect = (Button)findViewById(R.id.btnelect);
        Plumbing = (Button) findViewById(R.id.btnplumb);
        Infrastructure = (Button) findViewById(R.id.btninfrsst);
        next = (Button) findViewById(R.id.btn_next);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return true;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), MessageChatting.class));

            }
        });

        Elect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogOut.this , MessageChatting.class);
                startActivity(i);
            }
        });

        Plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(LogOut.this, MessageChatting.class);
                startActivity(p);
            }
        });

        Infrastructure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            Intent f = new Intent(LogOut.this,MessageChatting.class);
                startActivity(f);
            }
        });


        //Populate
        mFirebase = FirebaseAuth.getInstance();
        //OnClickListener
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(LogOut.this, LogIn.class));

                }
            }
        };

//        //An intent for opening the colors activity
//        Intent colorsIntent = new Intent(LogOut.this, Electricity.class);
//        startActivity(colorsIntent);//start the new activity
    //Button Listener
        LogOutBtn = (Button)findViewById(R.id.LogOut_btn);

        LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebase.signOut();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebase.addAuthStateListener(mListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
