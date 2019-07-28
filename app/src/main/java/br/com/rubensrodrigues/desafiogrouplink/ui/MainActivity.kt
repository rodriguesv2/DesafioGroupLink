package br.com.rubensrodrigues.desafiogrouplink.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.rubensrodrigues.desafiogrouplink.R
import br.com.rubensrodrigues.desafiogrouplink.bluetooth.CallbackDispositivos
import br.com.rubensrodrigues.desafiogrouplink.util.Permissoes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_BT = 1
    private var scanning = false

    private val infoMajor by lazy { main_major }
    private val infoMinor by lazy { main_minor }
    private val alerta by lazy { main_alerta }
    private val campoScanTime by lazy { main_campo_scantime }
    private val botao by lazy { main_botao_scan }

    private val callbackDispositivos by lazy {
        CallbackDispositivos(this, infoMajor, infoMinor, alerta)
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verificaSuporteBLE()
        ativarBluetoothCasoDesligado()
        Permissoes.checaPermissoesLocalizacao(this)

        acaoBotao()
    }

    private fun acaoBotao() {
        botao.setOnClickListener {
            if (Permissoes.checaPermissoesLocalizacao(this)) {

                if (!scanning) {
                    var tempo: Long
                    try {
                        tempo = campoScanTime.text.toString().toLong()
                    }catch (ex: NumberFormatException){
                        tempo = 60
                        campoScanTime.setText("60")
                    }

                    scanLeDevice(true, tempo*1000)
                    desabilitaCampos()
                    Log.i("BOTAO", "FALSO")
                }else{
                    scanLeDevice(false)
                    habilitaCampos()
                    Log.i("BOTAO", "VERDADEIRO")
                }
            }
        }
    }

    private fun habilitaCampos() {
        botao.text = "Iniciar Scan"
        campoScanTime.isEnabled = true
        infoMajor.text = ""
        infoMinor.text = ""
        alerta.visibility = View.INVISIBLE
    }

    private fun desabilitaCampos() {
        botao.text = "Parar Scan"
        campoScanTime.isEnabled = false
    }

    private fun ativarBluetoothCasoDesligado() {
        if (temBT()) {
            ativarBT()
        }
    }

    private fun temBT(): Boolean {
        var valida = true

        if (bluetoothAdapter == null) {
            valida = false
            Toast.makeText(this, "Celular não suporta Bluetooth", Toast.LENGTH_SHORT).show()
        }

        return valida
    }

    private fun ativarBT() {
        if (!bluetoothAdapter!!.isEnabled()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun verificaSuporteBLE() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE não suportado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            @NonNull permissionsList: Array<String>,
                                            @NonNull grantResults: IntArray) {

        when(requestCode){
            Permissoes.CODIGO_LOCALIZACAO -> {
                if (grantResults.size > 0) {
                    val permissoesNegadas = StringBuilder()

                    for (permissao in permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissoesNegadas.append("\n").append(permissao)
                        }
                    }
                }
            }
        }
    }

    private fun scanLeDevice(enable: Boolean, tempo: Long = 0L) {
        if (enable) {
            Handler().postDelayed({
                scanning = false
                bluetoothAdapter!!.stopLeScan(callbackDispositivos)
                habilitaCampos()
            }, tempo)

            scanning = true
            bluetoothAdapter!!.startLeScan(callbackDispositivos)
        } else {
            scanning = false
            bluetoothAdapter!!.stopLeScan(callbackDispositivos)
        }
    }
}
