package com.wb.httpforward.constant;

/**
 *
 * @author www
 */
public class RequestConstant {

    /**
     * 编码方式，全局UTF-8
     */
    public static final String CHARSET = "utf-8";
    /**
     * Controller方法的@RequestMapping{produces = RequestConstant.CONTROLLER_PRODUCES}
     */
    public static final String CONTROLLER_PRODUCES = "application/json;charset=" + CHARSET;


}
