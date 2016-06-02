package projet.istic.fr.firedrone.synchro;


import java.util.ArrayList;

/**
 * Created by tbernard on 28/04/16.
 */
public class MyObservable implements Observable {

    private static MyObservable INSTANCE = null;

    public static MyObservable getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new MyObservable();
        }
        return INSTANCE;
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
    private Observateur fragment1;

    public void setFragment(Observateur fragment){
        this.fragment = fragment;
    }

    public void setFragment1(Observateur fragment1){
        this.fragment1 = fragment1;
    }

    public Observateur getFragment(){
        return this.fragment;
    }



    @Override
    public void notifierObservateurs() {
        fragment.actualiser(this);
        if(fragment1 != null){
            fragment1.actualiser(this);
        }
        /*for(int i=0;i<tabObservateur.size();i++)
        {
            Observateur o = (Observateur) tabObservateur.get(i);
            o.actualiser(this);// On utilise la méthode "tiré".
        }*/

    }


}
