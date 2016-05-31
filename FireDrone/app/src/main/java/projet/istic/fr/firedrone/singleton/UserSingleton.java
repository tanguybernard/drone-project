package projet.istic.fr.firedrone.singleton;

import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.User;

/**
 * Created by mamadian on 28/04/16.
 */
public class UserSingleton {

    /** Constructeur privé */
    private UserSingleton()
    {}

    /** Instance unique non préinitialisée */
    private static UserSingleton INSTANCE = null;

    private User user;

    /** Point d'accès pour l'instance unique du singleton */
    public static UserSingleton getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new UserSingleton();
        }
        return INSTANCE;
    }

    public void setUser(User user){
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void reset() {
        user = new User();
    }
}
