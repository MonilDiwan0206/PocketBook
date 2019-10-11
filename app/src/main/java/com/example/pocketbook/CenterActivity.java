package com.example.pocketbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CenterActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private Button button;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private DatabaseReference familyRefrence;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        textView = findViewById(R.id.reason);
        textView1 = findViewById(R.id.amount);
        button = findViewById(R.id.submit_frorm_add);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        familyRefrence = FirebaseDatabase.getInstance().getReference().child("Family");


        final String uid = mAuth.getCurrentUser().getUid();
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("position");
        mProgress = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Updating your details");
                mProgress.show();
                final int amt = Integer.parseInt(textView1.getText().toString());
                final String purpose = textView.getText().toString();
                mRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String family = dataSnapshot.child("Family").getValue().toString();
                        long te = (long) dataSnapshot.child("total_expense").getValue();
                        te = te + amt;
                        mRef.child("Users").child(uid).child("total_expense").setValue(te);
                        mRef.child("Users").child(uid).child(message).child(date).child(purpose).setValue(amt);
                        familyRefrence.child(family).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long total_expense = (long) dataSnapshot.child("total_expense").getValue();
                                total_expense = total_expense + amt;
                                familyRefrence.child(family).child("total_expense").setValue(total_expense);
                                mProgress.dismiss();
                                Intent mainIntent = new Intent(CenterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
