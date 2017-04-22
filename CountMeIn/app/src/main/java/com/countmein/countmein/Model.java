package com.countmein.countmein;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Home on 4/13/2017.
 */

public class Model {

    private String naslov;
    private String opis;
    private Date datum;

    public Model(String naslov, String opis, Date datum) {
        this.naslov = naslov;
        this.opis = opis;
        this.datum = datum;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Model() {


    }

    public String getNaslov() {

        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }
}
