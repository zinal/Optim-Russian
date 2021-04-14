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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zinal
 */
public class NamesTool {
    
    private DcsDict input = null;
    private final List<String> pending = new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            new NamesTool() . run(args[0], args[1]);
        } catch(Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    private void run(String inFile, String outFile) throws Exception {
        pending.clear();
        input = new DcsDict(new File(inFile));
        int sizeBefore = input.getEntries().size();
        for (String value : input.getEntries()) {
            handleSuffix(value, "ов", "ова");
            handleSuffix(value, "ев", "ева");
            handleSuffix(value, "ин", "ина");
            handleSuffix(value, "ый", "ая");
            handleSuffix(value, "ий", "ая");
        }
        for (String value : pending) {
            input.add(value);
        }
        int sizeAfter = input.getEntries().size();
        if (sizeBefore < sizeAfter) {
            System.out.println("*** Added values: " + (sizeAfter - sizeBefore));
        }
        input.save(new File(outFile));
    }
    
    private void handleSuffix(String value, String male, String female) {
        String candidate = null;
        if (value.length() > male.length() && value.endsWith(male)) {
            candidate = value.substring(0, value.length() - male.length()) + female;
        } else if (value.length() > female.length() && value.endsWith(female)) {
            candidate = value.substring(0, value.length() - female.length()) + male;
        }
        if (candidate != null) {
            if (!input.containsDirect(candidate))
                pending.add(candidate);
        }
    }
    
}
