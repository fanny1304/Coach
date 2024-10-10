package com.example.coach.controleur;

import android.content.Context;

import com.example.coach.modele.AccesLocal;
import com.example.coach.modele.AccesDistant;
import com.example.coach.modele.Profil;
import com.example.coach.outils.Serializer;
import com.example.coach.vue.MainActivity;

import org.json.JSONObject;

import java.util.Date;

/**
 * Classe singleton Controle : répond aux attentes de l'activity
 */
public final class Controle {

    private static Controle instance = null;
    private static Profil profil;
    private static String nomFic = "saveprofil";
    //private static AccesLocal accesLocal;
    private static AccesDistant accesDistant;
    private static Context context;

    public Controle(Context context) {
        //recupSerialize(context);
        //accesLocal = AccesLocal.getInstance(context);
        //profil = accesLocal.recupDernier();
        if (context!=null){
            Controle.context=context;
        }

    }

    /**
     * Création d'une instance unique de la classe
     * @return l'instance unique
     */
    public final static Controle getInstance(Context context) {
        if (instance == null){
            instance = new Controle(context);
            accesDistant = AccesDistant.getInstance();
            accesDistant.envoi("dernier", new JSONObject());
        }
        return instance;
    }

    /**
     * Création du profil par rapport aux informations saisies
     * @param poids
     * @param taille en cm
     * @param age
     * @param sexe 1 pour homme et 0 pour femme
     */
    public void creerProfil(Integer poids, Integer taille, Integer age, Integer sexe){
        profil = new Profil(new Date(), poids, taille, age, sexe);
        //Serializer.serialize(nomFic, profil, context);
        //accesLocal.ajout(profil);
        accesDistant.envoi("enreg", profil.convertToJSONObject());
    }

    /**
     * getter sur le résultat du calcul de l'IMG pour le profil
     * @return img du profil
     */
    public float getImg(){
        if(profil != null){
            return profil.getImg();
        }else {
            return 0;
        }
    }

    /**
     * getter sur le message correspondant à l'IMG du profil
     * @return message du profil
     */
    public String getMessage(){
        if(profil!=null){
            return profil.getMessage();
        }else{
            return "";
        }
    }

    /**
     * getter sur le poids si le profil existe
     * @return
     */
    public Integer getPoids(){
        if (profil==null){
            return null;
        }else{
            return profil.getPoids();
        }
    }

    /**
     * getter sur la taille si le profil existe
     * @return
     */
    public Integer getTaille(){
        if (profil==null){
            return null;
        }else{
            return profil.getTaille();
        }
    }

    /**
     * getter sur l'age si le profil existe
     * @return
     */
    public Integer getAge(){
        if (profil==null){
            return null;
        }else{
            return profil.getAge();
        }
    }

    /**
     * getter sur le sexe si le profil existe
     * @return
     */
    public Integer getSexe(){
        if (profil==null) {
            return null;
        }else{
            return profil.getSexe();
        }
    }

    /**
     * Récupération du profil sérialisé
     * @param context
     */
    private static void recupSerialize(Context context){
        profil = (Profil)Serializer.deSerialize(nomFic, context);
    }

    /**
     * Valorisation du profil
     * @param profil
     */
    public void setProfil(Profil profil){
        Controle.profil = profil;
        ((MainActivity)context).recupProfil();
    }


}
