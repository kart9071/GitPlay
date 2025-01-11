package com.GitPlay.GitPlay.Object;

public class Commands {

    private  final String name;
    private final String description;

    public Commands(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public final Commands CREATE_REPOSITORY=new Commands("gh repo create","command for creating the repository");
}
