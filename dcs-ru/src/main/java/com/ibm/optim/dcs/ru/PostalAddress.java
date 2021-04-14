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
package com.ibm.optim.dcs.ru;

import com.ibm.infosphere.classification.ValueBasedClassifier;

/**
 * Почтовый адрес для Российской Федерации.
 * @author zinal
 */
public class PostalAddress implements ValueBasedClassifier {

    private DcsDict cities = null;
    private DcsDict regions = null;
    private DcsDict mapObjects = null;
    private DcsDict distObjects = null;
    private DcsDict cityObjects = null;
    private DcsDict bldObjects = null;

    @Override
    public boolean matchValue(Object value) {
        if (value==null || !(value instanceof String))
            return false;
        String v = value.toString().toLowerCase();
        int counter = 0;
        boolean isCityName = false;
        for (String t : v.split("\\s")) {
            t = DcsDict.normalize(t);
            if (t.length()==0)
                continue;
            if (getMapObjects().containsDirect(t)
                    || getDistObjects().containsDirect(t)
                    || getCityObjects().containsDirect(t)
                    || getBldObjects().containsDirect(t)
                    || getRegions().containsDirect(t)) {
                counter++;
                continue;
            }
            if (!isCityName) {
                if (getCities().contains(t)) {
                    counter ++;
                    isCityName = true;
                }
            }
        }
        return (counter >= 2);
    }

    public DcsDict getCities() {
        if (cities==null)
            cities = DcsDict.dictionary("Addr_cities");
        return cities;
    }

    public DcsDict getRegions() {
        if (regions==null)
            regions = DcsDict.dictionary("Addr_regions");
        return regions;
    }

    public DcsDict getMapObjects() {
        if (mapObjects==null)
            mapObjects = DcsDict.dictionary("Addr_mapObjects");
        return mapObjects;
    }

    public DcsDict getDistObjects() {
        if (distObjects==null)
            distObjects = DcsDict.dictionary("Addr_distObjects");
        return distObjects;
    }

    public DcsDict getCityObjects() {
        if (cityObjects==null)
            cityObjects = DcsDict.dictionary("Addr_cityObjects");
        return cityObjects;
    }

    public DcsDict getBldObjects() {
        if (bldObjects==null)
            bldObjects = DcsDict.dictionary("Addr_bldObjects");
        return bldObjects;
    }

}
