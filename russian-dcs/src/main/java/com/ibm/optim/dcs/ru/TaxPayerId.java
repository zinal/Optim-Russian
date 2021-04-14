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

    public static final int[] N10 = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final int[] N11 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final int[] N12 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    private final Pattern innPattern = Pattern.compile("\\d{10}|\\d{12}");

    @Override
    public boolean matchValue(Object value) {
        if (value==null)
            return false;
        // Переводим в строку и удаляем все пробелы
        String strValue = normalize(value);
        if (!innPattern.matcher(strValue).matches())
            return false; // Не соответствует формату
        int[] inn = stringToDigits(strValue);
        switch (inn.length) {
            case 12:
                int n11 = getChecksum(inn, N11);
                int n12 = getChecksum(inn, N12);
                return inn[inn.length - 1] == n12 && inn[inn.length - 2] == n11;
            case 10:
                int n = getChecksum(inn, N10);
                return inn[inn.length - 1] == n;
            default: // На практике срабатывать не должно
                return false;
        }
    }
    
    public static String normalize(Object value) {
        return value.toString().trim().replaceAll("\\s", "");
    }

    public static int getChecksum(int[] digits, int[] multipliers) {
        int checksum = 0;
        for (int i = 0; i < multipliers.length; i++) {
            checksum += digits[i] * multipliers[i];
        }
        return (checksum % 11) % 10;
    }
    
    // Сокращаем выделение памяти при работе stringToDigits().
    private final int[] work10 = new int[10];
    private final int[] work12 = new int[12];

    public int[] stringToDigits(String value) {
        int len = value.length();
        int[] digits = work12;
        if (len<work12.length) {
            digits = work10;
            len = work10.length;
        } else if (len>work12.length) {
            len = work12.length;
        }
        for (int i=0; i<len; ++i) {
            if ( i < value.length() )
                digits[i] = Character.getNumericValue(value.charAt(i));
            else
                digits[i] = 0;
        }
        return digits;
    }
}
