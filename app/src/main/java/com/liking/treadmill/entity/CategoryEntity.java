package com.liking.treadmill.entity;

import java.io.Serializable;

/**
 * Created on 2017/08/21
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class CategoryEntity implements Serializable{

    public int categoryRes;
    public String categoryName;

    public CategoryEntity(int cRes, String cName) {
        this.categoryRes = cRes;
        this.categoryName = cName;
    }
}
