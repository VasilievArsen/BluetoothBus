package com.example.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CardAct extends AppCompatActivity {
    TextView cardnum, carddata, cardcvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        cardnum = findViewById(R.id.CardNum);
        carddata = findViewById(R.id.Carddata);
        cardcvc = findViewById(R.id.CardCvc);

    }
}