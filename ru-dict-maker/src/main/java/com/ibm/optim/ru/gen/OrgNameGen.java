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
package com.ibm.optim.ru.gen;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;

/**
 * Генератор наименований организаций
 * @author zinal
 */
public class OrgNameGen implements ValueGenerator {

    private final boolean comm;
    private final List<String> flowers;
    private final Pattern pattern;

    private int position = 0;
    private int iteration = 0;

    public OrgNameGen(File f, boolean comm) throws Exception {
        this.comm = comm;
        this.flowers = new ArrayList<>( loadDict(f) );
        this.pattern = Pattern.compile("^\\d+$");
        reorderFlowers();
    }

    public OrgNameGen(String f, boolean comm) throws Exception {
        this((f==null) ? (File)null : new File(f), comm);
    }

    public int getDictionarySize() {
        return flowers.size();
    }

    public int getIteration() {
        return iteration;
    }

    private static Collection<String> loadDict(File f) throws Exception {
        final Set<String> values = new HashSet<>();
        Files.lines(f.toPath(), StandardCharsets.UTF_8).forEach(
            s -> {
                String n = WordUtils.capitalize(s.trim().toLowerCase());
                if (n.length() > 0)
                    values.add(n);
            }
        );
        if (values.isEmpty())
            throw new Exception("No flower names available");
        return values;
    }

    private void reorderFlowers() {
        final int topKey = flowers.size() * 100;
        final Random rand = new Random();
        final Map<Integer, String> x = new TreeMap<>();
        for (String v : flowers) {
            Integer h = rand.nextInt(topKey);
            while ( x.containsKey(h) )
                h = h + 1;
            x.put(h, v);
        }
        flowers.clear();
        flowers.addAll(x.values());
    }

    @Override
    public String nextValue() {
        String prefix = comm ? "ООО " : "Министерство ";
        String v = prefix + flowers.get(position);
        position += 1;
        if (position >= flowers.size()) {
            position = 0;
            iteration += 1;
            reorderFlowers();
        }
        if (iteration > 0) {
            v = v + " № " + String.valueOf(iteration);
        }
        return v;
    }

    @Override
    public boolean allUnique() {
        return true;
    }

    /**
     * Определяем максимальную предыдущую итерацию 
     * на основе загружаемых ранее сгенерированных значений.
     * Стартовая итерация равна нулю только при отсутствии вызовов
     * с непустыми аргументами.
     * Иначе стартовая итерация равна 1, либо на 1 больше максимально
     * найденной итерации среди загруженных значений.
     * @param value Загружаемое значение
     * @return true, если значение было проанализировано, иначе false
     */
    @Override
    public boolean addExisting(String value) {
        if (value==null)
            return false;
        value = value.trim().toLowerCase();
        if (value.length()==0)
            return false;
        if (iteration==0)
            iteration = 1;
        final String[] items = value.split(" ");
        final String candidate = items[items.length - 1];
        if (pattern.matcher(candidate).matches()) {
            try {
                int cur = Integer.parseInt(candidate);
                if (cur>0 && cur<1000000 && cur>=iteration)
                    iteration = cur + 1;
            } catch(NumberFormatException nfe) {}
        }
        return true;
    }

}
