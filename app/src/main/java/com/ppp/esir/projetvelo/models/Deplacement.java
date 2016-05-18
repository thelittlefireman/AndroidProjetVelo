package com.ppp.esir.projetvelo.models;

/**
 * Created by thoma on 18/05/2016.
 */
public class Deplacement {
    private String vitesse, deplacement, depart, arrivee, distance;

    public Deplacement(String vitesse, String deplacement, String depart, String arrivee, String distance) {
        this.vitesse = vitesse;
        this.deplacement = deplacement;
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
    }

    public String getVitesse() {
        return vitesse;
    }

    public String getDistance() {
        return distance;
    }

    public String getDeplacement() {
        return deplacement;
    }

    public String getDepart() {
        return depart;
    }

    public String getArrivee() {
        return arrivee;
    }


}
