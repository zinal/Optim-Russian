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

import java.util.Random;
import org.apache.commons.text.RandomStringGenerator;

/**
 *
 * @author zinal
 */
public class PhoneGenerator extends AbstractGenerator {
    
    private final Random coin = new Random();

    private final RandomStringGenerator gen0
            = new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .build();

    private final RandomStringGenerator gen1 
            = new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .build();

    public PhoneGenerator() {
        super(false);
    }

    @Override
    protected String nextRandom() {
        final StringBuilder sb = new StringBuilder();
        if (coin.nextBoolean()) {
            sb.append("+7");
        } else {
            sb.append("8");
        }
        sb.append("(").append(gen0.generate(3)).append(") ");
        sb.append(gen1.generate(3))
                .append("-").append(gen1.generate(2))
                .append("-").append(gen1.generate(2));
        return sb.toString();
    }
    
}
