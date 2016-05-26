package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

import projet.istic.fr.firedrone.R;

/**
 * Created by mamadian on 24/05/16.
 */
public class PanelListDroneFragment extends Fragment implements Serializable {

    private transient MapMoyenFragment mapDroneFragment;
    private Boolean segment=false, zone=false, boucle=false;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){
        final View view = inflater.inflate(R.layout.fragment_list_panel_drone, container, false);
        buttonClicked(view);
        return  view;
    }


    public void buttonClicked(View view){
        final Button zone = (Button) view.findViewById(R.id.Bzone);
        final Button segment = (Button) view.findViewById(R.id.Bsegment);
        final Button boucle = (Button) view.findViewById(R.id.Bboucle);

        //choix d'une balayge par zone
        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setZone(Boolean.TRUE);
                setSegment(Boolean.FALSE);
                setBoucle(Boolean.FALSE);

               /* if(zone.getBackground().getAlpha()!=Color.BLUE ){
                    zone.setTextColor(Color.WHITE);
                    zone.setBackgroundColor(Color.BLUE);
                }
                else{
                    zone.setTextColor(Color.BLACK);
                    zone.setBackgroundColor(Color.GRAY);
                }*/
            }
        });
        //choix d'un balayage par segment
        segment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSegment(Boolean.TRUE);
                setZone(Boolean.FALSE);
                setBoucle(Boolean.FALSE);
            }
        });


        //choix d'un balayage par boucle
        boucle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSegment(Boolean.FALSE);
                setZone(Boolean.FALSE);
                setBoucle(Boolean.TRUE);
            }
        });
    }

    public Boolean getSegment() {
        return segment;
    }

    public void setSegment(Boolean segment) {
        this.segment = segment;
    }

    public Boolean getZone() {
        return zone;
    }

    public void setZone(Boolean zone) {
        this.zone = zone;
    }

    public Boolean getBoucle() {
        return boucle;
    }

    public void setBoucle(Boolean boucle) {
        this.boucle = boucle;
    }

}
