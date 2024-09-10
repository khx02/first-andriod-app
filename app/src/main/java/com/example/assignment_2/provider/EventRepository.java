package com.example.assignment_2.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {
    // private class variable to hold reference to DAO
    private EventDAO eventDAO;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    // constructor to initialise the repository class
    EventRepository(Application application) {
        // get reference/instance of the database
        EventDatabase db = EventDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        eventDAO = db.eventDAO();

        // once the class is initialised get all the items in the form of LiveData
        allEventsLiveData = eventDAO.getAllEvents();
        allEventCategoriesLiveData = eventDAO.getAllEventCategories();
    }

    /**
     * Repository method to get all events
     * @return LiveData of type List<Event>
     */
    LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }
    LiveData<List<EventCategory>> getAllEventsCategories() {
        return allEventCategoriesLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Event to be inserted
     */
    void insert(Event event) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.addEvent(event));
    }
    void insert(EventCategory eventCategory) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.addEventCategory(eventCategory));
    }
    void update(EventCategory eventCategory) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.update(eventCategory.getEventCount(), eventCategory.getCategoryId()));
    }
    void deleteAllEvent(){
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAllEvent());
    }
    void deleteAllEventCategory(){
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAllEventCategory());
    }
    void deleteEvent(String eventId){
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteEvent(eventId));
    }
}
