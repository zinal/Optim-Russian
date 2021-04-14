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
public class TestOGRN {

    @Test
    public void test15() {
        String in1 = "319631300041247";
        boolean res1 = new OGRN().matchValue(in1);
        Assert.assertEquals(true, res1);
        String in2 = "319631041247";
        boolean res2 = new OGRN().matchValue(in2);
        Assert.assertEquals(false, res2);
        String in3 = "319631300041241";
        boolean res3 = new TaxPayerId().matchValue(in3);
        Assert.assertEquals(false, res3);
    }

    @Test
    public void test13() {
        String in1 = "1027739004600";
        boolean res1 = new OGRN().matchValue(in1);
        Assert.assertEquals(true, res1);
        String in2 = "10277390046";
        boolean res2 = new OGRN().matchValue(in2);
        Assert.assertEquals(false, res2);
        String in3 = "1027739004601";
        boolean res3 = new TaxPayerId().matchValue(in3);
        Assert.assertEquals(false, res3);
    }

}
