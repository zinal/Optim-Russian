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

import java.util.HashSet;

/**
 *
 * @author zinal
 */
public abstract class AbstractGenerator implements ValueGenerator {
    
    public static final String[] NUMS = {"0","1","2","3","4","5","6","7","8","9"};

    private final HashSet<String> values;
    
    public AbstractGenerator(boolean uniq) {
        this.values = uniq ? new HashSet<>() : null;
    }

    @Override
    public boolean allUnique() {
        return (values != null);
    }

    @Override
    public boolean addExisting(String value) {
        if (value==null)
            return false;
        value = value.trim();
        if (value.length()==0)
            return false;
        if (values!=null)
            return values.add(value);
        return true;
    }

    protected abstract String nextRandom();

    @Override
    public String nextValue() {
        String v = null;
        while (v==null) {
            v = nextRandom();
            if (! addExisting(v))
                v = null;
        }
        return v;
    }
    
}
