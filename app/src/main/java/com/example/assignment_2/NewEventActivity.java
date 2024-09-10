package com.example.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class NewEventActivity extends AppCompatActivity {
    private EventActivitySMSReceiver eventActivitySMSReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);
        eventActivitySMSReceiver = new EventActivitySMSReceiver();
        registerReceiver(eventActivitySMSReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
    }

    public void onSaveEventClick(View view){
        TextView etEventName = findViewById(R.id.etEvName);
        TextView etEventId = findViewById(R.id.etEvId);
        TextView etEventCatId = findViewById(R.id.etEvCatId);
        TextView etTickets = findViewById(R.id.etEvTicket);
        Switch isActive = findViewById(R.id.isActive);
        SharedPreferences sharedPreferences = getSharedPreferences("EVENT_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // convert to string
        String evName = etEventName.getText().toString();
        String evCatId = etEventCatId.getText().toString();
        String evTickets = etTickets.getText().toString();
        boolean switchStatus = isActive.isChecked();

        if (evName.isEmpty() || evCatId.isEmpty()){
            Toast.makeText(this, "Category or Event Id can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (evTickets.isEmpty() || Integer.parseInt(evTickets) < 0){
            Toast.makeText(this, "Invalid ticket amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // generate random eventID
        Random random = new Random();
        char c1 = (char) (random.nextInt(26) + 'A');
        char c2 = (char) (random.nextInt(26) + 'A');
        String fiveDigits = String.valueOf(random.nextInt(90000) + 10000);
        String eventID = "E" + c1 + c2 + '-' + fiveDigits;

        etEventId.setText(eventID);

        // save
        editor.putString("KEY_EVENT_ID", eventID);
        editor.putInt("KEY_TICKETS_AVAILABLE", Integer.parseInt(evTickets));
        editor.putString("KEY_EVENT_NAME", evName);
        editor.putString("KEY_EVENT_ID", eventID);
        editor.putBoolean("KEY_EVENT_SWITCH", switchStatus);

        editor.apply();
        String msg = "Event saved: " + eventID + " to " + evCatId;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private boolean smsValidator(String s1){
        s1 = s1.trim();
        String[] parts = s1.split(":");

        if(parts[0].equals("event")){
            String[] semicolonPartString = parts[1].split(";", 4);


            if(semicolonPartString.length == 4){
                try {
                    String eventNameString = semicolonPartString[0];
                    String catID = semicolonPartString[1];
                    String avaiTickets = semicolonPartString[2];
                    String boolString = semicolonPartString[3];

                    if (!eventNameString.isEmpty()){
                        if (catID.isEmpty()){
                            return false;
                        }
                        else {
                            String[] catIdSplit = catID.split("-");
                            String firstThreeChar = catIdSplit[0];
                            String lastFourChar = catIdSplit[1];
                            // check if the three char is alphabets
                            if (!firstThreeChar.matches("[a-zA-Z]+")){
                                return false;
                            }
                            try {
                                int lastFourCharInt = Integer.parseInt(lastFourChar);
                                if (lastFourCharInt < 0){
                                    return false;
                                }
                            }
                            catch (NumberFormatException e){
                                return false;
                            }
                        }
                    }
                    if (!boolString.isEmpty()){
                        if (!boolString.equalsIgnoreCase("TRUE") && !boolString.equalsIgnoreCase("FALSE"))
                        {
                            return false;
                        }
                    }
                    if (!avaiTickets.isEmpty()){
                        try {
                            int ticket = Integer.parseInt(avaiTickets);
                            if (ticket < 0) {
                                return false;
                            }
                        }
                        catch (NumberFormatException e){
                            return false;
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    return false;
                }
            }
            else {
                return false; // list len not 4
            }
        }
        else {
            return false; // failed category
        }
        return true;
    }

    class EventActivitySMSReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView etEventName = findViewById(R.id.etEvName);
            TextView etEventCatId = findViewById(R.id.etEvCatId);
            TextView etTickets = findViewById(R.id.etEvTicket);
            Switch isActive = findViewById(R.id.isActive);
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            System.out.println(msg);
            System.out.println(smsValidator(msg));
            if (smsValidator(msg)) {
                // import from incoming messages
                if (!msg.isEmpty()) {
                    String[] colonSplit = msg.split(":");
                    String[] semicolonSplit = colonSplit[1].split(";", 4);
                    for(String s32 : semicolonSplit){
                    System.out.println(s32);}
                    String eventName = semicolonSplit[0];
                    String eventCatID = semicolonSplit[1];
                    String eventTickets = semicolonSplit[2];
                    String eventStatus = semicolonSplit[3];
                    if (!eventName.isEmpty()){
                        etEventName.setText(eventName);
                    }
                    if (!eventTickets.isEmpty()) {
                        etTickets.setText(eventTickets);
                    }
                    if (!eventCatID.isEmpty()){
                        etEventCatId.setText(eventCatID);
                    }
                    if (!eventStatus.isEmpty()){
                        isActive.setChecked(eventStatus.equalsIgnoreCase("TRUE"));
                    }
                }
            }
            else {
                Toast.makeText(context, "Invalid message. Event Activity cannot be autofilled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(eventActivitySMSReceiver);
    }
}