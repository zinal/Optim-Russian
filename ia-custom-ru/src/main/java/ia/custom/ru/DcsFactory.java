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

import java.io.File;
import java.util.HashMap;

/**
 * Коллекция загруженных справочников, с целью кеширования.
 * @author zinal
 */
public class DcsFactory {
    
    private static final Object GUARD = new Object();
    private static String BASE_PATH = null;

    private static final HashMap<String, DcsDict> DICTS = new HashMap<>();

    /**
     * Получить путь к каталогу хранения файлов со справочниками.
     * @return Путь к каталогу со справочниками.
     */
    public static String getBasePath() {
        synchronized(GUARD) {
            if (BASE_PATH==null || BASE_PATH.length()==0)
                BASE_PATH = computeBasePath();
            return BASE_PATH;
        }
    }

    /**
     * Определить путь к каталогу хранения файлов со справочниками.
     * @return Путь к каталогу со справочниками.
     */
    private static String computeBasePath() {
        String basePath = System.getenv("OPTIM_DCS_DICT");
        if (basePath == null || basePath.length()==0) {
            if ( "\\".equals(File.pathSeparator) ) {
                basePath = "/opt/IBM/Masking/DCS/dict";
            } else {
                basePath = "C:\\IBM\\Masking\\DCS\\dict";
            }
        }
        return basePath;
    }

    public static void setBasePath(String basePath) {
        synchronized(GUARD) {
            BASE_PATH = basePath;
        }
    }

    public static void setBasePath(File basePath) {
        setBasePath(basePath==null ? (String)null : basePath.getAbsolutePath());
    }

    /**
     * Получить справочник с указанным именем
     * @param dictName Имя справочника
     * @return Объект справочника
     */
    public static DcsDict dictionary(String dictName) {
        dictName = (dictName==null) ? "" :
                dictName.replaceAll("\\s{2,}", " ").trim();
        if (dictName.length()==0)
            dictName = "default";
        DcsDict d;
        synchronized(DICTS) {
            d = DICTS.get(dictName);
            if (d==null) {
                d = new DcsDict(new File(getBasePath(), dictName + ".txt"));
                DICTS.put(dictName, d);
            }
        }
        return d;
    }
    
}
