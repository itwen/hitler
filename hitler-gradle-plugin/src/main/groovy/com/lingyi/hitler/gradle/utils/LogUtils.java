package com.lingyi.hitler.gradle.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * @author sunchuanwen
 * @time 2019/5/8.
 */
public class LogUtils {
    private String mProjectPath;
    private static final String LOG_PREFIX = "[hitler-log]:";
    private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(2000);
    private int logLevel = 2;

    private LogUtils() {
    }

    private WriteThread mThread;

    private static class SingleHolder {
        public static LogUtils instance = new LogUtils();
    }

    public static LogUtils getInstance() {
        return SingleHolder.instance;
    }

    public void setLogLevel(int level){
        this.logLevel = level;
        if (level == 1){
            if (mThread != null && mThread.isAlive()){
                mThread.interrupt();
            }
        }else if (logLevel == 2){
            if (mThread == null || !mThread.isAlive()){
                createLogFile();
                startLog();
            }
        }
    }

    private void createLogFile(){
        mProjectPath = FilePathUtil.getHitlerBuildPath();
        File file = new File(mProjectPath);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }

    private void startLog() {
        mThread = new WriteThread();
        mThread.start();
    }

    public void log(String message) {
        if (message != null) {
            if(logLevel == 2 ){
                messageQueue.offer(LOG_PREFIX + message);
            }else if(logLevel == 1){
                System.out.println(LOG_PREFIX + message);
            }
        }
    }

    public void end() {
        if(mThread != null && !mThread.isInterrupted){
            mThread.interrupt();
        }

    }

    private class WriteThread extends Thread {

        private boolean isInterrupted = false;

        public WriteThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            super.run();
            File file = new File(mProjectPath, "hitler_build_log.txt");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                while (!isInterrupted) {
                    String message = messageQueue.take();
                    fos.write((message + "\n").getBytes());
                }
            } catch (Exception e) {
                System.out.println("hitler thread interrupt!!!!!");
            } finally {
                try {
                    fos.flush();
                    fos.close();
                } catch (Exception e) {

                }
            }
        }

        @Override
        public void interrupt() {
            isInterrupted = true;
            super.interrupt();
        }
    }
}
