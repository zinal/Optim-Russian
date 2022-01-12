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
package com.ibm.optim.ru.gen;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestOrgNameGen {
    
    @Test
    public void testOrgNameGen() throws Exception {
        OrgNameGen gen = new OrgNameGen(new File("../ru-dict-data/flowers.txt"), false);
        final int dictSize = gen.getDictionarySize();
        System.out.println("gov-dict-size: " + dictSize);
        System.out.println("gov-dict-iter: " + gen.getIteration());
        String v = gen.nextValue();
        System.out.println("gov-0 " + v);
        for (int i=0; i<dictSize; ++i)
            v = gen.nextValue();
        System.out.println("gov-1 " + v);
        System.out.println("gov-dict-iter: " + gen.getIteration());
        Assert.assertEquals(1, gen.getIteration());

        gen = new OrgNameGen(new File("../ru-dict-data/flowers.txt"), true);
        Assert.assertEquals(dictSize, gen.getDictionarySize());
        gen.addExisting("ООО Баран");
        gen.addExisting("АО МММ 3");
        gen.addExisting("ООО Ардизия № 5");
        System.out.println("com-dict-iter: " + gen.getIteration());
        Assert.assertEquals(6, gen.getIteration());
        v = gen.nextValue();
        System.out.println("com-0 " + v);
        for (int i=0; i<dictSize; ++i)
            v = gen.nextValue();
        System.out.println("com-1 " + v);
        System.out.println("com-dict-iter: " + gen.getIteration());
        Assert.assertEquals(7, gen.getIteration());
    }
    
}
