package com.lexot.cenicafe.Models;

import java.io.Serializable;
import java.util.List;

public class CoffeeBranch implements Serializable {
    public Integer Id;
    public Integer BackendId;
    public Integer Index;
    public Integer TreeId;
    public Integer StemId;
    public String Date;
    public String Data;
    public String Info;
    public String VideoUrl;
    public Integer Type;
    public Integer FramesCount;
    public Boolean Synced;
    public Integer TreeBackendId;
    public List<CoffeeFrame> CoffeeFrames;

    public String getPath() {
        return "/mnt/sdcard/cenicafe/" + Id.toString();
    }
}