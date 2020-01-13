package com.example.demo.modal;

import java.util.HashMap;

/**
 * Created by 86131 on 2020/1/13.
 */
public class DataInfo<T> extends BaseInfo {

    public DataInfo() {
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> DataInfo<T> success(T t) {
        DataInfo result = success();
        result.setData(t);
        return result;
    }

    public static <T> DataInfo<Object> success() {
        DataInfo result = new DataInfo();
        result.setStatus(SUCCESS_CODE);
        result.setMessage(SUCCESS_MESSAGE);
        result.setData(new HashMap<>());
        return result;
    }

    public static DataInfo error(String message) {
        DataInfo result = new DataInfo();
        result.setStatus(ERROR_CODE);
        result.setMessage(message);
        return result;
    }
}
