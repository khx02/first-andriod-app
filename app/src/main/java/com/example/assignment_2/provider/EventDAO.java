package com.example.assignment_2.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDAO {
    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();
    @Query("select * from `event-category`")
    LiveData<List<EventCategory>> getAllEventCategories();
    @Insert
    void addEvent(Event event);
    @Insert
    void addEventCategory(EventCategory eventCategory);
    @Query("UPDATE `event-category` SET columnCategoryEventCount=:eventCount WHERE columnCategoryId = :id")
    void update(int eventCount, String id);
    // delete All
    @Query("DELETE FROM `event-category`")
    void deleteAllEventCategory();
    @Query("DELETE FROM events")
    void deleteAllEvent();
    @Query("delete from events where columnEventId= :eventId")
    void deleteEvent(String eventId);

}
