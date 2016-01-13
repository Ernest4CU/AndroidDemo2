package com.qlemon.test.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.qlemon.test.MainActivity;
import com.qlemon.test.bean.SerialPortSendData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android_serialport_api.SerialPort;

/**
 * TODO 读取身份证指令暂不支持
 * 串口操作工具类
 * １.锁指令支持(不同锁支持的锁指令可能不一样)
 * ２.扫描枪指令支持(不同枪支持的扫描指令可能不一样)
 * ３.读取身份证指令支持(不同设备支持的读取身份证指令可能不一样)
 * @author JeanSit added at 2016-01-12
 */
public class DevicesUtils {
    private static final String TAG = DevicesUtils.class.getSimpleName();
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private SerialPort mSerialPort;
    private Context context;
    private static final ExecutorService deviceThreadPool = Executors.newFixedThreadPool(1);
    private long timeout;
    private boolean autoClose = true;

    public DevicesUtils() {
        this.timeout = 1500;
    }

    public DevicesUtils(long timeout) {
        this.timeout = timeout;
    }

    public DevicesUtils(long timeout, boolean autoClose) {
        this.timeout = timeout;
        this.autoClose = autoClose;
    }

    /**
     * 从串口通信设备中获取返回数据的任务类
     */
    private class ReadTask implements Callable<RecevedData> {
        private SerialPortSendData sendData;

        public ReadTask(SerialPortSendData sendData) {
            this.sendData = sendData;
        }

        @Override
        public RecevedData call() {
            StringBuilder sb = new StringBuilder();
            while (true) {
                int size;
                try {
                    byte[] buffer = new byte[1024];
                    if (mInputStream == null)
                        return new RecevedData(ReturnType.ERR, "input stream is null");
                    size = mInputStream.read(buffer);
                    Log.i("TAG", "---------------mInputStream---------------" + mInputStream.available());
                    if (size > 0) { // 读取数据
                        String str = ConvertUtils.bytesToHexString(buffer, size).trim().toLowerCase();
                        sb.append(str);
                        Log.i("TAG", "OutputStream-->ComData"+sb.toString());
                        boolean flag = false;
                        if (sendData.special == SerialPortSendData.Signal.ALL_LOCK_STATUS.ordinal()) {
                            //锁状态检查：某块锁控板上所有的锁状态
                            if (sb.toString().length() == 16) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.LOCK_STATUS.ordinal()) {
                            //锁状态检查：某块锁控板上某个锁的状态
                            if (sb.toString().length() == 10) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.OPEN_LOCK.ordinal()) {
                            //开锁：打开某块锁控板上某个锁后一秒钟返加的状态(即返回锁状态检查的数据)
                            if (sb.toString().length() == 10) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.SCAN.ordinal()) {
                            //返回：扫描枪读取到的条形码
                            String receiData = sb.toString();
                            if (((receiData.indexOf(sendData.okStr, 0) == 0)
                                    || (receiData.indexOf(sendData.failStr, 0) == 0))
                                    && receiData.lastIndexOf(sendData.stopStr) > 0) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.FIND_ID_CARD.ordinal()) {
                            //寻找身份证信息
                            Log.i("TAG", "FIND_CARD="+sb.toString());
                            if (ConvertUtils.hexStringToBytes(sb.toString()).length == sendData.digitNum) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.SELECT_ID_CARD.ordinal()) {
                            //选取身份证信息
                            Log.i("TAG", "SELECT_CARD="+sb.toString());
                            if (ConvertUtils.hexStringToBytes(sb.toString()).length == sendData.digitNum) {
                                flag = true;
                            }
                        } else if (sendData.special == SerialPortSendData.Signal.READ_ID_CARD.ordinal()) {
                            //读取身份证信息(文字＋照片)＝14字节头＋(256 字节文字信息)+(1024 字节 照片信息)+(1 字节 CRC)
                            if (sb.toString().length() >= sendData.digitNum) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            final String msg = sb.toString();
                            Log.i("TAG", "run__msg="+msg);
                            if (null == context)
                                return new RecevedData(ReturnType.ERR, "context is null");
                            return new RecevedData(ReturnType.OK, msg);
                        }
                    }
                } catch (Exception e) {
                    return new RecevedData(ReturnType.Exception, e.getMessage());
                }
            }
        }
    }

    /**
     * 发送数据
     *
     * @param context
     * @param sendData
     * @param listener
     */
    public void toSend(final Context context, final SerialPortSendData sendData,
                       final ReciverListener listener) {
        this.context = context;
        if ("".equals(sendData.path) || "/dev/tty".equals(sendData.path)) {
            Toast.makeText(context, "设备地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(sendData.commandStr)) {
            Toast.makeText(context, "指令不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mSerialPort = getSerialPort(sendData.path, sendData.baudRate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

//            deviceThreadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            if (null != listener) {
                final Future<RecevedData> future = deviceThreadPool.submit(new ReadTask(sendData));
                deviceThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (future.isDone() && !future.isCancelled()) {
                                Log.i("TAG", "分析串口流数据：");
                                try {
                                    final RecevedData recevedData = future.get();
                                    if (null != recevedData) {
                                        ((MainActivity) context)
                                                .runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (recevedData.returnType == ReturnType.OK) {
                                                            listener.onReceived(recevedData.receviedData);
                                                        } else if (recevedData.returnType == ReturnType.ERR) {
                                                            listener.onFail(recevedData.receviedData);
                                                        } else if (recevedData.returnType == ReturnType.Exception) {
                                                            listener.onFail(recevedData.receviedData);
                                                        }
                                                    }
                                                });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        if (autoClose) {
                            closeDevice();
                        }
                        Log.i("TAG", "主线程回调显示后关闭串口");
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }

        // 上面是获取设置而已 下面这个才是发送指令
        byte[] text = ConvertUtils.hexStringToBytes(sendData.commandStr);
        try {
            mOutputStream.write(text);
            mOutputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取到串口通信实例
     *
     * @param path
     * @param baudrate
     * @return
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public SerialPort getSerialPort(String path, int baudrate)
            throws SecurityException, IOException, InvalidParameterException {
        if ((path.length() == 0) || (baudrate == -1)) {
            throw new InvalidParameterException();
        }
        if (null == mSerialPort)
            mSerialPort = new SerialPort(new File(path), baudrate, 0);// 打开这个串口
        return mSerialPort;
    }

    public void closeDevice() {
        closeSerialPort();
    }

    public void closeSerialPort() {// 关闭窗口
        if (null != mSerialPort) {
            mSerialPort.close();
        }
    }

    /**
     * 调用toSend方法需要回调的对象来实现
     */
    public interface ReciverListener {
        /**
         * 接受以后的处理方法
         *
         * @param receviceStr
         */
        public abstract void onReceived(String receviceStr);

        /**
         * 出错
         *
         * @param fialStr
         */
        public abstract void onFail(String fialStr);

        /**
         * 出现异常
         *
         * @param e
         */
        public abstract void onErr(Exception e);

    }

    /**
     * 串口返回的封装对象
     */
    public class RecevedData {
        public RecevedData() {
        }

        public RecevedData(ReturnType returnType, String receviedData) {
            this.returnType = returnType;
            this.receviedData = receviedData;
        }

        public ReturnType returnType;

        public String receviedData;
    }

    /**
     * 串口返回的封装数据类型
     */
    public enum ReturnType {
        ERR, // 错误
        OK, // OK
        Exception
    }
}
