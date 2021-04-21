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

import com.ibm.optim.ru.supp.StrUtils;
import org.apache.commons.text.RandomStringGenerator;

/**
 *
 * @author zinal
 */
public class OgrnGen extends AbstractGenerator {

    public static enum Type {
        Physical,
        Government,
        Commercial
    };
    
    private final Type type;
    
    private final RandomStringGenerator gen1 
            = new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .build();

    public OgrnGen() {
        this(true, Type.Physical);
    }
    
    public OgrnGen(boolean uniq, Type type) {
        super(uniq);
        this.type = type;
    }

    @Override
    protected String nextRandom() {
        switch (type) {
            case Physical:
                return randomPhysical();
            case Government:
                return randomGovernment();
            case Commercial:
                return randomCommercial();
        }
        throw new IllegalStateException();
    }

    public String randomPhysical() {
        String main = "3" + gen1.generate(13);
        long longValue = Long.parseLong(main);
        int control = (int) (longValue % 13) % 10;
        return main + NUMS[control];
    }

    public String randomGovernment() {
        String main = "2" + gen1.generate(11);
        long longValue = Long.parseLong(main);
        int control = (int) (longValue % 11) % 10;
        return main + NUMS[control];
    }

    public String randomCommercial() {
        String main = "1" + gen1.generate(11);
        long longValue = Long.parseLong(main);
        int control = (int) (longValue % 11) % 10;
        return main + NUMS[control];
    }

}
