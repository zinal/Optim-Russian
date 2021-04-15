/*
 *  (c) Copyright IBM Corp. 2021 All rights reserved.
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

import ia.custom.ru.SNILS;
import ia.custom.ru.DcsUtil;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestSNILS {

    @Test
    public void test() {
        String inExpected = "11223344595";
        int[] inArray = new int[] {
            1, 1, 2, 2, 3, 3, 4, 4, 5, 9, 5
        };
        // Проверка строкового варианта
        String in = "112-233-445 95";
        String norm = SNILS.normalize(in);
        Assert.assertEquals(inExpected, norm);
        SNILS snils = new SNILS();
        int[] digits = snils.stringToDigits(norm);
        Assert.assertEquals(norm.length(), digits.length);
        Assert.assertArrayEquals(inArray, digits);
        int[] ctl = snils.getControl(digits);
        Assert.assertEquals(digits[digits.length - 2], ctl[0]);
        Assert.assertEquals(digits[digits.length - 1], ctl[1]);
        // Проверка числового варианта
        Long inLong = 11223344595L;
        norm = DcsUtil.extractDigits(inLong);
        Assert.assertEquals(inExpected, norm);
        BigDecimal inDec = new BigDecimal("11223344595.0");
        norm = DcsUtil.extractDigits(inDec);
        Assert.assertEquals(inExpected, norm);
    }

}
