package br.com.rubensrodrigues.desafiogrouplink.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.rubensrodrigues.desafiogrouplink.R
import br.com.rubensrodrigues.desafiogrouplink.model.Beacon
import kotlinx.android.synthetic.main.item_lista.view.*

class ListaAdapter(private val context: Context,
                   private var beacons: MutableList<Beacon>): RecyclerView.Adapter<ListaAdapter.ListaViewHolder>()  {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_lista, p0, false)
        return ListaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return beacons.size
    }

    override fun onBindViewHolder(p0: ListaViewHolder, p1: Int) {
        p0.vincula(beacons[p1])
    }

    fun adiciona(beacon: Beacon) {
        var ehNovo = true

        for (beaconArmazenado in beacons) {
            if (beaconArmazenado.device.address == beacon.device.address) {
                Log.i("TESTE", "Loop " + beacon.uuid)
                ehNovo = false
                break
            }
        }

        if (ehNovo) {
            Log.i("TESTE", "Adiciona " + beacon.uuid)
            beacons.add(beacon)
            notifyDataSetChanged()
        }
    }

    fun limpaLista(){
        beacons.clear()
        notifyDataSetChanged()
    }

    class ListaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val infoMac = itemView.item_lista_mac
        private val infoUuid = itemView.item_lista_uuid
        private val infoMajor = itemView.item_lista_major
        private val infoMinor = itemView.item_lista_minor

        fun vincula(beacon: Beacon){
            infoMac.text = beacon.device.address

            if (beacon.ehIbeacon()){
                infoUuid.text = beacon.uuid
                infoMajor.text = beacon.major.toString()
                infoMinor.text = beacon.minor.toString()
            }else{
                infoUuid.text = "N/A"
                infoMajor.text = "N/A"
                infoMinor.text = "N/A"
            }
        }
    }
}