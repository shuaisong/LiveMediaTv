package com.tangmu.app.TengKuTV.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits;
    private final Pattern pattern;
    private static final String POINTER = ".";

    /**
     * Constructor.
     *
     * @param decimalDigits maximum decimal digits
     */
    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
        pattern = Pattern.compile("([0-9]|\\.)*");
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {


        String sourceText = source.toString();
        String destText = dest.toString();
        if (TextUtils.isEmpty(sourceText)) {
            if (dstart == 0 && destText.indexOf(POINTER) == 1) {//保证小数点不在第一个位置
                return "0";
            }
            return "";
        }
        Matcher matcher = pattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.contentEquals(source)) { //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入decimalDigits位
            int index = destText.indexOf(POINTER);
            int length = destText.trim().length() - index;
            if (length > decimalDigits && dstart > index) {
                return "";
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.contentEquals(source)) && dstart == 0) {//第一个位置输入小数点的情况
                    return "0.";
                } else if (source.toString().matches("0+")) {
                    if (dest.length() == 0)
                        return source.toString().replaceFirst("0+", "0");
                    if ("0".contentEquals(dest))
                        return "";
                }
            }
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }

}
