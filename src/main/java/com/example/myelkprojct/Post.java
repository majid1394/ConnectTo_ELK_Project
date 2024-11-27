package com.example.myelkprojct;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;


public class Post {
    private String title;
    private String description;
    //private String link;

    public Post(String title, String description/*, String link*/) {
        this.title = title;
        this.description = description;
        //this.link = link;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("price")
    public String getPrice() {
        return description;
    }

    /*@JsonProperty("link")
    public String getLink() {
        return link;
    }*/
}