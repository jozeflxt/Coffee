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
    public Integer FramesCount;
    public Integer Synced;
    public Integer TreeBackendId;

    public String getPath() {
        return "/mnt/sdcard/cenicafe/" + Id.toString();
    }
}