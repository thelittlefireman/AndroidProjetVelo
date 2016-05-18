package com.ppp.esir.projetvelo.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.activities.BluetoothActivity;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

/**
 * Created by thoma on 02/05/2016.
 */
public class RecyclerViewAdapterListBuetoothDevices extends CursorRecyclerAdapter<RecyclerViewAdapterListBuetoothDevices.RecyclerViewAdapterListBluetoothDevicesHolder> {
    private BluetoothActivity bluetoothActivity;

    public RecyclerViewAdapterListBuetoothDevices(BluetoothActivity bluetoothActivity, final EasyObjectCursor<BluetoothLeDevice> cursor) {
        super(cursor);
        this.bluetoothActivity = bluetoothActivity;

    }

    @SuppressWarnings("unchecked")
    @Override
    public EasyObjectCursor<BluetoothLeDevice> getCursor() {
        return ((EasyObjectCursor<BluetoothLeDevice>) super.getCursor());
    }

    public BluetoothLeDevice getItem(final int i) {
        return getCursor().getItem(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }


    @Override
    public RecyclerViewAdapterListBluetoothDevicesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_adapter_list_buetooth_devices_holder_item, parent, false);

        return new RecyclerViewAdapterListBluetoothDevicesHolder(itemView);
    }


    @Override
    public void onBindViewHolderCursor(RecyclerViewAdapterListBluetoothDevicesHolder holder, final Cursor cursor) {
        final BluetoothLeDevice device = getCursor().getItem(cursor.getPosition());
        holder.buttonConnectTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothLeDevice device = getCursor().getItem(cursor.getPosition());
                bluetoothActivity.getmBluetoothLeService().connect(device.getAddress());
            }
        });
        holder.textViewDeviceName.setText(device.getName());
        holder.textViewMacAdress.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return getCursor().getCount();
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
