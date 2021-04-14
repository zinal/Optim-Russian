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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestPassport {

    @Test
    public void test() {
        final PassportDomestic pd = new PassportDomestic();
        final PassportForeign pf = new PassportForeign();

        String dom1 = "28 02 324411";
        boolean dom1pd = pd.matchValue(dom1);
        Assert.assertEquals(true, dom1pd);
        boolean dom1pf = pf.matchValue(dom1);
        Assert.assertEquals(false, dom1pf);

        String dom2 = "28 02 324411";
        boolean dom2pd = pd.matchValue(dom2);
        Assert.assertEquals(true, dom2pd);
        boolean dom2pf = pf.matchValue(dom2);
        Assert.assertEquals(false, dom2pf);

        String for1 = "69 3244112";
        boolean for1pd = pd.matchValue(for1);
        Assert.assertEquals(false, for1pd);
        boolean for1pf = pf.matchValue(for1);
        Assert.assertEquals(true, for1pf);

        String for2 = "34 1234567";
        boolean for2pd = pd.matchValue(for2);
        Assert.assertEquals(false, for2pd);
        boolean for2pf = pf.matchValue(for2);
        Assert.assertEquals(true, for2pf);
    }

}
