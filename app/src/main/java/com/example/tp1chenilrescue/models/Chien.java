package com.example.tp1chenilrescue.models;

import android.annotation.SuppressLint;

import com.example.tp1chenilrescue.BR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Chien extends BaseObservable {

    private int id;
    private String nom;
    private String date_naissance;
    private String pere;
    private String mere;
    private String race;
    private String sexe;
    private boolean female;
    private Integer idPere, mereId, raceId;
    private int dogYear, dogMonth, dogDay;
    private int age;

    //region GETTER AND SETTER


    public int getAge() {
        return age;
    }

    public void setAge(String dateNaissance) {
        setDateToInt( dateNaissance );
        this.age = getAgeFromBirth( new Date(  ) );
    }

    @Bindable
    public Integer getIdPere() {
        return idPere;
    }

    public void setIdPere(Integer idPere) {
        this.idPere = idPere;
        notifyPropertyChanged( BR.idPere );
    }

    @Bindable
    public Integer getMereId() {
        return mereId;

    }

    public void setMereId(Integer mereId) {
        this.mereId = mereId;
        notifyPropertyChanged( BR.mere );
    }

    @Bindable
    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
        notifyPropertyChanged( BR.race );
    }


    @Bindable
    public int getDogYear() {
        return dogYear;
    }

    @Bindable
    public void setDogYear(int dogYear) {
        this.dogYear = dogYear;
        notifyPropertyChanged( BR.dogYear);
    }

    @Bindable
    public int getDogMonth() {
        return dogMonth;
    }

    @Bindable
    public void setDogMonth(int dogMonth) {
        this.dogMonth = dogMonth;
        notifyPropertyChanged( BR.dogMonth );
    }

    @Bindable
    public int getDogDay() {
        return dogDay;
    }

    @Bindable
    public void setDogDay(int dogDay) {
        this.dogDay = dogDay;
        notifyPropertyChanged( BR.dogDay );
    }

    @Bindable
    public boolean isFemale() {
        return female;
    }


    public void setFemale(boolean female) {
        this.female = female;
        notifyPropertyChanged( BR.female );
    }

    @Bindable
    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
        notifyPropertyChanged( BR.sexe );
        if (sexe.equals( "F" )){
            this.setFemale( true );
        }else {
            this.setFemale( false );
        }
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
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        notifyPropertyChanged( BR.nom );
    }

    @Bindable
    public String getDate_naissance() {
        return birthDateFormat();
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
        notifyPropertyChanged( BR.date_naissance );
    }

    @Bindable
    public String getPere() {
        return pere;
    }

    public void setPere(String pere) {
        this.pere = pere;
        notifyPropertyChanged( BR.pere );
    }

    @Bindable
    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
        notifyPropertyChanged( BR.mere );
    }

    @Bindable
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
        notifyPropertyChanged( BR.race );
    }
    //endregion

    public Chien(int id, String nom, String pere, String mere, String race, String sexe, String birthDate){
        setDateToInt( birthDate );
        this.setId( id );
        this.setNom( nom );
        this.setDate_naissance( birthDate );
        this.setPere( pere );
        this.setMere( mere );
        this.setRace( race );
        this.setSexe( sexe );
    }

    public Chien(){

    }


    @SuppressLint("SimpleDateFormat")
    public String birthDateFormat(){
        if (dogDay == 0 && dogMonth == 0 && dogYear == 0){
           return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }
        return String.valueOf( dogDay + "/" + String.valueOf(dogMonth < 10 ? "0" + (dogMonth) : dogMonth) + "/" + dogYear );

    }


    private int getAgeFromBirth(Date currentDate) {

        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(  );
        long dateTime = Date.parse( date_naissance );
        date.setTime( dateTime );
        int d1 = Integer.parseInt(formatter.format( date ));
        int d2 = Integer.parseInt(formatter.format(currentDate));
        int age = (d2 - d1) / 10000;
        return age;

    }

    private void setDateToInt(String dateString){
        String[] stringDate = dateString.split( "/", 3 );
        this.setDogDay( Integer.parseInt( stringDate[0] ) );
        if (Integer.parseInt(stringDate[1]) < 10){
            String zero = "0";
            this.setDogMonth( Integer.parseInt( zero.concat(stringDate[1] )) );
        } else {
            this.setDogMonth( Integer.parseInt( stringDate[1] ));
        }

        this.setDogYear( Integer.parseInt( stringDate[2] ) );

        String dateNaissance = birthDateFormat();
    }


    @Override
    public String toString() {
        return nom;
    }
}
