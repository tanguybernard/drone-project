package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author FireDroneTeam
 */
public class Action {

    @Id
    private String id;

    private String date;
    private String login;
    private String action;
    private List<String> parameters;

    public Action() {
    }

    public Action(String date, String login, String action, List<String> parameters) {
        this.date = date;
        this.login = login;
        this.action = action;
        this.parameters = parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
