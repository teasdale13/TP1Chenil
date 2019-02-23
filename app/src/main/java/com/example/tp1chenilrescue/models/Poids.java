package com.example.tp1chenilrescue.models;

import android.annotation.SuppressLint;

import com.example.tp1chenilrescue.BR;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Poids extends BaseObservable {

    private int id;
    private String date;
    private String poids;
    private int dog_id;


    public int getDog_id() {
        return dog_id;
    }

    public void setDog_id(int dog_id) {
        this.dog_id = dog_id;
    }

    private String chien_nom;
    private int year, month, day;

    @Bindable
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        notifyPropertyChanged( BR.year );
    }

    @Bindable
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        notifyPropertyChanged( BR.month );
    }

    @Bindable
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        notifyPropertyChanged( BR.day );
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged( BR.id );
    }

    @Bindable
    public String getDate() {
        return birthDateFormat();
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged( BR.date );
    }

    @Bindable
    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
        notifyPropertyChanged( BR.poids );
    }

    @Bindable
    public String getChien_nom() {
        return chien_nom;
    }

    public void setChien_nom(String chien_nom) {
        this.chien_nom = chien_nom;
        notifyPropertyChanged( BR.chien_nom );
    }



    public Poids(int pID, String date, String weight, String nom ){
        setDateToInt( date );
        this.setChien_nom( nom );
        this.setDate( date );
        this.setId( pID );
        this.setPoids( weight );
    }

    public Poids(){

    }

    @SuppressLint("SimpleDateFormat")
    public String birthDateFormat(){
        if (day == 0 && month == 0 && year == 0){
            return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }
        return String.valueOf( day + "/" + (month +1) + "/" + year );
    }

    private void setDateToInt(String dateString){
        String[] stringDate = dateString.split( "/", 3 );
        this.setDay( Integer.parseInt( stringDate[0] ) );
        this.setMonth( Integer.parseInt( stringDate[1] ) );
        this.setYear( Integer.parseInt( stringDate[2] ) );
    }

}
