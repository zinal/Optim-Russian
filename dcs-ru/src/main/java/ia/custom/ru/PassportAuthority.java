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
package ia.custom.ru;

import com.ibm.infosphere.classification.ValueBasedClassifier;

public class PassportAuthority implements ValueBasedClassifier {

    private transient DcsDict keyWords = null;
    private transient DcsDict words = null;
    private transient DcsDict regions = null;

    @Override
    public boolean matchValue(Object value) {
        String strValue = String.valueOf(value).trim().toLowerCase();
        strValue = strValue.replace(",", " ");
        strValue = strValue.replace(".", " ");
        int counter = 0;
        for (String token : strValue.split(" ")) {
            if (token.equals("")) {
                continue;
            }
            if (getKeyWords().contains(token)) {
                counter += 3;
                continue;
            }
            if (getWords().contains(token)) {
                counter++;
                continue;
            }
            if (getRegions().contains(token)) {
                counter += 2;
                continue;
            }
        }
        return (counter >= 7);
    }

    public DcsDict getKeyWords() {
        if (keyWords==null)
            keyWords = DcsFactory.dictionary("PA_Keys");
        return keyWords;
    }

    public DcsDict getWords() {
        if (words==null)
            words = DcsFactory.dictionary("PA_Words");
        return words;
    }

    public DcsDict getRegions() {
        if (regions==null)
            regions = DcsFactory.dictionary("PA_Regions");
        return regions;
    }

}
