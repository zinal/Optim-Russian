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
package iacheck;

import com.ibm.infosphere.classification.ValueBasedClassifier;
import java.util.regex.Pattern;

/**
 * ОРГН для юридических лиц и ИП.
 *
 * @author zinal
 */
public class OGRN implements ValueBasedClassifier {

    private final Pattern pattern = Pattern.compile("\\d{13}|\\d{15}");

    @Override
    public boolean matchValue(Object value) {
        if (value==null)
            return false;
        String strValue = extractDigits(value);
        if (!pattern.matcher(strValue).matches())
            return false; // Не соответствует формату
        long longValue = Long.parseLong(strValue);
        int delimiter = (strValue.length() <= 13) ? 11 : 13;
        int control1 = (int) (longValue % 10L);
        int control2 = (int) ( (longValue/10L) % delimiter ) % 10;
        return (control1 == control2);
    }

    public static String extractDigits(Object value) {
        if (value==null)
            return "";
        final String str = value.toString().trim();
        if (value instanceof Number) {
            if (str.endsWith(".0"))
                return str.substring(0, str.length()-2);
        }
        return str.replaceAll("[^\\d]", "");
    }
}
