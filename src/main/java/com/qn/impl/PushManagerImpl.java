package com.qn.impl;

import com.qn.service.PushManager;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Service
public class PushManagerImpl implements PushManager {

    /**
     * @Desc: 开始运行shell脚本推流
     * @param: [cmd]
     * @return: void
     * @date: 2019/3/7 下午5:41
     */
    @Override
    public void startPush(String cmd) {
        runCmd(cmd);
//        runCmd("/usr/local/youq/ffmpeg/start.sh");
    }
    /**
     * @Desc: 运行shell命令
     * @param: [command]
     * @return: void
     * @date: 2019/3/7 下午5:22
     */
    private void runCmd(String command) {
        //String cmd = "/home/ty/t.sh";//这里必须要给文件赋权限 chmod u+x fileName;
        try {
            // 使用Runtime来执行command，生成Process对象
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);
            // 取得命令结果的输出流
            InputStream is = process.getInputStream();
            // 用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(is);
            // 用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("进程ID：" + line);
            }
            //执行关闭操作
            is.close();
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//
//        System.out.println("start push command");
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec(command);
//            InputStream stderr = proc.getErrorStream();
//            InputStreamReader isr = new InputStreamReader(stderr);
//            BufferedReader br = new BufferedReader(isr);
//            String line = null;
//            System.out.println("<ERROR>");
//            while ((line = br.readLine()) != null){
//                System.out.println(line);
//            }
//            System.out.println("</ERROR>");
//            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//        System.out.println("end push command");
        //System.out.println("ffmpeg进程ID：" + this.getPID("ffmpeg"));
    }

    /**
     * 获取Linux进程的PID
     *
     * @param command
     * @return
     */
    @Override
    public String killProcess(String command) {
        BufferedReader reader = null;
        try {
            //显示所有进程
            Process process = Runtime.getRuntime().exec("ps -ef");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(command)) {
                    //System.out.println("相关信息 -----> " + command);
                    String[] strs = line.split("\\s+");
                    System.out.println("kill process pid： " + strs[1]);
                    this.closeLinuxProcess(strs[1]);
                    //return strs[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    /**
     * 关闭Linux进程
     *
     * @param Pid 进程的PID
     */
    private void closeLinuxProcess(String Pid) {
        Process process = null;
        BufferedReader reader = null;
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + Pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println("kill PID return info -----> " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}






