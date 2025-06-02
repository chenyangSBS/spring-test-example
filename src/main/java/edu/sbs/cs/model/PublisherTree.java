package edu.sbs.cs.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublisherTree {
    private String publisher;
    private List<String> authors = new ArrayList<>();

    public PublisherTree(String publisher) {
        this.publisher = publisher;
    }
}
