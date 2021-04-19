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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.StringTokenizer;

/**
 *
 * @author zinal
 */
public class NamesBean {

    private static final org.slf4j.Logger LOG
            = org.slf4j.LoggerFactory.getLogger(NamesBean.class);

    private String name;
    private byte[] sortKey;

    public NamesBean() {
    }

    public NamesBean(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }

    public byte[] getSortKey(Charset cset) {
        if (sortKey==null)
            sortKey = name.getBytes(cset);
        return sortKey;
    }

    public static final List<NamesBean> readSimple(String fname)
            throws Exception {
        if (fname.startsWith("~/"))
            fname = System.getProperty("user.home") + fname.substring(1);
        try (FileInputStream fis = new FileInputStream(fname)) {
            return readSimple(fis);
        }
    }

    public static final List<NamesBean> readSimple(InputStream is)
            throws Exception {
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final StringTokenizer stok = new StringTokenizer();
        final Map<String, NamesBean> retval = new HashMap<>();
        String line;
        while ((line=br.readLine())!=null) {
            stok.reset(line);
            String[] items = stok.getTokenArray();
            if (items.length < 2)
                continue;
            String value = StrUtils.unquote(items[1]);
            if (value.length()==0)
                continue;
            NamesBean bean = new NamesBean(value);
            bean = retval.put(bean.getName(), bean);
            if (bean!=null) {
                LOG.warn("Duplicate input value [{}]", bean.getName());
            }
        }
        return new ArrayList<>(retval.values());
    }

}
