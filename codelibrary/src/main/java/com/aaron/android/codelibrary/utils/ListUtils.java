package com.aaron.android.codelibrary.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014/11/17 19:44.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ListUtils {
    /**
     * 判断List是否为null或者size为0
     * @param list List
     * @return true：empty
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 清空List中所有null元素
     * @param list List
     * @param <T> 泛型类型
     */
    public static <T> void wipeNullElement(List<T> list) {
        List<T> newList = new ArrayList();
        for (T obj : list) {
            if (obj != null) {
                newList.add(obj);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    public static <T> List<T> createFixedLengthList(Class<T> cls, int length) {
        List<T> list = new ArrayList<>();
        try {
            T t;
            for (int i =0; i< length;i++) {
                t = cls.newInstance();
                list.add(t);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }
}
