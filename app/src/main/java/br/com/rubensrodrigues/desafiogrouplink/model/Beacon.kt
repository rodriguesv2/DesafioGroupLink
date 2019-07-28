package br.com.rubensrodrigues.desafiogrouplink.model

import java.nio.ByteBuffer
import java.util.*

class Beacon(private val scanRecord: ByteArray) {

    val uuid by lazy { parseUUID() }
    val major by lazy { parseMajor() }
    val minor by lazy { parseMinor() }

    private fun parseUUID(): String{
        val uuidByte = ByteArray(16)

        for (i in 0..15) {
            uuidByte[i] = scanRecord[i + 6]
        }

        return getUuidDeByteArray(uuidByte)
    }

    private fun getUuidDeByteArray(bytes: ByteArray): String {
        val byteBuffer = ByteBuffer.wrap(bytes)
        val high = byteBuffer.long
        val low = byteBuffer.long
        val uuid = UUID(high, low)
        return uuid.toString()
    }

    private fun parseMajor(): Int{
        val majorByte = ByteArray(2)

        for (i in 0..1) {
            majorByte[i] = scanRecord[i + 22]
        }

        return ByteBuffer.wrap(majorByte).char.toInt()
    }

    private fun parseMinor(): Int{
        val minorByte = ByteArray(2)

        for (i in 0..1) {
            minorByte[i] = scanRecord[i + 24]
        }

        return ByteBuffer.wrap(minorByte).char.toInt()
    }

}