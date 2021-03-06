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
package com.ibm.optim.ru.supp;

/**
 *
 * @author zinal
 */
public class StrUtils {

    public static String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length()>1) {
            s = s.substring(1, s.length()-1);
        }
        return s.trim();
    }

    public static int[] stringToDigits(String value) {
        int[] digits = new int[value.length()];
        for (int i=0; i<value.length(); ++i) {
            digits[i] = Character.getNumericValue(value.charAt(i));
        }
        return digits;
    }

}
