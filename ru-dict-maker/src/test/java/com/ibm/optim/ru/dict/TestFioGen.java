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
package com.ibm.optim.ru.dict;

import java.util.Properties;
import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestFioGen {
    
    @Test
    public void testFioGen() throws Exception {
        Properties props = new Properties();
        props.setProperty(PropNames.PROP_NAMES_F, "../ru-dict-data/names_f.txt");
        props.setProperty(PropNames.PROP_NAMES_M, "../ru-dict-data/names_m.txt");
        props.setProperty(PropNames.PROP_FAM_F, "../ru-dict-data/fam_f.txt");
        props.setProperty(PropNames.PROP_FAM_M, "../ru-dict-data/fam_m.txt");
        NamesSource src = new NamesSourceBuilder().loadNames(props);
        src.setAntiDupProtection(true);
        src.setReorder(true);
        NameValues v;
        v = src.nextMale();
        System.out.println("fio-M " + v.full);
        v = src.nextFemale();
        System.out.println("fio-F " + v.full);
        v = src.nextMale();
        System.out.println("fio-M " + v.full);
        v = src.nextMale();
        System.out.println("fio-M " + v.full);
        v = src.nextFemale();
        System.out.println("fio-F " + v.full);
        v = src.nextFemale();
        System.out.println("fio-F " + v.full);
    }
    
}
