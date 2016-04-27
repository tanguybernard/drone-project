package projet.istic.fr.firedrone.synchro;

/**
 * Created by tbernard on 27/04/16.
 */
public interface Observateur {

    // Méthode appelée automatiquement lorsque l'état (position ou précision) du GPS change.
    public void actualiser(Observable o);
}
