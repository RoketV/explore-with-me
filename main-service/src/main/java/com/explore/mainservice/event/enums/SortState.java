package com.explore.mainservice.event.enums;

public enum SortState {

    EVENT_DATE("eventDate"),
    VIEWS("views");

    private final String field;

    SortState(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
