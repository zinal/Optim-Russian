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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Простой справочник значений, загружаемый из текстового файла.
 * @author zinal
 */
public class DcsDict {

    private static final HashMap<String, DcsDict> dicts = new HashMap<>();
    private final HashSet<String> values = new HashSet<>();

    /**
     * Поиск значения в справочнике
     * @param value Значение (до нормализации)
     * @return true, если нормализованный вариант найден в справочнике, иначе false
     */
    public boolean contains(String value) {
        return values.contains(normalize(value));
    }

    /**
     * Поиск значения в справочнике
     * @param value Значение (ПОСЛЕ нормализации)
     * @return true, если нормализованный вариант найден в справочнике, иначе false
     */
    public boolean containsDirect(String value) {
        return values.contains(value);
    }

    /**
     * Нормализация значения
     * @param value Значение до нормализации
     * @return Нормализованное значение: 
     *           удалены пробелы по краям, 
     *           множественные пробелы на одинарный, 
     *           в нижнем регистре
     */
    public static String normalize(String value) {
        value = (value==null) ? "" : 
                value.replaceAll("\\s{2,}", " ").trim().toLowerCase();
        return value;
    }
    
    /**
     * Загрузка справочника из файла
     * @param dictName Имя справочника
     */
    private DcsDict(String dictName) {
        final Path f = new File(getBasePath(), dictName + ".txt").toPath();
        try {
            Files.lines(f, StandardCharsets.UTF_8).forEach(
                    s -> values.add(normalize(s))
            );
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
        values.remove("");
    }
    
    /**
     * Получить путь к каталогу хранения файлов со справочниками.
     * @return Путь к каталогу со справочниками.
     */
    private static String getBasePath() {
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
    
    /**
     * Получить справочник с указанным именем
     * @param dictName Имя справочника
     * @return Объект справочника
     */
    public static DcsDict dictionary(String dictName) {
        dictName = normalize(dictName);
        if (dictName.length()==0)
            dictName = "default";
        DcsDict d;
        synchronized(dicts) {
            d = dicts.get(dictName);
            if (d!=null)
                return d;
        }
        d = new DcsDict(dictName);
        synchronized(dicts) {
            dicts.put(dictName, d);
        }
        return d;
    }

}
