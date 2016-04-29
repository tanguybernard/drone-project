package projet.istic.fr.firedrone.synchro;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by tbernard on 28/04/16.
 */
public class MyObservable implements Observable {


    private static MyObservable INSTANCE = null;
    public  String toto;

    public static MyObservable getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new MyObservable();
        }
        return INSTANCE;

    }

    public void lol(String test){
        System.out.println(test);
        toto = test;
    }









    private ArrayList tabObservateur=new ArrayList();// Tableau d'observateurs.
    @Override
    public void ajouterObservateur(Observateur o) {

        tabObservateur.add(o);

    }

    @Override
    public void supprimerObservateur(Observateur o) {

    }

    private Observateur fragment;

    public void setFragment(Observateur fragment){
        this.fragment = fragment;

    }

    public Observateur getFragment(){
        return this.fragment;
    }



    @Override
    public void notifierObservateurs() {


        fragment.actualiser(this);

        /*for(int i=0;i<tabObservateur.size();i++)
        {
            Observateur o = (Observateur) tabObservateur.get(i);
            o.actualiser(this);// On utilise la méthode "tiré".
        }*/

    }


}
