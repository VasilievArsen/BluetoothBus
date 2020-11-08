package com.example.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondaryActivity extends AppCompatActivity {
    TextView mBalanceTV, mPlusKarta, CVCView, dataview, numbview;
    private String b;
    ImageButton mPayCard;
    SharedPreferences share;
    Object MM, YY, card_number;


    DatabaseReference rootCard = FirebaseDatabase.getInstance().getReference();
    DatabaseReference cardBal = rootCard.child("card");
    DatabaseReference cardNum = rootCard.child("card_info");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mBalanceTV = findViewById(R.id.id_balance);
        mPlusKarta = findViewById(R.id.textView5);
        mPlusKarta.setText("Добавить" + "\n" + "карту");
        mPayCard = findViewById(R.id.btn_pay1);

        numbview = findViewById(R.id.numbview);
        dataview = findViewById(R.id.DataView);

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
            public void onDataChange(DataSnapshot dataSnapshot2) {

                for (DataSnapshot ds : dataSnapshot2.getChildren()) {
                    Object fbbalance = ds.getValue();
                    mBalanceTV.setText("   " + fbbalance + " " + getString(R.string.Rub) + "   ");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        cardBal.addListenerForSingleValueEvent(eventListener3);


        ValueEventListener eventListener5 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String g = ds.getKey();
                    Object cardnum = ds.getValue();
                    if(g.equals("card_number")) {
                        numbview.setText("" + cardnum);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        cardNum.addListenerForSingleValueEvent(eventListener5);

        cardNum.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MM = snapshot.child("MM").getValue();
                    YY = snapshot.child("YY").getValue();

                    if(MM.equals("0")) {
                        //mCardInfo.setText("Добавьте карту");
                    }else{
                        dataview.setText(MM + "/" + YY);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
