package com.countmein.countmein.beans;

import java.io.Serializable;

/**
 * Created by zivic on 6/8/2017.
 */

public class IdBean extends BaseModel implements Serializable {

    public IdBean(){

    }

    public IdBean(String id){
        super(id);
    }
}
