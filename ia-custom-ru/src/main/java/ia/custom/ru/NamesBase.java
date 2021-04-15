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
package ia.custom.ru;

/**
 * Доступ к справочникам имён, фамилий и отчеств.
 * @author zinal
 */
public abstract class NamesBase {
    
    private transient DcsDict namesFirst = null;
    private transient DcsDict namesMiddle = null;
    private transient DcsDict namesLast = null;
    private transient DcsDict namesItems = null;

    public final DcsDict getNamesFirst() {
        if (namesFirst == null)
            namesFirst = DcsFactory.dictionary("Names_First");
        return namesFirst;
    }

    public final DcsDict getNamesMiddle() {
        if (namesMiddle == null)
            namesMiddle = DcsFactory.dictionary("Names_Middle");
        return namesMiddle;
    }

    public final DcsDict getNamesLast() {
        if (namesLast == null)
            namesLast = DcsFactory.dictionary("Names_Last");
        return namesLast;
    }

    public final DcsDict getNamesItems() {
        if (namesItems == null)
            namesItems = DcsFactory.dictionary("Names_Items");
        return namesItems;
    }

    /**
     * Нормализация и деление имени на части
     * @param value Входное значение
     * @return Компоненты имени
     */
    public String[] extract(Object value) {
        return DcsDict.normalize(value.toString().replace('-', ' ')).split(" ");
    }

}
