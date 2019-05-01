package com.lexot.cenicafe.Models;

import java.io.Serializable;
import java.util.List;

import ir.mirrajabi.searchdialog.core.Searchable;

public class CoffeeBatch implements Serializable, Searchable {
    public Integer BackendId;
    public Integer Id;
    public Integer Age;
    public Integer Trees;
    public Integer BranchesAmmount;
    public String Name;
    public Integer Stems;
    public Boolean Synced;
    public List<CoffeeBranch> CoffeeBranches;

    @Override
    public String getTitle() {
        return Name;
    }
}