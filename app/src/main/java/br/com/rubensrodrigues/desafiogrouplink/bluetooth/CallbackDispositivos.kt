package br.com.rubensrodrigues.desafiogrouplink.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.widget.TextView
import br.com.rubensrodrigues.desafiogrouplink.model.Beacon

class CallbackDispositivos(val activity: Activity, val infoMajor: TextView, val infoMinor: TextView): BluetoothAdapter.LeScanCallback {

    override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {

        activity.runOnUiThread(object: Runnable{
            override fun run() {
                if (scanRecord != null){
                    val beacon = Beacon(scanRecord)
                    if (beacon.uuid == "20cc4ce3-5d0b-42c8-a57c-ed6ee945411e"){
                        infoMajor.text = beacon.major.toString()
                        infoMinor.text = beacon.minor.toString()
                    }
                }
            }
        })
    }
}