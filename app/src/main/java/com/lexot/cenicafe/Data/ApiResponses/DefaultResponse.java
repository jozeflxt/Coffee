package com.lexot.cenicafe.Data.ApiResponses;

import java.io.Serializable;

public class DefaultResponse implements Serializable {
    public Boolean isSuccess;
    public Integer id;
    public String error;
    public String error_description;
}
