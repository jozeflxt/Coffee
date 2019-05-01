package com.lexot.cenicafe.Models;

import java.io.Serializable;

public class CoffeeFrame implements Serializable {
    public Integer Id;
    public String Data;
    public Integer BranchId;
    public Integer Time;
    public Double Factor;
    public Boolean Synced;
    public Integer BranchBackendId;
}
