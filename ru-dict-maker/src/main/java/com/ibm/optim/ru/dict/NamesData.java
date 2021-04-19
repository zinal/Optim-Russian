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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mzinal
 */
public class NamesData {

    public List<NamesBean> first;
    public List<NamesBean> last;
    public List<NamesBean> middle;

    public NamesData() {
    }

    public NamesData(NamesData nd) {
        this.first = new ArrayList<>(nd.first);
        this.last = new ArrayList<>(nd.last);
        this.middle = new ArrayList<>(nd.middle);
    }

    public NamesData[] split(int n) {
        if (n<2) {
            return new NamesData[] { new NamesData(this) };
        }
        // Compute the number of elements in each bin.
        // Trying to make bin sizes equal.
        // The algorithm is not optimal, but with dictionary sizes
        // less than 1000... it does not matter at all.
        int ntotal = middle.size();
        int[] neach = new int[n];
        Arrays.fill(neach, 1);
        int ndone = n;
        int pos = 0;
        while (ndone < ntotal) {
            neach[pos] += 1;
            ndone += 1;
            pos += 1;
            if (pos >= n)
                pos = 0;
        }
        if (ndone > ntotal)
            throw new RuntimeException("Insufficient amount of middle names");
        // Generate the bins
        NamesData[] ret = new NamesData[n];
        pos = 0;
        for (int i=0; i<n; ++i) {
            ret[i] = new NamesData();
            ret[i].first = new ArrayList<>(this.first);
            ret[i].last = new ArrayList<>(this.last);
            ret[i].middle = new ArrayList<>(neach[i]);
            for (int j=0; j<neach[i]; ++j) {
                ret[i].middle.add(this.middle.get(pos++));
            }
        }
        return ret;
    }

}
