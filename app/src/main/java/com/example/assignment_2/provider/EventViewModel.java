package com.example.assignment_2.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * ViewModel class is used for pre-processing the data,
 * before passing it to the controllers (Activity or Fragments). ViewModel class should not hold
 * direct reference to database. ViewModel class relies on repository class, hence the database is
 * accessed using the Repository class.
 */
public class EventViewModel extends AndroidViewModel {
    // reference to CardRepository
    private EventRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    public EventViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EventRepository(application);

        // get all items by calling method defined in repository class
        allEventsLiveData = repository.getAllEvents();
        allEventCategoriesLiveData = repository.getAllEventsCategories();
    }

    public LiveData<List<Event>> getAllEventsLiveData() {
        return allEventsLiveData;
    }
    public LiveData<List<EventCategory>> getAllEventCategoriesLiveData() {
        return allEventCategoriesLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param event object containing details of new Item to be inserted
     */
    public void insert(Event event) {
        repository.insert(event);
    }
    public void insert(EventCategory eventCategory) {
        repository.insert(eventCategory);
    }
    public void update(EventCategory eventCategory){
        repository.update(eventCategory);
    }
    public void deleteAllEvent(){
        repository.deleteAllEvent();
    }
    public void deleteAllEventCategory(){
        repository.deleteAllEventCategory();
    }
    public void deleteEvent(String eventId){
        repository.deleteEvent(eventId);
    }
}
