package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by fracma on 3/14/16.
 */
public class Intervention {
    @Id
    private String id;

    private String content;
    private String date;
    private String type;
    private String author;

    public Intervention() {
    }

    public Intervention(String id, String content, String date, String type, String author) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.type = type;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
