package projet.istic.fr.firedrone.synchro;

/**
 * Created by tbernard on 27/04/16.
 */
public interface Observable {

    // Méthode permettant d'ajouter (abonner) un observateur.
    public void ajouterObservateur(Observateur o);
    // Méthode permettant de supprimer (résilier) un observateur.
    public void supprimerObservateur(Observateur o);
    // Méthode qui permet d'avertir tous les observateurs lors d'un changement d'état.
    public void notifierObservateurs();

}
