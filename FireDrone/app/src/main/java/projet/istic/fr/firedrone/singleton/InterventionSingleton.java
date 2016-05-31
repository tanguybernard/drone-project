package projet.istic.fr.firedrone.singleton;

import projet.istic.fr.firedrone.model.Intervention;

/**
 * Created by tbernard on 23/04/16.
 */
public class InterventionSingleton
{
    /** Constructeur privé */
    private InterventionSingleton()
    {}

    /** Instance unique non préinitialisée */
    private static InterventionSingleton INSTANCE = null;

    private Intervention intervention;

    /** Point d'accès pour l'instance unique du singleton */
    public static InterventionSingleton getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new InterventionSingleton();
        }
        return INSTANCE;
    }

    public void setIntervention(Intervention intervention){
        this.intervention=intervention;
    }

    public Intervention getIntervention(){
        return this.intervention;
    }

    public void reset() {
        intervention = new Intervention();
    }

}