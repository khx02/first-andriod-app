package com.example.assignment_2.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name="columnEventId")
    private final String eventId;
    @ColumnInfo(name="columnEventName")
    private final String eventName;
    @ColumnInfo(name="columnTicketCount")
    private final int ticketCount;
    @ColumnInfo(name="columnEventCategoryId")
    private final String eventCategoryId;
    @ColumnInfo(name="columnIsActive")
    private final boolean isActive;

    public Event(String eventId, String eventName, String eventCategoryId,int ticketCount, boolean isActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.ticketCount = ticketCount;
        this.eventCategoryId = eventCategoryId;
        this.isActive = isActive;
    }
    public int getId() {
        return id;
    }
    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public String getEventCategoryId() {
        return eventCategoryId;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setId(int id) {
        this.id = id;
    }

}
