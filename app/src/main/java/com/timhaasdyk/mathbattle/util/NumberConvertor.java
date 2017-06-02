package com.timhaasdyk.mathbattle.util;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Tim Haasdyk on 15-May-17.
 */
public class NumberConvertor {

    public static String getTextForNumber(Number number) {
        NumberFormat formatter = new RuleBasedNumberFormat(ULocale.getDefault(), RuleBasedNumberFormat.SPELLOUT);
        return StringUtils.replaceAll(formatter.format(number), "-", " ");
    }
}