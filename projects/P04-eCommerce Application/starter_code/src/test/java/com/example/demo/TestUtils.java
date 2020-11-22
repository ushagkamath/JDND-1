/*
 * Copyright 2018 VMware, Inc.
 * All rights reserved.
 */

package com.example.demo;

import com.example.demo.controllers.CartController;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {
    private static final Logger log = LoggerFactory.getLogger(TestUtils.class);

    public static void injectObjects(Object target, String fieldName, Object toInject) {
       boolean wasPrivate = false;

        Field f = null;
        try {
            f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        } catch (IllegalAccessException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }


}
