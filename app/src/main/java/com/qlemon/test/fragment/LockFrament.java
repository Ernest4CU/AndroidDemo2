package com.qlemon.test.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.qlemon.test.bean.BoxBean;
import com.qlemon.test.util.ConvertUtils;
import com.qlemon.test.util.DevicesUtils;
import com.qlemon.test.R;
import com.qlemon.test.bean.SerialPortSendData;

import java.util.ArrayList;
import java.util.List;

/**
 * 串口测试界面
 * @author JeanSit added at 2016-01-12
 */
public class LockFrament extends Fragment {

    private String lockerCom;
    private Spinner lockerComSpinner;

    private String lockerComBaudate;
    private Spinner lockerComBaudateSpinner;

    private String lockerBoardNum;

    private String lockerNum;

    private TextView errorText;

    Button lockerStatusBtn;

    Button perLockerStatusBtn;

    private List<BoxBean> boxBeanList;

    private DevicesUtils device;

    public LockFrament() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        //锁控板对应的串口
        lockerComSpinner = (Spinner)view.findViewById(R.id.lockerComSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.comArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lockerComSpinner.setAdapter(adapter);

        //锁控板波特率
        lockerComBaudateSpinner = (Spinner)view.findViewById(R.id.lockerComBaudateSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.baudateArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lockerComBaudateSpinner.setAdapter(adapter2);

        //锁控板数量
        final EditText lockerBoardNumText = (EditText)view.findViewById(R.id.lockerBoardNum);

        //每块锁控板的格口数
        final EditText lockerNumText = (EditText)view.findViewById(R.id.lockerNum);


        errorText = (TextView)view.findViewById(R.id.errorText);
        final Button btn = (Button)view.findViewById(R.id.startTestBtn);
        final Button stopBtn = (Button)view.findViewById(R.id.stopTestBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != device) {
                    device.closeDevice();
                }
                handler.removeMessages(999);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockerCom = ((TextView)lockerComSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerCom)) {
                    errorText.setText("串口设备 不能为空");
                    return;
                }
                lockerComBaudate = ((TextView) lockerComBaudateSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerComBaudate)) {
                    errorText.setText("波特率 不能为空");
                    return;
                }

                lockerBoardNum = lockerBoardNumText.getText().toString();

                lockerNum = lockerNumText.getText().toString();

