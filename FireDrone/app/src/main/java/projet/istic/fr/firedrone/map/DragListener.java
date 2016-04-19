package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ramage on 18/04/16.
 */
public class DragListener implements GoogleMap.OnMarkerDragListener {

    public static final int COLOR_BUTTON = Color.TRANSPARENT;
    private ImageButton suppressionMarker;
    private GoogleMap myMap;
    private ManagePolyline polylineManager;

    public DragListener(ImageButton pSuppressionMarker,GoogleMap pMap,ManagePolyline polylinesOption){
        suppressionMarker=pSuppressionMarker;
        myMap=pMap;
        polylineManager=polylinesOption;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        suppressionMarker.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Point markerScreenPosition = myMap.getProjection().toScreenLocation(marker.getPosition());
        //si on drag au dessus du bouton de suppression
        if (overlap(markerScreenPosition, suppressionMarker)) {
            //on modifie la couleur de fond du bouton
            suppressionMarker.setBackgroundColor(Color.argb(40, 183, 210, 226));
        }else{

            suppressionMarker.setBackgroundColor(COLOR_BUTTON);
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Point markerScreenPosition = myMap.getProjection().toScreenLocation(marker.getPosition());
        if (overlap(markerScreenPosition, suppressionMarker)) {
            marker.remove();
            suppressionMarker.setBackgroundColor(COLOR_BUTTON);
            polylineManager.removePoint(marker);
        }else{
            polylineManager.addPolyline(marker);
        }
        suppressionMarker.setVisibility(View.INVISIBLE);
    }

    /**
     * Vérifie si le point est au est se situe au niveau du bouton
     * @param point : point de la carte
     * @param imgview : button
     * @return vrai si le point se trouve sur le bouton
     */
    private boolean overlap(Point point, ImageView imgview) {
        int[] imgCoords = new int[2];
        imgview.getLocationOnScreen(imgCoords);
        boolean overlapX = point.x < imgCoords[0] + imgview.getWidth() && point.x > imgCoords[0] - imgview.getWidth();
        boolean overlapY = point.y < imgCoords[1] + imgview.getHeight() && point.y > imgCoords[1] - imgview.getWidth();
        return overlapX && overlapY;
    }
}
