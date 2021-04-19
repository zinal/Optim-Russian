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
package com.ibm.optim.ru.dict;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import com.ibm.optim.ru.supp.PureJavaCrc32;

/**
 * Compare two NamesBean values, according to their salted hashes.
 * @author mzinal
 */
public class NamesComparator implements Comparator<NamesBean> {

    private static final Charset CSET = StandardCharsets.UTF_8;

    private final byte[] sortSalt1;
    private final byte[] sortSalt2;
    private final PureJavaCrc32 crc = new PureJavaCrc32();

    public NamesComparator(String sortSalt1) {
        this.sortSalt1 = sortSalt1.getBytes(CSET);
        this.sortSalt2 = null;
    }

    public NamesComparator(byte[] sortSalt1) {
        this.sortSalt1 = sortSalt1;
        this.sortSalt2 = null;
    }

    public NamesComparator(String sortSalt1, String sortSalt2) {
        this.sortSalt1 = sortSalt1.getBytes(CSET);
        this.sortSalt2 = sortSalt2.getBytes(CSET);
    }

    public NamesComparator(byte[] sortSalt1, byte[] sortSalt2) {
        this.sortSalt1 = sortSalt1;
        this.sortSalt2 = sortSalt2;
    }

    @Override
    public int compare(NamesBean o1, NamesBean o2) {
        crc.reset();
        crc.update(o1.getSortKey(CSET));
        crc.update(sortSalt1);
        if (sortSalt2!=null)
            crc.update(sortSalt2);
        long v1 = crc.getValue();
        crc.reset();
        crc.update(o2.getSortKey(CSET));
        crc.update(sortSalt1);
        if (sortSalt2!=null)
            crc.update(sortSalt2);
        long v2 = crc.getValue();
        if (v1 == v2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
        return (v1 < v2) ? -1 : 1;
    }

}
