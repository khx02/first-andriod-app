package com.example.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment_2.provider.EventCategory;
import com.example.assignment_2.provider.EventViewModel;

import java.util.Random;

public class NewEventCategoryActivity extends AppCompatActivity {
    private EventCategoryBroadcastReceiver eventCategoryBroadcastReceiver;
    private EventViewModel eventViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_event_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);
        eventCategoryBroadcastReceiver = new EventCategoryBroadcastReceiver();
        registerReceiver(eventCategoryBroadcastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    public void onSaveCategoryClick(View view){
        TextView tvCatID = findViewById(R.id.etEvId);
        TextView tvCatName = findViewById(R.id.etEvName);
        TextView tvEventCount = findViewById(R.id.etEvCatId);
        Switch isActive = findViewById(R.id.isActive);
        TextView tvLocation = findViewById(R.id.etCatLoc);
        String location = tvLocation.getText().toString();
        boolean isChecked = isActive.isChecked();

//        SharedPreferences sharedPreferences = getSharedPreferences("EVENT_ACTIVITY", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();

        String eventCount = tvEventCount.getText().toString();
        String catName = tvCatName.getText().toString();

        // check for name is not empty
        if (catName.isEmpty()){
            Toast.makeText(this, "Saving Failed. Event Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!eventCount.isEmpty() && Integer.parseInt(eventCount) < 0) {
            Toast.makeText(this, "Saving Failed. Event Count must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (eventCount.isEmpty()) {
            eventCount = "0";
            Toast.makeText(this, "Event Count is empty, default to 0", Toast.LENGTH_SHORT).show();
        }
        String REGEX = "^[a-zA-Z0-9\\s]*[a-zA-Z][a-zA-Z0-9\\s]*$";
        if (!catName.matches(REGEX)){
            Toast.makeText(this, "Saving Failed. Category Name must be Alphanumeric", Toast.LENGTH_SHORT).show();
            return;
        }


        // generate random catID
        Random random = new Random();
        char c1 = (char) (random.nextInt(26) + 'A');
        char c2 = (char) (random.nextInt(26) + 'A');
        String fourDigits = String.valueOf(random.nextInt(9000) + 1000);
        String catID = "C" + c1 + c2 + '-' + fourDigits;

        tvCatID.setText(catID);

        // Event Count
        if (eventCount.isEmpty()){
            tvEventCount.setText(0);
        }
        // Location
        if (location.isEmpty()){
            tvLocation.setText("");
        }
        EventCategory nEventCategory = new EventCategory(catID, catName, Integer.parseInt(eventCount), isChecked, location);
//        SharedPreference Implementation
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        ArrayList<EventCategory> retrievedEventCatList;
//        try {
//            retrievedEventCatList = gson.fromJson(sharedPreferences.getString("EVENT_KEY", "No Event Saved"), type);
//            System.out.println("successful retrieved: " + retrievedEventCatList);
//        }
//        catch (JsonSyntaxException e){
//            retrievedEventCatList = new ArrayList<>();
//            System.out.println("event cat saved array list cant be retrieved"+ e);
//        }
//        retrievedEventCatList.add(nEventCategory);
//        String jsonString = gson.toJson(retrievedEventCatList);
//        editor.putString("EVENT_KEY", jsonString);
//        editor.apply();

        eventViewModel.insert(nEventCategory);

        String output = "Category successfully saved " + catID;
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();

        finish();
    }
    private boolean stringValidator(String s1){
        s1 = s1.trim();
        String[] strSplit = s1.split(":");
        if(strSplit[0].equals("category")){
            String[] semicolonPartString = strSplit[1].split(";", 3);

            if(semicolonPartString.length == 3){
                try {
                    String eventNameString = semicolonPartString[0];
                    String countString = semicolonPartString[1];
                    String boolString = semicolonPartString[2];

                    if (!eventNameString.isEmpty()){
                        if (!countString.isEmpty()){
                            try {
                                int count = Integer.parseInt(countString);
                                if(count <= 0){
                                    return false; // not positive
                                }
                            }
                            catch (NumberFormatException e) {
                                return false; // event count not int
                            }
                        }
                        if (!boolString.isEmpty()){
                            if ((!boolString.equalsIgnoreCase("TRUE") && !boolString.equalsIgnoreCase("FALSE"))) {
                                return false; // the word isn't 'true' or 'false'
                            }
                        }
                    }
                    else {
                        return false; // no Event Name
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    return false; // less than 2 semicolon
                }
            }
            else {
                return false; // list len not 3
            }
        }
        else {
            return false; // prefix don't have 'category:'
        }
        return true;
    }
    class EventCategoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView tvCatName = findViewById(R.id.etEvName);
            TextView tvEventCount = findViewById(R.id.etEvCatId);
            Switch isActive = findViewById(R.id.isActive);
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            if (stringValidator(msg)) {
                // import from incoming messages
                if (!msg.isEmpty()) {
                    String[] colonSplit = msg.split(":");
                    String[] semicolonSplit = colonSplit[1].split(";", 3);
                    String eventName = semicolonSplit[0];
                    String eventCount = semicolonSplit[1];
                    String switchStatus = semicolonSplit[2];
                    if (!eventName.isEmpty()) {
                        tvCatName.setText(eventName);
                    }
                    if (!eventCount.isEmpty()){
                        tvEventCount.setText(eventCount);
                    }
                    if (!switchStatus.isEmpty()){
                        isActive.setChecked(switchStatus.equalsIgnoreCase("TRUE"));
                    }
                }
            }
            else {
                Toast.makeText(context, "Invalid Message. Event Category cannot be autofilled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(eventCategoryBroadcastReceiver);
    }
}