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

import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestNamesAbbr {
    
    @Test
    public void test() {
        final File dictBase = new File(new File("").getAbsoluteFile(), "dict");
        DcsFactory.setBasePath(dictBase);
        
        final NamesAbbr na = new NamesAbbr();
        String in;
        boolean out;
        
        in = "Пагосян Гагик Спартакович-оглы";
        out = na.matchValue(in);
        Assert.assertEquals(in, false, out);
        
        in = "Иван Иванов";
        out = na.matchValue(in);
        Assert.assertEquals(in, false, out);
        
        in = "Петров П. П.";
        out = na.matchValue(in);
        Assert.assertEquals(in, true, out);

        in = "П. Петров";
        out = na.matchValue(in);
        Assert.assertEquals(in, true, out);

        in = "Петров";
        out = na.matchValue(in);
        Assert.assertEquals(in, false, out);

        in = "Г. С.-оглы Пагосян";
        out = na.matchValue(in);
        Assert.assertEquals(in, true, out);

        in = "Служил Гаврила программистом";
        out = na.matchValue(in);
        Assert.assertEquals(in, false, out);
    }
    
}
