package com.mumumusuc.stb

import com.mumumusuc.native.NativeLoader
import java.io.InputStream
import java.lang.RuntimeException
import java.nio.Buffer
import java.nio.ByteBuffer

typealias stb = StbImage

private const val LARGE_FILE = 4 * 1024 * 1024 // 4MB

object StbImage {
    // helper
    private fun Boolean.toInt() = if (this) 1 else 0
    private fun Int.toBoolean() = this > 0

    data class Info(val width: Int, val height: Int, val channel: Channel)

    data class Data(val buffer: Buffer, val info: Info) {
        internal constructor(b: Buffer, x: Int, y: Int, c: Channel) : this(b, Info(x, y, c))
    }

    enum class Channel {
        DEFAULT,
        GREY,
        GREY_ALPHA,
        RGB,
        RGBA
    }

    private class IOCallbackDelegate(private val mStream: InputStream) : StbIOCallbacks {
        override fun read(data: ByteBuffer): Int {
            val buffer = ByteArray(data.limit())
            val size = mStream.read(buffer)
            data.put(buffer)
            return size
        }

        override fun skip(n: Int) {
            mStream.skip(n.toLong())
        }

        override fun eof(): Int {
            mStream.close()
            return 1
        }
    }

    // info
    fun info(stream: InputStream): Info = if (stream.available() < LARGE_FILE) {
        val buff = ByteBuffer.allocateDirect(stream.available()).apply {
            put(stream.use { ByteArray(stream.available()).apply { it.read(this) } })
        }.rewind()
        infoFromMemory(buff)
    } else {
        infoFromCallbacks(IOCallbackDelegate(stream))
    }

    fun infoFromMemory(buffer: Buffer): Info {
        require(buffer.isDirect) { "require direct buffer" }
        val x = IntArray(1)
        val y = IntArray(1)
        val c = IntArray(1)
        return StbImageNative.nInfoFromMemory(buffer, buffer.limit(), x, y, c).let {
            if (it != 1) {
                throw RuntimeException(StbImageNative.nFailureReason())
            }
            Info(x[0], y[0], Channel.values()[c[0]])
        }
    }

    fun infoFromCallbacks(cb: StbIOCallbacks): Info {
        val x = IntArray(1)
        val y = IntArray(1)
        val c = IntArray(1)
        return StbImageNative.nInfoFromCallbacks(cb, x, y, c).let {
            if (it != 1) {
                throw RuntimeException(StbImageNative.nFailureReason())
            }
            Info(x[0], y[0], Channel.values()[c[0]])
        }
    }

    fun is16BitFromMemory(buffer: Buffer): Boolean {
        require(buffer.isDirect) { "require direct buffer" }
        return StbImageNative.nIs16BitFromMemory(buffer, buffer.limit()).toBoolean()
    }

    fun is16BitFromCallbacks(cb: StbIOCallbacks): Boolean {
        return StbImageNative.nIs16BitFromCallbacks(cb).toBoolean()
    }

    // config
    fun setUnpremultiplyOnLoad(unpremultiply: Boolean) {
        StbImageNative.nSetUnpremultiplyOnLoad(unpremultiply.toInt())
    }

    fun convertIphonePngToRgb(convert: Boolean) {
        StbImageNative.nConvertIphonePngToRgb(convert.toInt())
    }

    fun setFlipVerticallyOnLoad(flip: Boolean) {
        StbImageNative.nSetFlipVerticallyOnLoad(flip.toInt())
    }

    // 8bpc
    fun load(stream: InputStream, desiredChannel: Channel = Channel.DEFAULT): Data =
        if (stream.available() < LARGE_FILE) {
            val buff = ByteBuffer.allocateDirect(stream.available()).apply {
                put(stream.use { ByteArray(stream.available()).apply { it.read(this) } })
            }.rewind()
            loadFromMemory(buff, desiredChannel)
        } else {
            loadFromCallback(IOCallbackDelegate(stream))
        }

    fun loadFromMemory(buffer: Buffer, desiredChannel: Channel = Channel.DEFAULT): Data {
        require(buffer.isDirect) { "require direct buffer" }
        val x = IntArray(1)
        val y = IntArray(1)
        val c = IntArray(1)
        val b = StbImageNative.nLoadFromMemory(buffer, buffer.limit(), x, y, c, desiredChannel.ordinal).also {
            if (it.capacity() == 0) {
                throw RuntimeException(StbImageNative.nFailureReason())
            }
        }
        return Data(b, x[0], y[0], Channel.values()[c[0]])
    }

    fun loadFromCallback(cb: StbIOCallbacks, desiredChannel: Channel = Channel.DEFAULT): Data {
        val x = IntArray(1)
        val y = IntArray(1)
        val c = IntArray(1)
        val b = StbImageNative.nLoadFromCallback(cb, x, y, c, desiredChannel.ordinal).also {
            if (it.capacity() == 0) {
                throw RuntimeException(StbImageNative.nFailureReason())
            }
        }
        return Data(b, x[0], y[0], Channel.values()[c[0]])
    }

    // 16bpc
    fun load16FromMemory() {
        TODO("not implemented yet")
    }

    fun load16FromCallback() {
        TODO("not implemented yet")
    }

    fun load16() {
        TODO("not implemented yet")
    }

    // float
    fun loadFFromMemory() {
        TODO("not implemented yet")
    }

    fun loadFFromCallback() {
        TODO("not implemented yet")
    }

    fun loadF() {
        TODO("not implemented yet")
    }

    init {
        NativeLoader.load("stb")
        StbImageNative.nInit()
    }
}