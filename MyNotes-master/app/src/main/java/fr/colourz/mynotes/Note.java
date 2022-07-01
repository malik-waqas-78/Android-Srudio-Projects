package fr.colourz.mynotes;


public class Note {

    int _id;
    String _title;
    String _content;
    String _dateModified;
    String _dateCreated;

    // Empty constructor
    public Note() {

    }

    // Constructor with ID
    public Note(int id, String title, String content, String dateCreated, String dateModified) {
        this._id = id;
        this._title = title;
        this._content = content;
        this._dateCreated = dateCreated;
        this._dateModified = dateModified;
    }

    // Constructor without ID
    public Note(String title, String content, String dateCreated, String dateModified) {
        this._title = title;
        this._content = content;
        this._dateCreated = dateCreated;
        this._dateModified = dateModified;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public String getDateCreated() {
        return _dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this._dateCreated = dateCreated;
    }

    public String getDateModified() {
        return _dateModified;
    }

    public void setDateModified(String dateModified) {
        this._dateModified = dateModified;
    }
}
