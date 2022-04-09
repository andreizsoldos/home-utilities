package com.home.keycode.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KeyCodeList<T> {

    private final List<T> list;

    public KeyCodeList(final List<T> builder) {
        this.list = builder;
    }

    public static class KeyCodeBuilder<T> {
        private final List<T> list;

        public KeyCodeBuilder() {
            this.list = new ArrayList<>();
        }

        public KeyCodeBuilder<T> addAll(final List<T> collection) {
            this.list.addAll(collection);
            return this;
        }

        public List<T> getList() {
            return this.list;
        }
    }
}
