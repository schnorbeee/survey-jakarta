package com.dynata.surveyhw.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuerySortUtils {

    /**
     * Létrehoz egy listát OrderSpecifier-ekből.
     *
     * @param sortMap rendezési mezők és irányuk (true = ascending, false = descending)
     * @param clazz   az entitás osztálya
     * @param <T>     entitás típusa
     * @return lista OrderSpecifier-ekből
     */
    public static <T> List<OrderSpecifier<?>> toOrderSpecifiers(Map<String, Order> sortMap, Class<T> clazz) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortMap == null || sortMap.isEmpty()) {
            return orderSpecifiers;
        }

        PathBuilder<T> entityPath = new PathBuilder<>(clazz, clazz.getSimpleName().toLowerCase());

        sortMap.forEach((property, order) -> {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            OrderSpecifier<? extends Comparable> spec =
                    new OrderSpecifier<>(order, entityPath.getComparable(property, Comparable.class));
            orderSpecifiers.add(spec);
        });

        return orderSpecifiers;
    }
}
