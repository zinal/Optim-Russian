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
public class SnilsGen extends AbstractGenerator {

    private final RandomStringGenerator gen 
            = new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .build();

    public SnilsGen() {
        super(true);
    }
    
    public SnilsGen(boolean uniq) {
        super(uniq);
    }

    @Override
    protected String nextRandom() {
        String main = gen.generate(9);
        int[] vals = StrUtils.stringToDigits(main);
        int control = getControl(vals);
        return main.substring(0, 3) + "-" 
                + main.substring(3, 6) + "-"
                + main.substring(6, 9) + " "
                + NUMS[control/10] + NUMS[control%10];
    }

    public static final int[] SNILS = { 9,8,7,6,5,4,3,2,1 };

    public int getControl(int[] digits) {
        int control = 0;
        for (int i=0; i<SNILS.length; ++i) {
            control += digits[i] * SNILS[i];
        }
        if (control > 101) {
            control = control % 101;
        }
        if (control == 100 || control == 101)
            control = 0;
        return control;
    }

}
