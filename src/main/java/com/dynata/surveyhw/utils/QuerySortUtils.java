package com.dynata.surveyhw.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QuerySortUtils {

    public static <T> List<OrderSpecifier<?>> toOrderSpecifiers(Pageable pageable, Class<T> clazz) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable == null || pageable.getSort().isUnsorted()) {
            return orderSpecifiers;
        }

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            // itt adunk típusparamétert: <Comparable>
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(direction,
                    Expressions.path(Comparable.class, clazz.getSimpleName().toLowerCase() + "." + order.getProperty())
            );
            orderSpecifiers.add(orderSpecifier);
        }

        return orderSpecifiers;
    }
}
