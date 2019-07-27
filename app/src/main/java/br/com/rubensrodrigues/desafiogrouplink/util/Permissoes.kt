package br.com.rubensrodrigues.desafiogrouplink.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.ArrayList

object Permissoes {

    val CODIGO_LOCALIZACAO = 10

    private val permissoesLocalizacao =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)

    fun checaPermissoesLocalizacao(activity: Activity): Boolean {
        var result: Int
        val permissoesNecessarias = ArrayList<String>()

        for (permissao in permissoesLocalizacao) {
            result = ContextCompat.checkSelfPermission(activity, permissao)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissoesNecessarias.add(permissao)
            }
        }

        if (!permissoesNecessarias.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity, permissoesNecessarias
                    .toTypedArray<String>(), CODIGO_LOCALIZACAO)

            return false
        }

        return true
    }
}