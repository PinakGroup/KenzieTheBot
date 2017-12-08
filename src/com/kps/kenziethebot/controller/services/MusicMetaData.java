package com.kps.kenziethebot.controller.services;

public class MusicMetaData {

    /**
     * This class is used to store music metadata properties.
     */
    private Integer _id;
    private String name;
    private String path;
    public static MusicMetaData instance;

    public MusicMetaData(Integer _id, String name, String path) {

        this._id = _id;
        this.name = name;
        this.path = path;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "> Id = " + _id + "\n>Name = " + name + "\n>Path = " + path;
    }
}
