package com.example.pocketbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        textView = findViewById(R.id.reason);
        textView1 = findViewById(R.id.amount);
        textView2 = findViewById(R.id.date);
        textView3 = findViewById(R.id.note);
        button = findViewById(R.id.submit_frorm_add);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        familyRefrence = FirebaseDatabase.getInstance().getReference().child("Family");


        final String uid = mAuth.getCurrentUser().getUid();
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("position");
        textView.setText(message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int amt = Integer.parseInt(textView1.getText().toString());
                final String purpose = textView.getText().toString();
                mRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String family = dataSnapshot.child("Family").getValue().toString();
                        mRef.child("Users").child(uid).child(message).child(date).child(purpose).setValue(amt);
                        familyRefrence.child(family).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long total_expense = (long) dataSnapshot.child("total_expense").getValue();
                                total_expense = total_expense + amt;
                                familyRefrence.child(family).child("total_expense").setValue(total_expense);
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
