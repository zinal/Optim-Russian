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
package com.ibm.optim.dcs.ru;

/**
 * Доступ к справочникам имён, фамилий и отчеств.
 * @author zinal
 */
public abstract class NamesBase {
    
    private DcsDict namesFirst = null;
    private DcsDict namesMiddle = null;
    private DcsDict namesLast = null;

    public final DcsDict getNamesFirst() {
        if (namesFirst == null)
            namesFirst = DcsDict.dictionary("Names_First");
        return namesFirst;
    }

    public final DcsDict getNamesMiddle() {
        if (namesMiddle == null)
            namesMiddle = DcsDict.dictionary("Names_Middle");
        return namesMiddle;
    }

    public final DcsDict getNamesLast() {
        if (namesLast == null)
            namesLast = DcsDict.dictionary("Names_Last");
        return namesLast;
    }

}
