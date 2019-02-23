package com.example.tp1chenilrescue.models;

import com.example.tp1chenilrescue.BR;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Chenil extends BaseObservable {

    private Integer id;
    private String name;
    private String address;
    private String ville;
    private String code_postal;
    private Float longitude;
    private Float latitude;

    @Bindable
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
        notifyPropertyChanged( BR.longitude );
    }

    @Bindable
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
        notifyPropertyChanged( BR.latitude );
    }

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        notifyPropertyChanged( BR.id );
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged( BR.name );
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged( BR.address );
    }

    @Bindable
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
        notifyPropertyChanged( BR.ville );
    }

    @Bindable
    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
        notifyPropertyChanged( BR.code_postal );
    }


    public Chenil( @Nullable Integer id, String name, String address, String ville, String codePostal,
                   @Nullable Float mLongitude, @Nullable Float mLatitude){

        this.setId( id );
        this.setName( name );
        this.setAddress( address );
        this.setVille( ville );
        this.setCode_postal( codePostal.toUpperCase() );
        this.setLatitude( mLatitude );
        this.setLongitude( mLongitude );


    }

    public Chenil() {
    }

    public String idString(){
        return String.valueOf( id );
    }

    public String latituteString(){
        return String.valueOf( latitude );

    }

    public String longitudeString(){
        return String.valueOf( longitude );
    }


}
