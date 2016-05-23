package fr.istic.sit.notification;

/**
 * @author FireDroneTeam
 */
public class PushyPushRequest {
    public Object data;
    public String[] registration_ids;

    public PushyPushRequest(Object data, String[] registrationIDs)
    {
        this.data = data;
        this.registration_ids = registrationIDs;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String[] getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(String[] registration_ids) {
        this.registration_ids = registration_ids;
    }
}
