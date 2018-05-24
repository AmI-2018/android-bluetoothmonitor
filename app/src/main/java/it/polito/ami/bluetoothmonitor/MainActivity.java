package it.polito.ami.bluetoothmonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Switch bluetoothSwitch;
    LinearLayout bluetoothDataLayout;
    TextView nameText;
    TextView addressText;

    private class BluetoothReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){

                //check the state of the bluetooth
                if(state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_TURNING_ON) {
                    //the Bluetooth has been turned on
                    bluetoothSwitch.setChecked(true);
                    bluetoothDataLayout.setVisibility(View.VISIBLE);
                    nameText.setText(BluetoothAdapter.getDefaultAdapter().getName());
                    addressText.setText(BluetoothAdapter.getDefaultAdapter().getAddress());
                }
                else if(state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF){
                    //the Bluetooth has been turned off
                    bluetoothSwitch.setChecked(false);
                    bluetoothDataLayout.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get references to the UI widgets
        bluetoothSwitch = findViewById(R.id.switch_bluetooth);
        bluetoothDataLayout = findViewById(R.id.layout_bluetooth_data);
        nameText = findViewById(R.id.text_name);
        addressText = findViewById(R.id.text_address);

        //set the bluetooth switch with the initial bluetooth status
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            bluetoothSwitch.setChecked(true);
            bluetoothDataLayout.setVisibility(View.VISIBLE);
            nameText.setText(BluetoothAdapter.getDefaultAdapter().getName());
            addressText.setText(BluetoothAdapter.getDefaultAdapter().getAddress());
        }
        else {
            bluetoothSwitch.setChecked(false);
            bluetoothDataLayout.setVisibility(View.INVISIBLE);
        }

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //I want to turn the bluetooth on
                    BluetoothAdapter.getDefaultAdapter().enable();
                }
                else{
                    //I want to turn the bluetooth off
                    BluetoothAdapter.getDefaultAdapter().disable();
                }
            }
        });

        //register a broadcast receiver to monitor changes in the bluetooth status
        BroadcastReceiver bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

    }


}
