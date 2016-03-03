package com.dusty.test;

/**
 * Created by Dustin on 3/3/2016.
 */
public class ActivitiesBridge {

    private static Object object;

    /**
     * set object to static variable and retrieve it from another activity
     *
     * @param obj
     */
    public static void setObject(Object obj) {
        object = obj;
    }

    /**
     * get object passed from previous activity
     *
     * @return
     */
    public static Object getObject() {
        Object obj = object;
// can get only once
        object = null;
        return obj;
    }
}
