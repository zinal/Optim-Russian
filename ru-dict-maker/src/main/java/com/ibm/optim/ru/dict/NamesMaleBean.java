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

import com.ibm.optim.ru.supp.StrUtils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.WordUtils;

/**
 *
 * @author zinal
 */
public class NamesMaleBean extends NamesBean {

    private String midMale;
    private String midFemale;

    public NamesMaleBean() {
    }

    public NamesMaleBean(String name) {
        super(name);
    }

    public NamesMaleBean(String name, String midMale, String midFemale) {
        super(name);
        this.midMale = WordUtils.capitalizeFully(midMale);
        this.midFemale = WordUtils.capitalizeFully(midFemale);
    }

    public String getMidMale() {
        return midMale;
    }

    public void setMidMale(String midMale) {
        this.midMale = WordUtils.capitalizeFully(midMale);
    }

    public String getMidFemale() {
        return midFemale;
    }

    public void setMidFemale(String midFemale) {
        this.midFemale = WordUtils.capitalizeFully(midFemale);
    }


    public static final List<NamesMaleBean> readExtended(String fname)
            throws Exception {
        if (fname.startsWith("~/"))
            fname = System.getProperty("user.home") + fname.substring(1);
        try (FileInputStream fis = new FileInputStream(fname)) {
            return readExtended(fis);
        }
    }

    public static final List<NamesMaleBean> readExtended(InputStream is)
            throws Exception {
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final StringTokenizer stok = new StringTokenizer();
        final Map<String, NamesMaleBean> retval = new HashMap<>();
        String line;
        while ((line=br.readLine())!=null) {
            stok.reset(line);
            String[] items = stok.getTokenArray();
            if (items.length < 4)
                continue;
            String name = StrUtils.unquote(items[1]);
            if (name.length()==0)
                continue;
            String midMale = StrUtils.unquote(items[2]);
            String midFemale = StrUtils.unquote(items[3]);
            NamesMaleBean bean = new NamesMaleBean(name, midMale, midFemale);
            retval.put(bean.getName(), bean);
        }
        return new ArrayList<>(retval.values());
    }
}
