package com.imagedb.image_db_backend.model;

import java.util.List;

public class ResponseFromQueryFileNameAPI {

    private List<ResultFromZilliz> results;

    public ResponseFromQueryFileNameAPI() {
    }

    public ResponseFromQueryFileNameAPI(List<ResultFromZilliz> results) {
        this.results = results;
    }

    public List<ResultFromZilliz> getResults() {
        return results;
    }

    public void setResults(List<ResultFromZilliz> results) {
        this.results = results;
    }
}
