package com.qlemon.test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.qlemon.test.util.NationUtils;
import com.qlemon.test.R;
import com.qlemon.test.bean.SerialPortSendData;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 串口测试界面
 * @author JeanSit added at 2016-01-12
 */
public class CardFragment extends Fragment {

    private String cardCom;
    private Spinner cardComSpinner;

    private String cardComBaudate;
    private Spinner cardComBaudateSpinner;

    private TextView errorText;

    Button cardBtn;

    private DevicesUtils device;

    public CardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card, container, false);

        //锁控板对应的串口
        cardComSpinner = (Spinner)view.findViewById(R.id.cardComSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.comArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardComSpinner.setAdapter(adapter);
        cardComSpinner.setSelection(4);

        //锁控板波特率
        cardComBaudateSpinner = (Spinner)view.findViewById(R.id.cardComBaudateSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.baudateArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardComBaudateSpinner.setAdapter(adapter2);
        cardComBaudateSpinner.setSelection(2);

        errorText = (TextView)view.findViewById(R.id.errorText);

        cardBtn = (Button)view.findViewById(R.id.cardBtn);
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCom = ((TextView)cardComSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(cardCom)) {
                    errorText.setText("串口设备 不能为空");
                    return;
                }
                cardComBaudate = ((TextView) cardComBaudateSpinner.getSelectedView()).getText().toString();
                if (ConvertUtils.isBlank(cardComBaudate)) {
                    errorText.setText("波特率 不能为空");
                    return;
                }
                ((Button)v).setClickable(false);
                device = new DevicesUtils(2000, false);
                findIdCard(device);
            }
        });

        Button resetBtn = (Button)view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBtn.setClickable(true);
            }
        });

        return view;
    }

    private void findIdCard(final DevicesUtils device) {
        SerialPortSendData sendData = new SerialPortSendData(cardCom, Integer.parseInt(cardComBaudate),
                DeviceConstants.FIND_ID_CARD, "", "", "", true);
        sendData.special = SerialPortSendData.Signal.FIND_ID_CARD.ordinal();
        sendData.digitNum = 15;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                selectIdCard(device);
            }

            @Override
            public void onFail(String fialStr) {
                device.closeDevice();
            }

            @Override
            public void onErr(Exception e) {
                device.closeDevice();
            }
        });
    }

    private void selectIdCard(final DevicesUtils device) {
        SerialPortSendData sendData = new SerialPortSendData(cardCom, Integer.parseInt(cardComBaudate),
                DeviceConstants.SELECT_ID_CARD, "", "", "", true);
        sendData.special = SerialPortSendData.Signal.SELECT_ID_CARD.ordinal();
        sendData.digitNum = 19;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                readIdCard(device);
            }

            @Override
            public void onFail(String fialStr) {
                device.closeDevice();
            }

            @Override
            public void onErr(Exception e) {
                device.closeDevice();
            }
        });
    }

    private void readIdCard(final DevicesUtils device) {
        SerialPortSendData sendData = new SerialPortSendData(cardCom, Integer.parseInt(cardComBaudate),
                DeviceConstants.READ_ID_CARD, "", "", null,
                true);
        sendData.special = SerialPortSendData.Signal.SELECT_ID_CARD.ordinal();
        sendData.digitNum = 1295;
        device.toSend(getActivity(), sendData, new DevicesUtils.ReciverListener() {
            @Override
            public void onReceived(String receviceStr) {
                byte[] card = ConvertUtils.hexStringToBytes(receviceStr);
                if (card.length > 270) {
                    byte[] cardArray = Arrays.copyOfRange(card, 14, 270);
                    try {
                        String name = new String(Arrays.copyOfRange(cardArray,0, 30),"UTF-16LE").trim().trim();
                        String gender = new String(Arrays.copyOfRange(cardArray,30, 32),"UTF-16LE").trim();
                        String nation = new String(Arrays.copyOfRange(cardArray,32, 36),"UTF-16LE").trim();
                        String birthday = new String(Arrays.copyOfRange(cardArray,36, 52),"UTF-16LE").trim();
                        String address = new String(Arrays.copyOfRange(cardArray,52, 122),"UTF-16LE").trim();
                        String idCard = new String(Arrays.copyOfRange(cardArray,122, 158),"UTF-16LE").trim();
                        String issuingAuthority = new String(Arrays.copyOfRange(cardArray,158, 188),"UTF-16LE").trim();
                        String startTime = new String(Arrays.copyOfRange(cardArray,188, 204),"UTF-16LE").trim();
                        String startopTime = new String(Arrays.copyOfRange(cardArray,204, 220),"UTF-16LE").trim();
                        gender = gender.equals("1")?"Male":(gender.equals("2")?"FeMale":"Privacy");
                        //名族的特殊处理
                        nation = NationUtils.getNationNameById(nation);
                        errorText.setText(idCard);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                device.closeDevice();
            }

            @Override
            public void onFail(String fialStr) {
                device.closeDevice();
            }

            @Override
            public void onErr(Exception e) {
                device.closeDevice();
            }
        });
    }
}
