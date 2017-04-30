package ru.mikroacse.engine.util;

import java.util.List;
import java.util.Random;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class ListUtil {
    public static <E> E getRandom(List<E> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