                if (ConvertUtils.isBlank(lockerBoardNum)) {
                    errorText.setText("锁控板数量 不能为空");
                    return;
                }
                if (ConvertUtils.isBlank(lockerNum)) {
                    errorText.setText("每块锁控板的格口数 不能为空");
                    return;
                }
                if (currentIndex > 0) {
                    errorText.setText("正在进行开锁测试　请不要再点击");
                    return;
                }
                assembleLockerInfo(Integer.valueOf(lockerBoardNum), Integer.valueOf(lockerNum));
                device = new DevicesUtils(1500, false);
                handler.sendEmptyMessageDelayed(999, 1500);
                errorText.setText("正在进行格口测试，请不要再点击按钮!");
            }
        });

        lockerStatusBtn = (Button)view.findViewById(R.id.lockerStatusBtn);
        lockerStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockerCom = ((TextView)lockerComSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerCom)) {
                    errorText.setText("串口设备 不能为空");
                    return;
                }
                lockerComBaudate = ((TextView) lockerComBaudateSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerComBaudate)) {
                    errorText.setText("波特率 不能为空");
                    return;
                }
                EditText lockerBoardNoText = (EditText)view.findViewById(R.id.lockerBoardNo);
                String lockerBoardNo = lockerBoardNoText.getText().toString();
                if (ConvertUtils.isBlank(lockerBoardNo)) {
                    errorText.setText("锁控板编号　不能为空");
                    return;
                }
                ((Button)v).setClickable(false);
                device = new DevicesUtils();
                lockStatus(device, lockerBoardNo);
            }
        });

        perLockerStatusBtn = (Button)view.findViewById(R.id.perLockerStatusBtn);
        perLockerStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockerCom = ((TextView)lockerComSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerCom)) {
                    errorText.setText("串口设备 不能为空");
                    return;
                }
                lockerComBaudate = ((TextView) lockerComBaudateSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(lockerComBaudate)) {
                    errorText.setText("波特率 不能为空");
                    return;
                }

                EditText lockerBoardNoText = (EditText)view.findViewById(R.id.lockerBoardNo);
                String lockerBoardNo = lockerBoardNoText.getText().toString();
                if (ConvertUtils.isBlank(lockerBoardNo)) {
                    errorText.setText("锁控板编号 不能为空");
                    return;
                }
                EditText lockerNoText = (EditText)view.findViewById(R.id.lockerNo);
                String lockerNo = lockerNoText.getText().toString();
                if (ConvertUtils.isBlank(lockerNo)) {
                    errorText.setText("锁编号 不能为空");
                    return;
                }
                ((Button)v).setClickable(false);
                device = new DevicesUtils();
                openLockAndCheckStatus(device, lockerBoardNo, lockerNo);
            }
        });

        Button resetBtn = (Button)view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setClickable(true);
                stopBtn.setClickable(true);
                lockerStatusBtn.setClickable(true);
                perLockerStatusBtn.setClickable(true);
            }
        });

        return view;
    }

    private int total = 0;

    private int currentIndex = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    if (currentIndex < total) {
                        openBox(device, boxBeanList.get(currentIndex++));
                        handler.sendEmptyMessageDelayed(999, 1500);
                    } else {
                        device.closeDevice();
                        handler.removeMessages(999);
                        currentIndex = 0;
                        errorText.setText("自动开锁测试结束");
                    }
                default:
                    Log.i("TAG", "nothing");
            }
        }
    };

    /**
     * 封装锁数据
     * @param lockerBoardNum
     * @param perBoardLockerNum
     */
    private void assembleLockerInfo(int lockerBoardNum, int perBoardLockerNum) {
        boxBeanList = new ArrayList<BoxBean>();
        for (int i=1; i<=lockerBoardNum; i++) {
            for (int j=1; j<=perBoardLockerNum; j++) {
                boxBeanList.add(new BoxBean(String.valueOf(i), String.valueOf(j)));
            }
        }
        total = boxBeanList.size();
    }

    /**
     * 开锁操作
     * @param bean
     */
    private void openBox(DevicesUtils device, final BoxBean bean) {
        if (null != device) {
            String opencmd = "";
            StringBuilder data = new StringBuilder();
            data.append("8a");
            data.append(ConvertUtils.convertDecToHexString(bean.board_no));
            data.append(ConvertUtils.convertDecToHexString(bean.physic_no));
            data.append("11");
            final String checkDigit = ConvertUtils.xor(data.toString());
            opencmd = data.append(checkDigit).toString();
            final String openStopstr = "11" + ConvertUtils.xor(opencmd + "11");
            SerialPortSendData sendData = new SerialPortSendData(lockerCom, Integer.parseInt(lockerComBaudate), opencmd, "", "",openStopstr, false);
            //不监听时无须设置下面二个值
            sendData.special = SerialPortSendData.Signal.OPEN_LOCK.ordinal();
            sendData.digitNum = 10;
            device.toSend(getActivity(), sendData, null);
        }
    }

    /**
     * 某块板的所有锁状态检查
     * @param device
     * @param board_no
     */
    private void lockStatus(DevicesUtils device, final String board_no) {
        String statusCmd = "";
        StringBuilder data = new StringBuilder();
        data.append("80");
        data.append(ConvertUtils.convertDecToHexString(board_no));
        data.append("00");
        data.append("33");
        final String checkDigit = ConvertUtils.xor(data.toString());
        statusCmd = data.append(checkDigit).toString();
        SerialPortSendData sendData = new SerialPortSendData(lockerCom, Integer.parseInt(lockerComBaudate), statusCmd, "", "", "", false);
        //监听时须设置以下二个值
        sendData.special = SerialPortSendData.Signal.ALL_LOCK_STATUS.ordinal();
        sendData.digitNum = 16;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                StringBuilder desc = new StringBuilder();
                byte[] byteArray = ConvertUtils.hexStringToByteArray(receviceStr, true);
                int index = 1;
                for (byte status : byteArray) {
                    desc.append("锁控板编号").append(board_no).append("锁编号").append(index).append(status=='1'?"锁状态打开":"锁状态关闭").append("\n");
                }
                errorText.setText(desc.toString());
                lockerStatusBtn.setClickable(true);
            }

            @Override
            public void onFail(String fialStr) {
                errorText.setText("锁状态检查发生了onFail失败");
            }

            @Override
            public void onErr(Exception e) {
                errorText.setText("锁状态检查发生了onErr错误");
            }

        });
    }

    /**
     * 某块板的某块锁状态检查(硬件设备开锁后一秒钟后会返回锁状态)
     * @param device
     * @param board_no
     * @param locker_no
     */
    private void openLockAndCheckStatus(DevicesUtils device, String board_no, String locker_no) {
        StringBuilder data = new StringBuilder();
        StringBuilder statusCloseData = new StringBuilder();
        data.append("8a");
        data.append(ConvertUtils.convertDecToHexString(board_no));
        data.append(ConvertUtils.convertDecToHexString(locker_no));
        statusCloseData.append(data.toString()).append("00");
        data.append("11");
        final String closeStatus = statusCloseData.append(ConvertUtils.xor(statusCloseData.toString())).toString();
        final String openCmd = data.append(ConvertUtils.xor(data.toString())).toString();
        SerialPortSendData sendData = new SerialPortSendData(lockerCom, Integer.parseInt(lockerComBaudate), openCmd, "", "", "", false);
        sendData.special = SerialPortSendData.Signal.OPEN_LOCK.ordinal();
        sendData.digitNum = 10;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                if (openCmd.equals(receviceStr)) {
                    errorText.setText("开锁后检查锁状态为开");
                } else if (closeStatus.equals(receviceStr)) {
                    errorText.setText("开锁后检查锁状态为关");
                } else {
                    errorText.setText("某块板的某块锁状态检查返回"+receviceStr);
                }
                perLockerStatusBtn.setClickable(true);
            }

            @Override
            public void onFail(String fialStr) {
                errorText.setText("某块板的某块锁状态检查发生了onFail失败");
            }

            @Override
            public void onErr(Exception e) {
                errorText.setText("某块板的某块锁状态检查发生了onErr错误");
            }

        });
    }


}
