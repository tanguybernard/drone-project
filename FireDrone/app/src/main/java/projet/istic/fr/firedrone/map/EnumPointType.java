package projet.istic.fr.firedrone.map;

import projet.istic.fr.firedrone.R;

/**
 * Created by ramage on 25/04/16.
 */
public enum  EnumPointType {
    SOURCE(R.drawable.triangle),
    CIBLE(R.drawable.triangle_bas_vert),
    SINISTRE(R.drawable.accident_rouge);

    private Integer idImage;

    EnumPointType(int idImage){
        this.idImage = idImage;
    }

    public Integer getResource(){
        return idImage;
    }
}
