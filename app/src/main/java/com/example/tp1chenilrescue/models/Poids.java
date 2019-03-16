package com.example.tp1chenilrescue.models;

import android.annotation.SuppressLint;

import com.example.tp1chenilrescue.BR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Poids extends BaseObservable {

    private int id;
    private String date;
    private String poids;
    private int dog_id;
    private Date myDate;


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
        setDateToInt( date );
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

    /**
     * Méthode qui prends les données du DatePicker et qui le "format" pour l'affichage à l'utilisateur.
     * @return la date de naissance en format String
     */
    @SuppressLint("SimpleDateFormat")
    public String birthDateFormat(){

        if (day == 0 && month == 0 && year == 0){
            return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }
        return String.valueOf( day + "/" + (month + 1) + "/" + year );
    }

    /**
     * Méthode qui prends la date du poids de format String et la déquortique pour en 3
     * pour peupler le DatePicker.
     * @param dateString date de la pesée.
     */
    @SuppressLint("SimpleDateFormat")
    private void setDateToInt(String dateString){
        try {
            Date date = new SimpleDateFormat( "dd/MM/yyyy" ).parse( dateString );
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( date );

            this.setYear( calendar.get( Calendar.YEAR ) );
            this.setMonth( calendar.get( Calendar.MONTH ) );
            this.setDay( calendar.get( Calendar.DAY_OF_MONTH ));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
