package com.example.dashingqi.yckstudy;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by dashingqi on 23/4/18.
 */

public class SharedPrefsUtils {

    private static Context ApplicationContext;
    private static SharedPreferences sp;

    public static void init(Context c) {

        if (ApplicationContext == null) {
            synchronized (SharedPrefsUtils.class) {
                if (ApplicationContext == null && c != null) {
                    ApplicationContext = c.getApplicationContext();
                }
            }
        }
        if (sp == null) {
            sp = c.getSharedPreferences("name.xml", Context.MODE_PRIVATE);
        }

    }

    public SharedPreferences getSp() {
        assert sp != null;
        return sp;
    }


    public SharedPreferences.Editor editSp() {

        SharedPreferences.Editor edit = getSp().edit();
        return edit;

    }


    public void removeValue(String key) {

        SharedPreferences.Editor remove = editSp().remove(key);
        remove.commit();
    }


    public void putValueAndKeys(Map<String, ?> params) {

        if (params != null && params.size() > 0) {
            SharedPreferences.Editor editor = editSp();
            for (Map.Entry<String, ?> value : params.entrySet()) {
                Object valueValue = value.getValue();

                if (valueValue instanceof Integer) {

                    editor.putInt(value.getKey(), (int) valueValue);
                } else if (valueValue instanceof Boolean) {

                    editor.putBoolean(value.getKey(), (boolean) valueValue);
                } else if (valueValue instanceof CharSequence) {

                    editor.putString(value.getKey(), valueValue.toString());
                } else if (valueValue instanceof Long) {

                    editor.putLong(value.getKey(), (Long) valueValue);
                } else if (valueValue instanceof Float) {

                    editor.putFloat(value.getKey(), (Float) valueValue);
                } else {
                    throw new ClassCastException("不支持类型:" + valueValue.getClass().getCanonicalName());
                }
            }
            editor.commit();
        }

    }


}
