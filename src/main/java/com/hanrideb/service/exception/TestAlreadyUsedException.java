package com.hanrideb.service.exception;

public class TestAlreadyUsedException extends Exception {

    public TestAlreadyUsedException() {
        super("bu test analizi daha önce kaydedilmiş");
    }
}
