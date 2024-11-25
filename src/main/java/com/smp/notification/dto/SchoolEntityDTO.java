package com.smp.notification.dto;

public class SchoolEntityDTO {

    private Long id; // ID школы
    private String name; // Название школы
    private String address; // Адрес школы
    private boolean isNew; // Флаг для определения, новая ли школа

    public SchoolEntityDTO() {
    }

    public SchoolEntityDTO(Long id, String name, String address, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isNew = isNew;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
