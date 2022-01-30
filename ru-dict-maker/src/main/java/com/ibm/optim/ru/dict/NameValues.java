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

/**
 * Pseudo-bean for a 3-part name
 * @author mzinal
 */
public class NameValues {

    public final String first;
    public final String last;
    public final String middle;
    public final String full;

    public NameValues(String first, String last, String middle) {
        this.first = first;
        this.last = last;
        this.middle = middle;
        this.full = last + " " + first +
                ((middle==null || middle.length()==0) ? ""
                : " " + middle);
    }
    
    public String getAbbrev() {
        if (first.length() > 0) {
            if (middle.length() > 0) {
                return last + " " + first.substring(0, 1) + "." 
                        + middle.substring(0, 1) + ".";
            } else {
                return last + " " + first.substring(0, 1) + ".";
            }
        } else {
            if (middle.length() > 0) {
                return last + " " + middle.substring(0, 1) + ".";
            } else {
                return last;
            }
        }
    }

}
