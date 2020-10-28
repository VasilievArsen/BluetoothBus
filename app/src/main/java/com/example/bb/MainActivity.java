package com.example.bb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.String;
import java.lang.*;


public class MainActivity extends AppCompatActivity{

    private ImageButton bScan;
    private boolean deviceFound;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;
    private String TAG = "myLogs";
    private int a = 0;
    private static final int REQUEST_ENABLE_BT = 0;
    private StorageReference mStorageRef;



    TextView mStatus, mPairedTV, mTextBon, mTextBoff, mTextCard, mTextRoute;
    Button mButtonPay;
    ImageButton mBtnOn, mBtnOff, mBtnCard, mBtnRoute, mBtnClear, mPairedBtn;

    BluetoothAdapter mBlueAdapter;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference macsRef = rootRef.child("macs");
    DatabaseReference rootCard = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPairedTV =  findViewById(R.id.id_Paired);
        mPairedTV.setText(getResources().getString(R.string.SearchBus));
        bScan =      findViewById(R.id.id_BtnPaired);
        mStatus =    findViewById(R.id.id_Status);
        mPairedTV =  findViewById(R.id.id_Paired);
        mPairedBtn = findViewById(R.id.id_BtnPaired);
        mBtnOn =     findViewById(R.id.id_BtnOn);
        mBtnOff =    findViewById(R.id.id_BtnOff);
        mButtonPay = findViewById(R.id.id_buttonpay);
        mBtnCard =     findViewById(R.id.id_BtnCard);
        mBtnRoute = findViewById(R.id.id_BtnRoute);
        mTextBoff = findViewById(R.id.textView6);
        mTextBoff.setText("Выкл." + "\n" + "Bluetooth");
        mTextBon = findViewById(R.id.textView4);
        mTextBon.setText("Вкл." + "\n" + "Bluetooth");
        mTextCard = findViewById(R.id.textView7);
        mTextCard.setText("Оплата");
        mTextRoute = findViewById(R.id.textView8);
        mTextRoute.setText("Маршруты");
        mBtnClear = findViewById(R.id.id_buttonclear);


        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_FINE_LOCATION);
        }

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if (mBlueAdapter == null) {
            mStatus.setText("Bluetooth недоступен");
        } else {
            mStatus.setText("Bluetooth доступен");
        }

        mBtnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("Включаем Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    showToast("Bluetooth уже включен");
                }
            }
        });
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mPairedTV.getText() != getString(R.string.SearchBus))){
                    mPairedTV.setText(getResources().getString(R.string.SearchBus));
                    if(mBlueAdapter.isEnabled()) bluetoothLeScanner.stopScan(scanCallback);
                }else{
                    showToast("Нечего удалять");
                }


            }
        });
                mBtnOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBlueAdapter.isEnabled()) {
                            mBlueAdapter.disable();
                            showToast("Выключаем Bluetooth");
                            showToast("Bluetooth выключен");
                        } else {
                            showToast("Bluetooth уже выключен");
                        }
                    }
                });
        mBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SecAct = new Intent(getApplicationContext(),SecondaryActivity.class);
                startActivity(SecAct);
                }


        });

        mBtnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ThiAct = new Intent(getApplicationContext(),ActivityThird.class);
                startActivity(ThiAct);
            }
        });
        
        mButtonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPairedTV.getText() == getString(R.string.SearchBus)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Ошибка");
                    builder.setMessage("Маршрут не найден.");
                    builder.setPositiveButton("OK", null);

                    rootCard.child("card").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            long p = 0;
                            String value = (String) currentData.getValue();
                            p = Long.parseLong(value);
                            if (value == null) {
                                Toast.makeText(MainActivity.this,"no", Toast.LENGTH_LONG).show();
                                return Transaction.success(currentData);
                            }else{
                                Toast.makeText(MainActivity.this,value,Toast.LENGTH_LONG).show();
                            }

                            //присваиваем новое значение
                            p = Long.parseLong(value) - 28;

                            // Set value and report transaction success
                            currentData.setValue(p);
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                            if (error == null) {
                                //всё ОК
                            } else {
                                //произошла ошибка. Она тут: databaseError.toException()
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Оплата");
                    builder.setMessage("Оплата произошла успешно.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    bluetoothLeScanner.stopScan(scanCallback);
                    Intent CheckAct = new Intent(getApplicationContext(), com.example.bb.CheckAct.class);
                    startActivity(CheckAct);
                }
            }
        });



        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                        bluetoothLeScanner = mBlueAdapter.getBluetoothLeScanner();
                        bluetoothLeScanner.startScan(scanCallback);
                        deviceFound = false;
                } else {
                    showToast("Включите Bluetooth");
                }
            }
        });

    }

    ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice bluetoothDevice = result.getDevice();
            final String s = bluetoothDevice.getAddress();
            final int rssi = result.getRssi();
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String fbname = ds.getKey();
                        Object fbmac = ds.getValue();
                        String fbmacstring = String.valueOf(fbmac);
                        if(s.equals(fbmacstring) && rssi > -200){
                            mPairedTV.setText("Имя маршрута: " + fbname + "\n" + "МAC маршрута: " + fbmac + "\n");
                            Log.d(TAG, fbname + " RSSI: " + rssi);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            macsRef.addListenerForSingleValueEvent(eventListener);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    showToast("Bluetooth включен");
                } else {
                    showToast("Не получилось включить Bluetooth");
                }break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
    }

@Override
    protected void onDestroy(){
    super.onDestroy();
    mBlueAdapter.disable();
}
    protected void onStart(){
        super.onStart();
        if (mBlueAdapter.isEnabled()) {
            bluetoothLeScanner = mBlueAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.startScan(scanCallback);
            deviceFound = false;
        } else {
            showToast("Включите Bluetooth");
        }
    }

}
