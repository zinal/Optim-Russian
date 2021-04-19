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
package com.ibm.optim.ru.supp;

import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author zinal
 */
public class DbUtils {

    public static void close(PreparedStatement ps) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String resolvePath(String path) {
        if (path.startsWith("~/"))
            return System.getProperty("user.home") + path.substring(1);
        return path;
    }

    public static void deleteFiles(String pathname) {
        pathname = resolvePath(pathname);
        final File pn = new File(pathname).getAbsoluteFile();
        final String fullName = pn.getAbsolutePath();
        final String fullStart = fullName + ".";
        File[] victims = pn.getParentFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                final String cur = pathname.getAbsolutePath();
                return (fullName.equals(cur)
                        || cur.startsWith(fullStart));
            }
        });
        if (victims!=null) {
            for (File v : victims)
                v.delete();
        }
    }

    public static String makeH2Url(String pathname) {
        return "jdbc:h2:" + resolvePath(pathname).replaceAll("\\\\", "/");
    }

    public static void createTables(Connection con, String[] cmds)
            throws Exception {
        try (Statement stmt = con.createStatement()) {
            for (String sql : cmds)
                stmt.execute(sql);
        }
    }

}
