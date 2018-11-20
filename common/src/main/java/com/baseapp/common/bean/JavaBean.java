package com.baseapp.common.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class JavaBean implements Serializable {
    private static final long serialVersionUID = -6111323241670458039L;

    public JavaBean() {
    }

    public String toString() {
        ArrayList<Field> list = new ArrayList();
        Class<?> clazz = this.getClass();
        list.addAll(Arrays.asList(clazz.getDeclaredFields()));
        StringBuilder sb = new StringBuilder();

        Field[] fields;
        Field[] var5;
        int var6;
        int var7;
        Field field;
        while(clazz != Object.class) {
            clazz = clazz.getSuperclass();
            fields = clazz.getDeclaredFields();
            var5 = fields;
            var6 = fields.length;

            for(var7 = 0; var7 < var6; ++var7) {
                field = var5[var7];
                int modifier = field.getModifiers();
                if(Modifier.isPublic(modifier) || Modifier.isProtected(modifier)) {
                    list.add(field);
                }
            }
        }

        fields = (Field[])list.toArray(new Field[list.size()]);
        var5 = fields;
        var6 = fields.length;

        for(var7 = 0; var7 < var6; ++var7) {
            field = var5[var7];
            String fieldName = field.getName();

            try {
                Object obj = field.get(this);
                sb.append(fieldName);
                sb.append("=");
                sb.append(obj);
                sb.append("\n");
            } catch (IllegalAccessException var11) {
//                LogUtils.error(var11);
            }
        }

        return sb.toString();
    }
}
