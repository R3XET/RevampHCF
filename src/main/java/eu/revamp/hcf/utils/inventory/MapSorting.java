package eu.revamp.hcf.utils.inventory;

import com.google.common.collect.Iterables;
import java.util.Comparator;
import com.google.common.collect.Ordering;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;

public class MapSorting
{
    private static Function EXTRACT_KEY = (Function<Map.Entry<Object, Object>, Object>) input -> (input == null) ? null : input.getKey();
    private static Function EXTRACT_VALUE = (Function<Map.Entry<Object, Object>, Object>) input -> (input == null) ? null : input.getValue();
    
    public static <T, V extends Comparable<V>> List<Map.Entry<T, V>> sortedValues(Map<T, V> map) {
        return sortedValues(map, Ordering.natural());
    }
    
    public static <T, V> List<Map.Entry<T, V>> sortedValues(Map<T, V> map, final Comparator<V> valueComparator) {
        return (List<Map.Entry<T, V>>)Ordering.from((Comparator)valueComparator).onResultOf(extractValue()).sortedCopy(map.entrySet());
    }
    
    public static <T, V> Iterable<T> keys(List<Map.Entry<T, V>> entryList) {
        return Iterables.transform(entryList, extractKey());
    }
    
    public static <T, V> Iterable<V> values(List<Map.Entry<T, V>> entryList) {
        return Iterables.transform(entryList, extractValue());
    }
    
    private static <T, V> Function<Map.Entry<T, V>, T> extractKey() {
        return (Function<Map.Entry<T, V>, T>)MapSorting.EXTRACT_KEY;
    }
    
    private static <T, V> Function<Map.Entry<T, V>, V> extractValue() {
        return (Function<Map.Entry<T, V>, V>)MapSorting.EXTRACT_VALUE;
    }
}
