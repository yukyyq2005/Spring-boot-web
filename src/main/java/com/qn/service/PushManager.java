package com.qn.service;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
public interface PushManager {
    void startPush(String cmd);
    String killProcess(String cmd);
}

