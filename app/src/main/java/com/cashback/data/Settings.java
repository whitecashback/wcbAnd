package com.cashback.data;

/**
 * Created by dev on 30/3/17.
 */

public class Settings {

    public int id,isChecked=1;
    public String name;
    public Settings(int id,String name,int isChecked)
    {
        this.id=id;
        this.name=name;
        this.isChecked=isChecked;
    }


}
