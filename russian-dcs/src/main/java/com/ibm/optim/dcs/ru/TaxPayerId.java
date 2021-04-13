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
 * ИНН.
 *
 * @author zinal
 */
public class TaxPayerId implements ValueBasedClassifier {

    private static final int[] N10 = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] N11 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] N12 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final Pattern innPattern = Pattern.compile("\\d{10}|\\d{12}");

    @Override
    public boolean matchValue(Object value) {
        if (value==null)
            return false;
        String strValue = value.toString();
        if (!innPattern.matcher(strValue).matches())
            return false;
        int[] inn = stringToDigits(strValue);
        switch (inn.length) {
            case 12:
                int n11 = getChecksum(inn, N11);
                int n12 = getChecksum(inn, N12);
                return inn[inn.length - 1] == n12 && inn[inn.length - 2] == n11;
            case 10:
                int n = getChecksum(inn, N10);
                return inn[inn.length - 1] == n;
            default:
                return false;
        }
    }

    private static int getChecksum(int[] digits, int[] multipliers) {
        int checksum = 0;
        for (int i = 0; i < multipliers.length; i++) {
            checksum += digits[i] * multipliers[i];
        }
        return (checksum % 11) % 10;
    }

    private static int[] stringToDigits(String value) {
        char[] values = value.toCharArray();
        int[] digits = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            digits[i] = Character.getNumericValue(values[i]);
        }
        return digits;
    }
}
