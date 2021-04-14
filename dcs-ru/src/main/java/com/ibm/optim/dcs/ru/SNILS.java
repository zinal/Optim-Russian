/*
 *  (c) Copyright IBM Corp. 2020 All rights reserved.
 *
 *  The following sample of source code ("Sample") is owned by International
 *  Business Machines Corporation or one of its subsidiaries ("IBM") and is
 *  copyrighted and licensed, not sold. You may use, copy, modify, and
 *  distribute the Sample in any form without payment to IBM.
 *
 *  The Sample code is provided to you on an "AS IS" basis, without warranty of
 *  any kind.
 *  IBM HEREBY EXPRESSLY DISCLAIMS ALL WARRANTIES, EITHER EXPRESS OR
 *  IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Some jurisdictions do
 *  not allow for the exclusion or limitation of implied warranties, so the above
 *  limitations or exclusions may not apply to you. IBM shall not be liable for
 *  any damages you suffer as a result of using, copying, modifying or
 *  distributing the Sample, even if IBM has been advised of the possibility of
 *  such damages.
 *
 *  Author:   Maksim Zinal <mzinal@ru.ibm.com>
 */
package com.ibm.optim.dcs.ru;

import com.ibm.infosphere.classification.ValueBasedClassifier;
import java.util.regex.Pattern;

/**
 * СНИЛС.
 *
 * @author zinal
 */
public class SNILS implements ValueBasedClassifier {

    public static final int[] SNILS = { 9,8,7,6,5,4,3,2,1 };

    private final Pattern snilsPattern = Pattern.compile("\\d{11}");

    @Override
    public boolean matchValue(Object value) {
        if (value==null)
            return false;
        String strValue = normalize(value);
        if (!snilsPattern.matcher(strValue).matches())
            return false; // Не соответствует формату
        int[] snils = stringToDigits(strValue);
        int[] control = getControl(snils);
        if (snils.length != 11 || control.length != 2)
            return false; // Не должно происходить
        return (control[0] == snils[snils.length-2])
                && (control[1] == snils[snils.length-1]);
    }

    public static String normalize(Object value) {
        String str = DcsUtil.extractDigits(value);
        if (str.length() > 6) {
            while (str.length() < 11) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static int getChecksum(int[] digits, int[] multipliers) {
        int checksum = 0;
        for (int i = 0; i < multipliers.length; i++) {
            checksum += digits[i] * multipliers[i];
        }
        checksum = checksum % 11;
        if (checksum > 9)
            checksum = 0;
        return checksum;
    }

    // Сокращаем выделение памяти при работе функций ниже.
    private final int[] work2 = new int[2];
    private final int[] work11 = new int[11];

    public int[] getControl(int[] digits) {
        int control = 0;
        for (int i=0; i<SNILS.length; ++i) {
            control += digits[i] * SNILS[i];
        }
        if (control > 101) {
            control = control % 101;
        }
        if (control == 100 || control == 101)
            control = 0;
        work2[0] = control / 10;
        work2[1] = control % 10;
        return work2;
    }

    public int[] stringToDigits(String value) {
        int len = value.length();
        int[] digits = work11;
        if (len > digits.length)
            len = digits.length;
        for (int i=0; i<len; ++i) {
            if ( i < value.length() )
                digits[i] = Character.getNumericValue(value.charAt(i));
            else
                digits[i] = 0;
        }
        return digits;
    }
}
