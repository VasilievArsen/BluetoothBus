package com.example.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondaryActivity extends AppCompatActivity {
    TextView mBalanceTV, mPlusKarta;
    private String b;
    ImageButton mPayCard;

    DatabaseReference rootCard = FirebaseDatabase.getInstance().getReference();
    DatabaseReference cardBal = rootCard.child("card");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mBalanceTV = findViewById(R.id.id_balance);
        mPlusKarta = findViewById(R.id.textView5);
        mPlusKarta.setText("Добавить" + "\n" + "карту");
        mPayCard = findViewById(R.id.btn_pay1);

        mPayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CardAct = new Intent(getApplicationContext(), com.example.bb.CardAct.class);
                startActivity(CardAct);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        ValueEventListener eventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Object fbbalance = ds.getValue();
                    mBalanceTV.setText("   " + fbbalance + " " + getString(R.string.Rub) + "   ");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        cardBal.addListenerForSingleValueEvent(eventListener3);


    }






}
