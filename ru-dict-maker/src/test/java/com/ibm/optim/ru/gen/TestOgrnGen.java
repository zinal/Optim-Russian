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
package com.ibm.optim.ru.gen;

import org.junit.Test;

/**
 *
 * @author zinal
 */
public class TestOgrnGen {
    
    @Test
    public void testCommercial() {
        final OgrnGen gen = new OgrnGen(true, OgrnGen.Type.Commercial);
        String v;
        v = gen.nextValue();
        System.out.println("orgn-com[0] " + v);
        v = gen.nextValue();
        System.out.println("orgn-com[1] " + v);
        v = gen.nextValue();
        System.out.println("orgn-com[2] " + v);
    }
    
    @Test
    public void testGovernment() {
        final OgrnGen gen = new OgrnGen(true, OgrnGen.Type.Commercial);
        String v;
        v = gen.nextValue();
        System.out.println("orgn-gov[0] " + v);
        v = gen.nextValue();
        System.out.println("orgn-gov[1] " + v);
        v = gen.nextValue();
        System.out.println("orgn-gov[2] " + v);
    }
    
    @Test
    public void testPhysical() {
        final OgrnGen gen = new OgrnGen(true, OgrnGen.Type.Physical);
        String v;
        v = gen.nextValue();
        System.out.println("orgn-phy[0] " + v);
        v = gen.nextValue();
        System.out.println("orgn-phy[1] " + v);
        v = gen.nextValue();
        System.out.println("orgn-phy[2] " + v);
    }
    
}
