package com.example.whatsappdatarecovery.modelclass;

public class GetImages {
    public int Images;
    public int checkedImages;
    boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public GetImages(int images, int checkedImages, String name) {
        Images = images;
        this.checkedImages = checkedImages;
        Name = name;
    }

    public void setImages(int images) {
        Images = images;
    }

    public int getCheckedImages() {
        return checkedImages;
    }

    public void setName(String name) {
        Name = name;
    }

    public String Name;

    public int getImages() {
        return Images;
    }

    public String getName() {
        return Name;
    }
}
