package com.qn.task;

import com.qn.model.Config;
import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 **/

@Component//被容器扫描
@EnableScheduling //开启定时任务，自动扫描
public class TaskService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //  "/usr/local/youq/ffmpeg/push.sh"
    private final static String SERI_FILE_NAME = "push.sh";

    @Autowired
    ConfigService configService;
    @Autowired
    PushManager pushManager;

    @Scheduled(cron = "*/2 * * * * *")//每2秒执行一次
    //@Scheduled(fixedRate = 1000*60*1)
    public boolean monitorIPC() {
        String ipcUrl = "rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream";
        if (this.reachableIPC(ipcUrl)){
            if (this.readConfigFileStartPush()){
                pushManager.startPush(SERI_FILE_NAME);
            }
        }
        return false;
    }
    /**
     * @Desc: 摄像头是否打开
     * @param: [ipcUrl]
     * @return: boolean
     * @date: 2019/3/7 下午5:27
     */
    public boolean reachableIPC(String ipcUrl) {
        //System.out.println("youqiang");
        //logger.debug("youqiang");
        //        String host1 = "rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream";
        //正则匹配找出url里面的ip地址
        String host = null;
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(ipcUrl);
        if (matcher.find()) {
            host = matcher.group();
        }
        boolean isPingSuc = false;
        try {
            isPingSuc = this.ping(host);
            if (isPingSuc){
                System.out.println("ip addr is : " + host);
            }
        } catch (Exception e) {
            logger.error("ip Address error: ", e);
            //e.printStackTrace();
            System.out.println("网络摄像头 " + host +" 不可达 ");
        }
        return isPingSuc;
    }
    /**
     * @Desc: 读取配置文件，开始推流
     * @param: []
     * @return: void
     * @date: 2019/3/7 下午5:30
     */
    private boolean readConfigFileStartPush() {

        boolean result = false;
//        Config config = configService.getConfigInfo();

        String str1 = "ffmpeg  -rtsp_transport tcp -i ";
        String ipcUrl = "rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream ";
        String imageSize = "1280x720 ";
        String codeFormat = "libx264";
        String bitrate = "1000";
        int iRate = Integer.parseInt(bitrate);
        int gop = 20;
        if (iRate < 512) {
            gop = 40;
        } else if (iRate < 1500) {
            gop = 30;
        } else if (iRate < 2000) {
            gop = 25;
        } else {
            gop = 20;
        }
        String pushUrl = str1 + ipcUrl + " -s:v " + imageSize + " -bufsize:v " + iRate / 2 + "k" + " -b:v " + iRate + "k"
                + " -bt " + iRate + "k" + " -maxrate:v " + (iRate - 50) + "k" + " -minrate:v " + (iRate - 100) + "k" + " -g " + gop + " -codec:v " + codeFormat + " -keyint_min 2 -nal-hrd cbr  -pass 1 -passlogfile " +
                "ffmpeg2pass -sc_threshold 0 -bf 0 -b_strategy 0 -r 20 " +
                "-profile:v high -preset:v fast -tune:v zerolatency -f flv \"rtmp://192.168.1.133:1935/live/push\" 1>1.txt 2>&1 &";

//ffmpeg  -rtsp_transport tcp -i "rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream"
// -s:v 1280x720 -bufsize:v 600k -b:v 1350k -bt 1350k -maxrate:v 1300k -minrate:v 1200k -g 20 -keyint_min 2 -nal-hrd cbr
// -pass 1 -passlogfile ffmpeg2pass -codec:v libx264 -sc_threshold 0 -bf 0 -b_strategy 0 -r 20 -profile:v high
// -preset:v fast -tune:v zerolatency -f flv "rtmp://192.168.1.133:1935/live/push" 1>1.txt 2>&1 &
        //config.getRtspUrl();
//
//        String pushUrl = "ffmpeg  -rtsp_transport tcp " +
//                "-i \"rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream\" " +
//                "-s:v 1280x720 -bufsize:v 600k -b:v 1350k -bt 1350k -maxrate:v 1300k -minrate:v 1200k -g 20 " +
//                "-keyint_min 2 -nal-hrd cbr  -pass 1 -passlogfile ffmpeg2pass -codec:v libx264 " +
//                "-sc_threshold 0 -bf 0 -b_strategy 0 -r 20 -profile:v high -preset:v fast -tune:v zerolatency " +
//                "-f flv \"rtmp://192.168.1.133:1935/live/push\" 1>1.txt 2>&1 &";
        System.out.println(pushUrl);
        FileWriter writer;
        try {
            writer = new FileWriter(SERI_FILE_NAME);
            writer.write(pushUrl);
            writer.flush();
            writer.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("FileWriter fail : ", e);
        }
        return result;
    }

    private boolean ping(String ipAddress) throws Exception {
        int timeOut = 3000;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
//        System.out.println("exit VolumeManage.execPingCommand(String deviceIp)"
//                + "[networkUseable] =" + status);
        return status;
    }

    /**
     * @param @param deviceIp
     * @return boolean true-能ping通，false-不能ping通
     * @throws
     * @Title: execPingCommand
     * @Description: 执行ping命令，查看设备是否可用
     */

    public boolean execPingCommand(String deviceIp) {
        logger.debug("enter VolumeManage.execPingCommand(String deviceIp)");
        boolean networkUseable = false;
// String address="www.javawind.net";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ping -c 3" + deviceIp);
        } catch (IOException e1) {
            logger.error("System error:", e1);
        }
        InputStreamReader r = new InputStreamReader(process.getInputStream());
        LineNumberReader returnData = new LineNumberReader(r);
        String returnMsg = "";
        String line = "";
        try {
            while ((line = returnData.readLine()) != null) {
                System.out.println(line);
                returnMsg += line;
            }
            if (returnMsg.indexOf("Unreachable") != -1
                    || returnMsg.indexOf("100% packet loss") != -1) {
                networkUseable = false;
            } else {
                networkUseable = true;
            }
        } catch (IOException e) {
            logger.error("System error:", e);
        } finally {
            if (returnData != null) {
                try {
                    returnData.close();
                } catch (IOException e) {
// TODO Auto-generated catch block
                    logger.error("System error:", e);
                }
            }
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    logger.error("System error:", e);
                }
            }
        }
        logger.debug("exit VolumeManage.execPingCommand(String deviceIp)"
                + "[networkUseable] =" + networkUseable);
        return networkUseable;
    }
}
