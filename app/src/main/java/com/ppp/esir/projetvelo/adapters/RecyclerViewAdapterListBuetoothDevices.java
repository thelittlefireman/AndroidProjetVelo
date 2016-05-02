package com.ppp.esir.projetvelo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.utils.BluetoothCommandsUtils;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;

/**
 * Created by thoma on 02/05/2016.
 */
public class RecyclerViewAdapterListBuetoothDevices extends RecyclerView.Adapter<RecyclerViewAdapterListBuetoothDevices.RecyclerViewAdapterListBluetoothDevicesHolder> {
    private List<Device> deviceList;

    public RecyclerViewAdapterListBuetoothDevices() {
        deviceList = new ArrayList<>();

    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    @Override
    public RecyclerViewAdapterListBluetoothDevicesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_adapter_list_buetooth_devices_holder_item, parent, false);

        return new RecyclerViewAdapterListBluetoothDevicesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterListBluetoothDevicesHolder holder, int position) {
        final Device device = deviceList.get(position);
        holder.getTextViewDeviceName().setText(device.getName());
        holder.getTextViewMacAdress().setText(device.getAddress());
        holder.getButtonConnectTo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothCommandsUtils.getConnectionCallback().connectTo(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class RecyclerViewAdapterListBluetoothDevicesHolder extends RecyclerView.ViewHolder {
        private TextView textViewDeviceName, textViewMacAdress;
        private Button buttonConnectTo;

        public RecyclerViewAdapterListBluetoothDevicesHolder(View itemView) {
            super(itemView);
            textViewDeviceName = (TextView) itemView.findViewById(R.id.textViewDeviceName);
            textViewMacAdress = (TextView) itemView.findViewById(R.id.textViewMacAdress);
            buttonConnectTo = (Button) itemView.findViewById(R.id.buttonConnectTo);
        }

        public TextView getTextViewDeviceName() {
            return textViewDeviceName;
        }

        public TextView getTextViewMacAdress() {
            return textViewMacAdress;
        }

        public Button getButtonConnectTo() {
            return buttonConnectTo;
        }
    }
}
