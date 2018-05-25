package com.doodleHub.io;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailVerificationActivity extends AppCompatActivity {
    private TextView status;
    private ImageView img;
    private Button btnCheck;
    private TextView email;
    private Button nextBtn;
    //Firebase
    DatabaseReference mUserDatabase;
    FirebaseAuth mAuth;
    String currentUser;
    FirebaseUser user;
    String returnedName;
    String returnedEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);



        status = (TextView) findViewById(R.id.verifyText);

        img = (ImageView) findViewById(R.id.verify);
        btnCheck=(Button)findViewById(R.id.btnCheck);
        user=FirebaseAuth.getInstance().getCurrentUser();

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);
        //click listener

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e("USER ID",mAuth.getCurrentUser().getUid());
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(user.isEmailVerified()){

                            mUserDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_SHORT).show();
                                    img.setImageResource(R.drawable.verified);
                                    btnCheck.setVisibility(View.INVISIBLE);
                                    nextBtn.setVisibility(View.VISIBLE);

                                    status.setText("Verified User");
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }else{Toast.makeText(getApplicationContext(),"Not Verified",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });


    }
}
