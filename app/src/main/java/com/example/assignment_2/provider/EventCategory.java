package com.example.assignment_2.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "event-category")
public class EventCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
//    @ColumnInfo(name = "columnEventArrayList")
//    private String eventArrayList;
    @ColumnInfo(name="columnCategoryId")
    private String categoryId;
    @ColumnInfo(name="columnCategoryName")
    private String categoryName;
    @ColumnInfo(name="columnCategoryEventCount")
    private int eventCount;
    @ColumnInfo(name="columnCategoryLocation")
    private String location;
    private int oriEventCount;
    @ColumnInfo(name="columnIsActive")
    private boolean isActive;

    public EventCategory(String categoryId, String categoryName, int eventCount, boolean isActive, String location) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.eventCount = eventCount;
        this.oriEventCount = eventCount;
        this.isActive = isActive;
//        this.eventArrayList = "";
        this.location = location;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }
    public void setOriEventCount(int oriEventCount){
        this.oriEventCount = oriEventCount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOriEventCount() {
        return oriEventCount;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

//    public void setEventArrayList(String eventArrayList) {
//        this.eventArrayList = eventArrayList;
//    }
    public int getId() {
        return id;
    }
    // getter
//    public String getEventArrayList() {
//        return eventArrayList;
//    }
    public String getCategoryName() {
        return categoryName;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public int getEventCount() {
        return eventCount;
    }
    public void revertEventCount(){
        eventCount = oriEventCount;
    }
    public boolean isActive() {
        return isActive;
    }
    public String getLocation() {
        return location;
    }
    public void minusEventByOne(){eventCount -= 1;}
    public void clearEventList(){
//        eventArrayList ="";
        isActive = false;
    }
}