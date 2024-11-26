package com.smp.notification.dto;

import lombok.Data;

@Data
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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
