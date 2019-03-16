package com.example.tp1chenilrescue.models;

import com.example.tp1chenilrescue.BR;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Race extends BaseObservable {

    private String name;
    private String note;
    private Integer id;
    private String country;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged( BR.note );
    }

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged( BR.country );
    }

    public Race() {
    }

    public Race (@Nullable Integer raceID, String nom, String pays, String _note){
        this.setCountry( pays );
        this.setId( raceID );
        this.setName( nom );
        this.setNote( _note );

    }


    @Override
    public String toString() {
        return name;
    }
}
