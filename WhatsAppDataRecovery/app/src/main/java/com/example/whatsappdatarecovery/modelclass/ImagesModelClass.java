package com.example.whatsappdatarecovery.modelclass;

public class ImagesModelClass {
    String path;
    String name;

    public ImagesModelClass(String absolutePath, String toLowerCase) {
        path=absolutePath;
        name=toLowerCase;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
