package projet.istic.fr.firedrone.map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by ramage on 22/04/16.
 */
public interface MethodCallWhenDrag {

    void dragStart();
    void dragEnd(Marker marker);

    boolean displayButton(Marker marker);

    void dragRemove(Marker marker);
}
