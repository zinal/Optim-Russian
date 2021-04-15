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
package ia.custom.ru;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Вспомогательные алгоритмы.
 * @author zinal
 */
public class DcsUtil {

    /**
     * Переводим в строку и удаляем все нечисловые символы
     * @param value Обрабатываемое значение.
     * @return Строка, содержащая только цифры из исходного значения.
     */
    public static String extractDigits(Object value) {
        if (value==null)
            return "";
        if (value instanceof Number) {
            String str = value.toString();
            if (str.endsWith(".0"))
                return str.substring(0, str.length()-2);
            return str.replaceAll("[^\\d]", "");
        }
        return value.toString().trim().replaceAll("[^\\d]", "");
    }

    public static void logException(Exception ex) {
        ex.printStackTrace(System.out);
        try {
            File f = new File(new File(System.getProperty("java.io.tmpdir")),
               "ia-DcsRus_"
                    + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS").format(new Date())
                    + ".txt");
            PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(f, true),
                                StandardCharsets.UTF_8)));
            try {
                ex.printStackTrace(out);
                out.println();
                out.println();
            } finally {
                out.close();
            }
        } catch(Exception skip) {}
    }

}
