package projet.istic.fr.firedrone.map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by ramage on 18/04/16.
 */
public interface ManagePolyline {
    void updatePolyline();
    void removePoint(Marker marker);
    void addPolyline(Marker marker);
}
