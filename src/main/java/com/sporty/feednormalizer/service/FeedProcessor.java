package com.sporty.feednormalizer.service;

import com.sporty.feednormalizer.model.StandardMessage;

public interface FeedProcessor<T> {
    StandardMessage process(T message);
}