package com.qlemon.test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.qlemon.test.util.ConvertUtils;
import com.qlemon.test.DeviceConstants;
import com.qlemon.test.util.DevicesUtils;
import com.qlemon.test.R;
import com.qlemon.test.bean.SerialPortSendData;

/**
 * 串口测试界面
 * @author JeanSit added at 2016-01-12
 */
public class ScanFragment extends Fragment {

    private String scanCom;
    private Spinner scanComSpinner;

    private String scanComBaudate;
    private Spinner scanComBaudateSpinner;

    private TextView errorText;

    Button scanBtn;

    private DevicesUtils device;

    public ScanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scan, container, false);

        //锁控板对应的串口
        scanComSpinner = (Spinner)view.findViewById(R.id.scanComSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.comArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scanComSpinner.setAdapter(adapter);
        scanComSpinner.setSelection(6);

        //锁控板波特率
        scanComBaudateSpinner = (Spinner)view.findViewById(R.id.scanComBaudateSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.baudateArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scanComBaudateSpinner.setAdapter(adapter2);
        scanComBaudateSpinner.setSelection(0);

        errorText = (TextView)view.findViewById(R.id.errorText);

        scanBtn = (Button)view.findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCom = ((TextView)scanComSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(scanCom)) {
                    errorText.setText("串口设备 不能为空");
                    return;
                }
                scanComBaudate = ((TextView) scanComBaudateSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(scanComBaudate)) {
                    errorText.setText("波特率 不能为空");
                    return;
                }
                ((Button)v).setClickable(false);
                device = new DevicesUtils();
                scan(device);
            }
        });

        Button resetBtn = (Button)view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBtn.setClickable(true);
            }
        });

        return view;
    }

    private void scan(final DevicesUtils device) {
        SerialPortSendData sendData = new SerialPortSendData(scanCom, Integer.parseInt(scanComBaudate),
                DeviceConstants.SCAN_START, DeviceConstants.SCAN_RSP_OK,
                DeviceConstants.SCAN_RSP_FAIL, DeviceConstants.SCAN_STOP,
                true);
        sendData.special = SerialPortSendData.Signal.SCAN.ordinal();
        sendData.digitNum = -1;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                receviceStr = ConvertUtils.convertHexToString(receviceStr);
                Log.i("TAG", "扫描返回"+receviceStr);
                errorText.setText(receviceStr);
                scanBtn.setClickable(true);
            }

            @Override
            public void onFail(String fialStr) {
                device.closeDevice();
                scanBtn.setClickable(true);
            }

            @Override
            public void onErr(Exception e) {
                device.closeDevice();
                scanBtn.setClickable(true);
            }
        });
    }
}
