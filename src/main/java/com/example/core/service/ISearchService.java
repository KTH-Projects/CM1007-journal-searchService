package com.example.core.service;

import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ISearchService {
    public Uni<List<Object>> search(String term);
}
