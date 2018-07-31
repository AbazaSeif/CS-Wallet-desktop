package com.credits.wallet.domain.transformer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Transformer<From, To> {
    private Function<From, To> transform;

    public Transformer(Function<From, To> transformFunction) {
        this.transform = transformFunction;
    }

    public To transformOne(From from) {
        return transform.apply(from);
    }

    public List<To> batchTransform(List<From> listFrom) {
        List<To> transactions = listFrom.stream().map(transform).collect(Collectors.toList());
        return transactions;
    }
}
