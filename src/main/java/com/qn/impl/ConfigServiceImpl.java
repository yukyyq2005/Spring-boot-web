package com.qn.impl;

import com.qn.model.Config;
import com.qn.service.ConfigService;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Service
public class ConfigServiceImpl implements ConfigService{

    private final static String SERI_FILE_NAME = "config.ser";

    @Override
    public Config getConfigInfo() {
        //声明序列化后的名称, 文件保存在项目的根目录下（和src目录平级）
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(SERI_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Config config = null;
        //开始读取
        try {
            config = (Config)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public boolean saveConfigInfo(Config config) {

        boolean result = true;
        //声明序列化后的名称, 文件保存在项目的根目录下（和src目录平级）
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(SERI_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        //开始写入
        try {
            oos.writeObject(config);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        System.out.println("file write finished");
        //关闭流
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
