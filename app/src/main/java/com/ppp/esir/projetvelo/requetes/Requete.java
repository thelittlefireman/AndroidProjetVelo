package com.ppp.esir.projetvelo.requetes;

import java.util.HashMap;

/**
 * Created by thoma on 02/05/2016.
 */
public class Requete {
    public static boolean getUserAuthentification(String username, String password) {
        HashMap<String, String> dataPost = new HashMap<>();
        String rst = RequeteConfigurator.sendRequetePost(RequeteConfigurator.ApiRequete.AUTHENTIFICATION, dataPost);
        if (!rst.equals("") && !rst.equals("false")) {
            return true;
        }
        return false;
    }
}
