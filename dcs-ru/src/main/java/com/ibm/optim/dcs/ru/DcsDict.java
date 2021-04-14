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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Простой справочник значений, загружаемый из текстового файла.
 * @author zinal
 */
public class DcsDict {

    private static final Object GUARD = new Object();
    private static String BASE_PATH = null;

    private static final HashMap<String, DcsDict> DICTS = new HashMap<>();
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
     * Доступ к перечню элементов.
     * @return Перечень элементов
     */
    public Collection<String> getEntries() {
        return Collections.unmodifiableCollection(values);
    }
    
    /**
     * Добавить значение в справочник.
     * @param value Добавляемое значение
     */
    public void add(String value) {
        value = normalize(value);
        if (value.length() > 0)
            values.add(value);
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
                value.trim().replaceAll("\\s{2,}", " ")
                     .toLowerCase()
                     .replace('ё', 'е');
        return value;
    }

    /**
     * Загрузка справочника из файла
     * @param dictName Имя справочника
     */
    private DcsDict(String dictName) {
        this(new File(getBasePath(), dictName + ".txt"));
    }

    /**
     * Загрузка справочника из файла
     * @param file Файл со справочником
     */
    public DcsDict(File file) {
        final Path f = file.toPath();
        try {
            Files.lines(f, StandardCharsets.UTF_8).forEach(
                s -> {
                    String n = normalize(s);
                    if (n.length() > 0)
                        values.add(n);
                }
            );
        } catch(Exception ex) {
            DcsUtil.logException(ex);
        }
        values.remove("");
    }

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
                d = new DcsDict(dictName);
                DICTS.put(dictName, d);
            }
        }
        return d;
    }
    
    public void save(File file) throws IOException {
        PrintWriter out = new PrintWriter(
            new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, false),
                            StandardCharsets.UTF_8)));
        try {
            for (String v : values) {
                out.println(v);
            }
        } finally {
            out.close();
        }
    }

}
