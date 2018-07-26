package com.example.textevaluatordemo;

import android.animation.TypeEvaluator;

/**
 * Created by dashingqi on 4/5/18.
 */

public class MyEvaluator implements TypeEvaluator<Character> {
    @Override
    public Character evaluate(float fraction, Character startValue, Character endValue) {

        int start = (int) startValue;
        int end = (int)endValue;

        char value = (char) (startValue+(end-start)*fraction);
        return value;
    }
}
