package com.uwika.laundrysystem;

public class ShopModel {
    String uuid, name, open_time, close_time, address, owner_name, image_url;

    public ShopModel(String uuid, String name, String open_time, String close_time, String address, String owner_name, String image_url){
        this.uuid = uuid;
        this.name = name;
        this.open_time = open_time;
        this.close_time = close_time;
        this.address = address;
        this.owner_name = owner_name;
        this.image_url = image_url;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public String getAddress() {
        return address;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getImage_url(){
        return image_url;
    }
}
