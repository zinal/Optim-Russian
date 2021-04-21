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
package com.ibm.optim.ru.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author zinal
 */
public class NamesSourceBuilder extends PropNames {

    public NamesSource loadNames(Properties props) throws Exception {
        String fNamesMale    = props.getProperty(PROP_NAMES_M);
        String fNamesFemale  = props.getProperty(PROP_NAMES_F);
        String fLastMale     = props.getProperty(PROP_FAM_M);
        String fLastFemale   = props.getProperty(PROP_FAM_F);
        String salt          = props.getProperty(PROP_SALT);
        
        if (fNamesMale==null || fNamesFemale==null ||
                fLastMale==null || fLastFemale==null) {
            throw new IllegalArgumentException("Missing input filenames, "
                    + "check job configuration");
        }
        if (salt==null)
            salt = " ";
        
        List<NamesMaleBean> firstMale = NamesMaleBean.readExtended(fNamesMale);
        List<NamesBean> firstFemale = NamesMaleBean.readSimple(fNamesFemale);
        List<NamesBean> lastMale = NamesBean.readSimple(fLastMale);
        List<NamesBean> lastFemale = NamesBean.readSimple(fLastFemale);

        final NamesData dataMale = new NamesData();
        final NamesData dataFemale = new NamesData();
        dataMale.last = lastMale;
        dataMale.first = new ArrayList<>();
        dataMale.middle = new ArrayList<>();
        dataFemale.last = lastFemale;
        dataFemale.first = firstFemale;
        dataFemale.middle = new ArrayList<>();

        for (NamesMaleBean nm : firstMale) {
            dataMale.first.add(new NamesBean(nm.getName()));
            dataMale.middle.add(new NamesBean(nm.getMidMale()));
            dataFemale.middle.add(new NamesBean(nm.getMidFemale()));
        }

        return new NamesSource(salt, dataMale, dataFemale);
    }

}
