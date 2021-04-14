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
package com.ibm.optim.dcs.ru;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestTaxPayerId {
    
    @Test
    public void test12() {
        int[] inArray = new int[] {
            6, 9, 0, 5, 0, 4, 2, 3, 4, 9, 3, 2
        };
        String in = "\t69 050 423 4932 ";
        String norm = TaxPayerId.normalize(in);
        Assert.assertEquals("690504234932", norm);
        TaxPayerId tpi = new TaxPayerId();
        int[] digits = tpi.stringToDigits(norm);
        Assert.assertEquals(norm.length(), digits.length);
        Assert.assertArrayEquals(inArray, digits);
        int c11 = TaxPayerId.getChecksum(digits, TaxPayerId.N11);
        int c12 = TaxPayerId.getChecksum(digits, TaxPayerId.N12);
        Assert.assertEquals(digits[digits.length - 2], c11);
        Assert.assertEquals(digits[digits.length - 1], c12);
    }
    
    @Test
    public void test10() {
        int[] inArray = new int[] {
            7, 7, 0, 5, 0, 4, 1, 8, 6, 6
        };
        String in = " 77 050 418 66\t";
        String norm = TaxPayerId.normalize(in);
        Assert.assertEquals("7705041866", norm);
        TaxPayerId tpi = new TaxPayerId();
        int[] digits = tpi.stringToDigits(norm);
        Assert.assertEquals(norm.length(), digits.length);
        Assert.assertArrayEquals(inArray, digits);
        int c10 = TaxPayerId.getChecksum(digits, TaxPayerId.N10);
        Assert.assertEquals(digits[digits.length - 1], c10);
    }
    
}
