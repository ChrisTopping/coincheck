package com.coincheck.coincheck.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BscDataseedRequest {
    private String jsonrpc;
    private int id;
    private String method;
    private List<Object> params;

    @Data
    @Builder
    public static class Params {
        private String data;
        private String to;
    }
}
