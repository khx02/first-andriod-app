package com.example.assignment_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment_2.provider.Event;
import com.example.assignment_2.provider.EventCategory;
import com.example.assignment_2.provider.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerlayout;
    Toolbar toolbar;
    FloatingActionButton fab;
    private TextView etEventName;
    private TextView etEventId;
    private TextView etEventCatId;
    private TextView etTickets;
    private Event lastSavedEvent;
    private EventCategory lastSavedEventCat;
    private TextView tvGesture;
    private View touchpad;
    private Switch isActive;
    private GestureDetectorCompat mDetector;
    private EventViewModel eventViewModel;
    private ArrayList<EventCategory> eventCategoryArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        etEventName  = findViewById(R.id.etEvName);
        etEventId = findViewById(R.id.etEvId);
        etEventCatId = findViewById(R.id.etEvCatId);
        etTickets = findViewById(R.id.etEvTicket);
        isActive = findViewById(R.id.isActive);
        fab = findViewById(R.id.floatingActionButton);
        drawerlayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        tvGesture = findViewById(R.id.tv_gesture);
        touchpad = findViewById(R.id.touchpad);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mDetector = new GestureDetectorCompat(this, customGestureDetector);
        mDetector.setOnDoubleTapListener(customGestureDetector);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);

                return true;
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nav_drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getAllEventCategoriesLiveData().observe(this, newData -> {
            // cast List<Item> to ArrayList<Item>
            eventCategoryArrayList = new ArrayList<>(newData);
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the state of the fragment if necessary
        // For example, you might restore the data to be displayed in the fragment
    }
    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d("NavigationListener", "Item clicked: " + item.getItemId());
            System.out.println("add category debug print");
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.options_viewAllCategories) {
                Intent intent = new Intent(getApplicationContext(), ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.options_addCategories) {
                Intent intent = new Intent(getApplicationContext(), NewEventCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.options_viewAllEvents) {
                Intent intent = new Intent(getApplicationContext(), ListEventActivity.class);
                startActivity(intent);
            } else if (id == R.id.options_logOut) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            drawerlayout.closeDrawers();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == R.id.option_clear_event_form) {
            etEventName.setText("");
            etEventId.setText("");
            etEventCatId.setText("");
            etTickets.setText("");
            isActive.setChecked(false);
        }  else if (item.getItemId() == R.id.option_delete_all_categories) {
//            SharedPreferences sharedPreferences = getSharedPreferences("EVENT_ACTIVITY", MODE_PRIVATE);
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//            try {
//                ArrayList<EventCategory> retrievedEventCatList = gson.fromJson(sharedPreferences.getString("EVENT_KEY", "No Event Saved"), type);
//                ArrayList<Event> tempEventList = new ArrayList<>();
//                int eventCounter = 0;
//                for (EventCategory eventCategory : retrievedEventCatList){
//                    // tempEventList.addAll(Converters.fromString(eventCategory.getEventArrayList()));
//                    eventCounter += eventCategory.getEventCount();
//                }
//                EventCategory eventCategory = new EventCategory(null, null, eventCounter, false, null);
//             //   eventCategory.setEventArrayList(Converters.fromArrayList(tempEventList));
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                ArrayList<EventCategory> oneCatList = new ArrayList<>();
//                oneCatList.add(eventCategory);
//                editor.putString("EVENT_KEY", gson.toJson(oneCatList));
//                editor.apply();
//            }
//            catch (JsonSyntaxException e){
//                Toast.makeText(this, "No category is created", Toast.LENGTH_SHORT).show();
//            }
            eventViewModel.deleteAllEventCategory();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentListCategory());
            fragmentTransaction.commit();
        } else if (item.getItemId() == R.id.option_delete_all_events) {
            // SharedPreference Implementation
//            SharedPreferences sharedPreferences = getSharedPreferences("EVENT_ACTIVITY", MODE_PRIVATE);
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//            try {
//                ArrayList<EventCategory> retrievedEventCatList = gson.fromJson(sharedPreferences.getString("EVENT_KEY", "No Event Saved"), type);
//                for (EventCategory eventCategory : retrievedEventCatList){
//                    eventCategory.revertEventCount();
//                    eventCategory.clearEventList();
//                }
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("EVENT_KEY", gson.toJson(retrievedEventCatList));
//                editor.apply();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentListCategory());
//                fragmentTransaction.commit();
//            }
//            catch (JsonSyntaxException e){
//                Toast.makeText(this, "No category is created", Toast.LENGTH_SHORT).show();
//            }
            eventViewModel.deleteAllEvent();
        }
        return true;
    }

    public void onSaveEventClick(View view){
        // convert to string
        String evName = etEventName.getText().toString();
        String evCatId = etEventCatId.getText().toString();
        String evTickets = etTickets.getText().toString();
        boolean switchStatus = isActive.isChecked();
        String REGEX = "^[a-zA-Z0-9\\s]*[a-zA-Z][a-zA-Z0-9\\s]*$";

        if (evName.isEmpty() || evCatId.isEmpty()){
            Toast.makeText(this, "Saving failed. Category or Event Id can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!evTickets.isEmpty() && Integer.parseInt(evTickets) < 0){
            Toast.makeText(this, "Saving failed. Invalid ticket amount.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!evName.matches(REGEX)){
            Toast.makeText(this, "Saving failed. Event name must be alphanumeric", Toast.LENGTH_SHORT).show();
            return;
        }
        if (evTickets.isEmpty()){
            evTickets = String.valueOf(0);
            Toast.makeText(this, "Event ticket is empty. Default to 0.", Toast.LENGTH_SHORT).show();

        }
//        SharedPreference Implementation
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = getSharedPreferences("EVENT_ACTIVITY", Context.MODE_PRIVATE);
        try {
//            SharedPreference Implementation
//            Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//            ArrayList<EventCategory> retrievedEventCatList = gson.fromJson(sharedPreferences.getString("EVENT_KEY", "No Event Saved"), type);
            for (EventCategory eventCategory : eventCategoryArrayList){
                if (eventCategory.getCategoryId() != null && eventCategory.getCategoryId().equalsIgnoreCase(evCatId)){
                    //add one to event count
                    eventCategory.setEventCount(eventCategory.getEventCount() + 1);
                    eventViewModel.update(eventCategory);
                    // generate random eventID
                    Random random = new Random();
                    char c1 = (char) (random.nextInt(26) + 'A');
                    char c2 = (char) (random.nextInt(26) + 'A');
                    String fiveDigits = String.valueOf(random.nextInt(90000) + 10000);
                    String eventID = "E" + c1 + c2 + '-' + fiveDigits;
                    etEventId.setText(eventID);

                    Event nEvent = new Event(eventID,evName ,evCatId ,Integer.parseInt(evTickets), switchStatus);
                    lastSavedEvent = nEvent;
                    lastSavedEventCat = eventCategory;
//                    SharePreference Implementation
//                    // save Event
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                    eventArrayList.add(nEvent);
//                    String jsonString = gson.toJson(retrievedEventCatList);
//
//                    System.out.println("dashboard" +jsonString);
//                    editor.putString("EVENT_KEY", jsonString);
//
//                    editor.apply();
                    eventViewModel.insert(nEvent);
                    String msg = "Event saved: " + eventID + " to " + evCatId;
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("UNDO", new MyUndoListener()).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentListCategory());
                    fragmentTransaction.commit();
                    return;
                }
            }
            Toast.makeText(this, "Category doesn't exist", Toast.LENGTH_SHORT).show();
        }
        catch (JsonSyntaxException e){
            Toast.makeText(this, "No event category has been created.", Toast.LENGTH_SHORT).show();
        }
    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            tvGesture.setText("onLongPress");
            etEventName.setText("");
            etEventId.setText("");
            etEventCatId.setText("");
            etTickets.setText("");
            isActive.setChecked(false);
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            tvGesture.setText("onDoubleTap");
            onSaveEventClick(getCurrentFocus());
            return super.onDoubleTap(e);
        }
    }

    public class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            SharedPreferences savedEventPreference = getSharedPreferences("EVENT_ACTIVITY", MODE_PRIVATE);
//            String eventPreferenceString = savedEventPreference.getString("EVENT_KEY", "No Event Saved");
//            Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//            Gson gson = new Gson();
//            ArrayList<EventCategory> retrievedEventCatList = gson.fromJson(eventPreferenceString, type);
//            for (EventCategory eventCat : retrievedEventCatList){
//                if (lastSavedCatId.equals(eventCat.getCategoryId())){
//                    // ArrayList<Event> eventsInTheCategoryList = Converters.fromString(eventCat.getEventArrayList());
//                    // eventsInTheCategoryList.remove(eventsInTheCategoryList.size() - 1);
//                    eventCat.minusEventByOne();
//                }
//            }
//            SharedPreferences.Editor editor = savedEventPreference.edit();
//            editor.putString("EVENT_KEY", gson.toJson(retrievedEventCatList));
//            editor.apply();
            eventViewModel.deleteEvent(lastSavedEvent.getEventId());
            lastSavedEventCat.setEventCount(lastSavedEventCat.getEventCount()-1);
            eventViewModel.update(lastSavedEventCat);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentListCategory());
            fragmentTransaction.commit();
        }
    }
}