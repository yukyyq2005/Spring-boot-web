package com.qn.model;

import java.io.Serializable;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
public class Config implements Serializable{
    private static final long serialVersionUID = 1L;
    //rtsp流地址
    private String rtspUrl;

    @Override
    public String toString() {
        return "Config{" +
                "rtspUrl='" + rtspUrl + '\'' +
                ", codeFormat='" + codeFormat + '\'' +
                ", bitrate='" + bitrate + '\'' +
                ", size='" + size + '\'' +
                ", frameRate='" + frameRate + '\'' +
                '}';
    }

    //编码格式 x264，x265
    private String codeFormat;
    //码率

    public Config(String rtspUrl, String codeFormat, String bitrate, String size, String frameRate) {
        this.rtspUrl = rtspUrl;
        this.codeFormat = codeFormat;
        this.bitrate = bitrate;
        this.size = size;
        this.frameRate = frameRate;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public String getCodeFormat() {
        return codeFormat;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getSize() {
        return size;
    }

    public String getFrameRate() {
        return frameRate;
    }

    private String bitrate;
    //视频画面的宽与高
    private String size;
    //帧率 默认为25
    private String frameRate;
}
