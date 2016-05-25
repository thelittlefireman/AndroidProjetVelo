package com.ppp.esir.projetvelo.requetes;

import android.util.Log;

import com.ppp.esir.projetvelo.models.Deplacement;
import com.ppp.esir.projetvelo.models.User;
import com.ppp.esir.projetvelo.utils.Datacontainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by thoma on 02/05/2016.
 */
public class Requete {
    public static final boolean DEBUG = true;

    /**
     * 1 = ok
     * 2= pas de compte
     * 3= mp incorrect
     *
     * @param username
     * @param password
     * @return
     */
    public static int getUserAuthentification(String username, String password) {
        HashMap<String, String> dataPost = new HashMap<>();
        dataPost.put("requete", "authentification");
        dataPost.put("login", username);
        dataPost.put("mp", password);
        String rst = RequeteConfigurator.sendRequetePost(RequeteConfigurator.ApiRequete.AUTHENTIFICATION, dataPost);
        if (DEBUG) {
            Log.i(Requete.class.getName(), "réponse:" + rst);
        }
        if (rst.equals("authentification réeussie !")) {
            return 1;
        } else if (rst.equals("pas de compte associé à ce login !")) {
            return 2;
        } else if (rst.equals("erreur d'authentification--->mot de passe  !")) {
            return 3;
        }
        return -1;
    }

    public static User getUserInformation() {
        HashMap<String, String> dataPost = new HashMap<>();
        dataPost.put("login", Datacontainer.getUsername());
        dataPost.put("password", Datacontainer.getPassword());
        String rst = RequeteConfigurator.sendRequetePost(RequeteConfigurator.ApiRequete.GET_INFORMATIONS, dataPost);
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(rst);
            jsonObject.getString("");
            //TODO
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
    //login mp nom prenom / age
    public static int inscription(User user) {
        HashMap<String, String> dataPost = new HashMap<>();
        dataPost.put("requete", "authentification");
        dataPost.put("login", user.getmLogin());
        dataPost.put("password", user.getmPassword());
        dataPost.put("nom", user.getmNom());
        dataPost.put("prenom", user.getmPrenom());
        dataPost.put("age", user.getmAge());
        dataPost.put("mail", user.getmEmail());
        String rst = RequeteConfigurator.sendRequetePost(RequeteConfigurator.ApiRequete.INSCRIPTION, dataPost);
        if (DEBUG) {
            Log.i(Requete.class.getName(), "réponse:" + rst);
        }
        if (rst.equals("echec de l'opération--->ce login existe déjà !")) {
            return 1;
        } else if (rst.equals("echec de l'opération--->ce email existe déjà !")) {
            return 2;
        }
        return 4;
    }

    public static int addDeplacement(Deplacement deplacement) {
        HashMap<String, String> dataPost = new HashMap<>();
        dataPost.put("requete", "authentification");
        dataPost.put("login", Datacontainer.getUsername());
        dataPost.put("password", Datacontainer.getPassword());
        dataPost.put("depart", deplacement.getDepart());
        dataPost.put("arrivee", deplacement.getArrivee());
        dataPost.put("vitesse", deplacement.getVitesse());//vitesse moyenne
        dataPost.put("distance", deplacement.getDistance());
        String rst = RequeteConfigurator.sendRequetePost(RequeteConfigurator.ApiRequete.ADD_DEPLACEMENT, dataPost);
        if (DEBUG) {
            Log.i(Requete.class.getName(), "réponse:" + rst);
        }
        if (rst.equals("insertion réeussie !")) {
            return 1;
        }
        return -1;
    }

}
