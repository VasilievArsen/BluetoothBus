package com.example.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class ActivityThird extends AppCompatActivity {
    EditText editText;
    Button mBtnSearch;
    TextView routeView;
    private String TAGs = "myLogs";
private int n = 0;


    DatabaseReference roouteRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference routeRef = roouteRef.child("route");

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);
        editText = findViewById(R.id.id_editTextRoute);
        mBtnSearch = findViewById(R.id.id_buttonSearch);
        routeView = findViewById(R.id.textView3);




        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                try {
                    n = Integer.parseInt(number);
                }catch (NumberFormatException ignored) {}
                Log.d(TAGs, " " + number);
                ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String fbnumber = ds.getKey();
                                   Integer fbnumbers = Integer.valueOf(fbnumber);
                                    Object fbroute = ds.getValue();
                                   // String n2 = String.valueOf(n);
                                    //Log.d(TAGs, n2);
                                    String fbroutes = String.valueOf(fbroute).replace("\"_n\"", "\n");
                                    if (n == fbnumbers) {
                                        routeView.setText("  Номер маршрута: " + fbnumber + "\n" + "  Маршрут: " + "    " + fbroutes + "\n");
                                        Log.d(TAGs, fbnumber);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        routeRef.addListenerForSingleValueEvent(eventListener);
            }
        });
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
    }







}