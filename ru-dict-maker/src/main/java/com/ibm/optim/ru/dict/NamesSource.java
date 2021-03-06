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

import java.util.HashSet;

/**
 * Random name generator for Russian names based on dictionary data.
 * @author mzinal
 */
public class NamesSource {

    private final String salt;
    private int saltCounter;

    private NamesData dataMale;
    private NamesData dataFemale;

    private final Positions posMale = new Positions();
    private final Positions posFemale = new Positions();

    private boolean antiDupProtection = false;
    private boolean reorder = false;
    private final HashSet<String> knownNames = new HashSet<>();
    private int duplicateCount = 0;

    public NamesSource(String salt, NamesData dataMale,
            NamesData dataFemale) throws Exception {
        if (salt==null || salt.length()==0)
            salt = " ";
        final NamesSorter ns = new NamesSorter(salt);
        this.dataMale = ns.sort(dataMale);
        this.dataFemale = ns.sort(dataFemale);
        this.salt = salt;
        this.saltCounter = 0;
    }

    public NamesData getDataMale() {
        return dataMale;
    }

    public NamesData getDataFemale() {
        return dataFemale;
    }

    public boolean isAntiDupProtection() {
        return antiDupProtection;
    }

    public void setAntiDupProtection(boolean antiDupProtection) {
        this.antiDupProtection = antiDupProtection;
    }

    public boolean isReorder() {
        return reorder;
    }

    public void setReorder(boolean reorder) {
        this.reorder = reorder;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

    public boolean addKnown(String value) {
        if (value==null)
            value = "";
        else
            value = value.trim();
        if (value.length()==0)
            return false;
        if (antiDupProtection) {
            return knownNames.add(value.toLowerCase());
        }
        return true;
    }

    public NameValues nextMale() throws Exception {
        NameValues nv = next(dataMale, posMale);
        if (reorder) {
            String s = salt + "." + Integer.toHexString(saltCounter++);
            dataMale = new NamesSorter(s).sortPart(dataMale);
        }
        return nv;
    }

    public NameValues nextFemale() throws Exception {
        NameValues nv = next(dataFemale, posFemale);
        if (reorder) {
            String s = salt + "." + Integer.toHexString(saltCounter++);
            dataFemale = new NamesSorter(s).sortPart(dataFemale);
        }
        return nv;
    }

    private NameValues next(NamesData data, Positions pos) throws Exception {
        NameValues val = null;
        while (val == null) {
            val = new NameValues(
                data.first.get(pos.first).getName(),
                data.last.get(pos.last).getName(),
                data.middle.get(pos.middle).getName()
            );
            pos.last += 1;
            if (pos.last >= data.last.size()) {
                pos.last = 0;
                pos.first += 1;
                if (pos.first >= data.first.size()) {
                    pos.first = 0;
                    pos.middle += 1;
                    if (pos.middle >= data.middle.size()) {
                        pos.middle = 0;
                        throw new Exception("Indexes overlapped, "
                                + "not enough input dictionary values");
                    }
                }
            }
            if (! addKnown(val.full) ) {
                val = null;
                ++duplicateCount;
            }
        }
        return val;
    }

    public void stop() {
        posFemale.reset();
        posMale.reset();
    }

    public void start() {
        posFemale.reset();
        posMale.reset();
        duplicateCount = 0;
        knownNames.clear();
    }

    static class Positions {
        int first = 0;
        int middle = 0;
        int last = 0;

        void reset() {
            first = 0;
            middle = 0;
            last = 0;
        }
    }

    @Override
    public String toString() {
        return "NamesSourceV2";
    }

}
