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
public class InnGen extends AbstractGenerator {

    public static enum Type {
        Physical,
        Legal
    };
    
    private final Type type;
    
    private final RandomStringGenerator gen 
            = new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .build();
    
    public InnGen() {
        this(true, Type.Physical);
    }

    public InnGen(boolean uniq, Type type) {
        super(uniq);
        this.type = type;
    }
    
    @Override
    protected String nextRandom() {
        switch (type) {
            case Physical:
                return randomPhysical();
            case Legal:
                return randomLegal();
        }
        throw new IllegalStateException();
    }
    
    public String randomPhysical() {
        // TODO: оптимизация на скорость
        String main = gen.generate(10);
        int[] inn = StrUtils.stringToDigits(main);
        int n11 = getChecksum(inn, N11);
        main = main + NUMS[n11];
        inn = StrUtils.stringToDigits(main);
        int n12 = getChecksum(inn, N12);
        return main + NUMS[n12];
    }

    public String randomLegal() {
        String main = gen.generate(9);
        int[] inn = StrUtils.stringToDigits(main);
        int n10 = getChecksum(inn, N10);
        return main + NUMS[n10];
    }

    //                               0  1  2  3  4  5  6  7  8  9  10
    public static final int[] N10 = {2, 4,10, 3, 5, 9, 4, 6, 8};
    public static final int[] N11 = {7, 2, 4,10, 3, 5, 9, 4, 6, 8};
    public static final int[] N12 = {3, 7, 2, 4,10, 3, 5, 9, 4, 6, 8};

    public static int getChecksum(int[] digits, int[] multipliers) {
        int checksum = 0;
        for (int i = 0; i < multipliers.length; i++) {
            checksum += digits[i] * multipliers[i];
        }
        checksum = checksum % 11;
        if (checksum > 9)
            checksum = 0;
        return checksum;
    }

}
