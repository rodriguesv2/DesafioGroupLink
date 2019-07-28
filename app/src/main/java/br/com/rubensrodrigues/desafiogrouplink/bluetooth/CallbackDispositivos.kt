package br.com.rubensrodrigues.desafiogrouplink.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import android.widget.TextView
import br.com.rubensrodrigues.desafiogrouplink.model.Beacon
import java.util.*

class CallbackDispositivos(private val activity: Activity,
                           private val infoMajor: TextView,
                           private val infoMinor: TextView,
                           private val alerta: TextView): BluetoothAdapter.LeScanCallback {

    var ultimaAtualizacaoMillis = Calendar.getInstance().timeInMillis

    override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {

        activity.runOnUiThread {
            if (scanRecord != null){
                val beacon = Beacon(scanRecord)
                if (beacon.uuid == "20cc4ce3-5d0b-42c8-a57c-ed6ee945411e"){
                    Log.i("SCAN", "Major: ${beacon.major} - Minor: ${beacon.minor} - dBm: $rssi")
                    infoMajor.text = beacon.major.toString()
                    infoMinor.text = beacon.minor.toString()
                    ultimaAtualizacaoMillis = Calendar.getInstance().timeInMillis
                    alerta.visibility = View.INVISIBLE
                }else{
                    val delay = Calendar.getInstance().timeInMillis - ultimaAtualizacaoMillis
                    if (delay > 1000){
                        alerta.visibility = View.VISIBLE
                        infoMajor.text = ""
                        infoMinor.text = ""
                        Log.e("DESCONEXAO", "beacon offline")
                    }
                }

            }
        }
    }
}