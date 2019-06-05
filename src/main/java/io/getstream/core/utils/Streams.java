package io.getstream.core.utils;

import java8.util.Spliterator;
import java8.util.Spliterators;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

public class Streams {
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterable.iterator(),
                        Spliterator.ORDERED
                ),
                false
        );
    }
}
