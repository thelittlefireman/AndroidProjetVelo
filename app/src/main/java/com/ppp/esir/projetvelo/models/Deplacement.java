package com.ppp.esir.projetvelo.models;

/**
 * Created by thoma on 18/05/2016.
 */
public class Deplacement {
    private String vitesse, depart, arrivee, distance;

    public Deplacement(String vitesse, String depart, String arrivee, String distance) {
        this.vitesse = vitesse;
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


    public String getDepart() {
        return depart;
    }

    public String getArrivee() {
        return arrivee;
    }


}
