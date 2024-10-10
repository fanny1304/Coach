package com.example.coach.modele;

import android.util.Log;

import com.example.coach.controleur.Controle;
import com.example.coach.outils.AccesHTTP;
import com.example.coach.outils.AsyncResponse;
import com.example.coach.outils.MesOutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AccesDistant implements AsyncResponse {

    private static final String SERVERADDR = "http://192.168.56.1/coach/serveurcoach.php";
    private static AccesDistant instance;
    private Controle controle;

    private AccesDistant(){
        controle = Controle.getInstance(null);
    }

    public static AccesDistant getInstance(){
        if (instance==null){
            instance = new AccesDistant();
        }
        return instance;
    }

    /**
     * Retour du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output){
        Log.d("serveur", "**********" + output);
        try{
            JSONObject retour = new JSONObject(output);
            String code = retour.getString("code");
            String message = retour.getString("message");
            String result = retour.getString("result");
            if (!code.equals("200")){
                Log.d("erreur", "********** retour serveur code="+code+" result="+result);
            }else{
                if (message.equals("dernier")){
                    JSONObject info = new JSONObject(result);
                    Integer poids = info.getInt("poids");
                    Integer taille = info.getInt("taille");
                    Integer age = info.getInt("age");
                    Integer sexe = info.getInt("sexe");
                    Date datemesure = MesOutils.convertStringToDate(info.getString("datemesure"), "yyyy-MM-dd hh:mm:ss");
                    Profil profil = new Profil(datemesure, poids, taille, age, sexe);
                    controle.setProfil(profil);
                }
            }
        }catch(JSONException e){
            Log.d("erreur", "********** output n'est pas au format JSON");
        }
    }

    public void envoi(String operation, JSONObject lesDonneesJSON){
        AccesHTTP accesDonnees = new AccesHTTP();
        accesDonnees.delegate = this;
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonnees", lesDonneesJSON.toString());
        accesDonnees.execute(SERVERADDR);
    }


}
