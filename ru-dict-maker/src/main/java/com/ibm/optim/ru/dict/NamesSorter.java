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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mzinal
 */
public class NamesSorter {

    private final NamesComparator cmp;

    public NamesSorter(String salt) {
        this.cmp = new NamesComparator(salt);
    }

    public NamesSorter(byte[] salt) {
        this.cmp = new NamesComparator(salt);
    }

    /**
     * Sort the lists in NamesData, according to the salted hash value,
     * and return the new sorted copy.
     * We assume that there are no duplicates in the input
     * (this is checked on load, see readSimple and readExtended methods).
     * @param nd Input lists
     * @return Output - sorted lists
     */
    public NamesData sort(NamesData nd) {
        final NamesData ret = new NamesData();
        ret.first = sort(nd.first);
        ret.last = sort(nd.last);
        ret.middle = sort(nd.middle);
        return ret;
    }

    /**
     * Partial sort over the NamesData (leaving last names intact).
     * @param nd Input lists
     * @return Output - sorted lists
     */
    public NamesData sortPart(NamesData nd) {
        final NamesData ret = new NamesData();
        ret.first = sort(nd.first);
        ret.last = nd.last;
        ret.middle = sort(nd.middle);
        return ret;
    }

    /**
     * Sort the list of values according to salted hash values.
     * @param src Input list with no duplicate values
     * @return New copy, sorted appropriately.
     */
    public List<NamesBean> sort(List<NamesBean> src) {
        final List<NamesBean> ret = new ArrayList<>(src);
        Collections.sort(ret, cmp);
        return ret;
    }

}
