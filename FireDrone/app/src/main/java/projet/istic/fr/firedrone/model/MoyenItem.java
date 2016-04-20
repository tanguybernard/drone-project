package projet.istic.fr.firedrone.model;

/**
 * Created by tbernard on 19/04/16.
 */
public class MoyenItem {

    private String name;

    private Boolean selected;

    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
