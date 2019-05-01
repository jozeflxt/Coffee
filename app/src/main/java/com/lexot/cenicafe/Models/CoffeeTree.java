package com.lexot.cenicafe.Models;

import java.io.Serializable;
import java.util.List;

public class CoffeeTree implements Serializable {
    public Integer Id;
    public Integer BackendId;
    public Integer BatchId;
    public Integer Index;
    public Double Lat;
    public Double Lng;
    public Boolean Synced;
    public Integer NoUsedBranchesCount;
    public Integer BatchBackendId;

    public String toString()
    {
        return "Árbol " + Id.toString() + " Lote " + BatchId;
    }
}