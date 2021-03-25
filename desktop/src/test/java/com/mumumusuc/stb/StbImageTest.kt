package com.mumumusuc.stb

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

private const val kImage = "test.png"
private const val kWidth = 512
private const val kHeight = 512
private const val kChannel = 4
private const val kImgSize = 1048576

class StbImageTest {
    @Test
    fun testSetUnpremultiplyOnLoad() {
        stb.setUnpremultiplyOnLoad(true)
        assertTrue(true)
    }

    @Test
    fun testConvertIphonePngToRgb() {
        stb.convertIphonePngToRgb(true)
        assertTrue(true)
    }

    @Test
    fun testSetFlipVerticallyOnLoad() {
        stb.setFlipVerticallyOnLoad(true)
        assertTrue(true)
    }

    @Test
    fun testInfo() {
        val d = Files.newInputStream(Paths.get(kImage))
        assertDoesNotThrow {
            stb.info(d).also {
                assertEquals(kWidth, it.width)
                assertEquals(kHeight, it.height)
                assertEquals(kChannel, it.channel.ordinal)
            }
        }
    }

    @Test
    fun testInfoFromMemory() {
        val d = Files.newInputStream(Paths.get(kImage)).use {
            ByteBuffer.allocateDirect(it.available()).apply {
                put(ByteArray(it.available()).apply { it.read(this) })
            }
        }.rewind()
        assertTrue(d.isDirect)
        assertDoesNotThrow {
            stb.infoFromMemory(d).also {
                assertEquals(kWidth, it.width)
                assertEquals(kHeight, it.height)
                assertEquals(kChannel, it.channel.ordinal)
            }
        }
    }

    @Test
    fun testInfoFromCallbacks() {
        val d = Files.newInputStream(Paths.get(kImage))
        assertDoesNotThrow {
            StbImage.infoFromCallbacks(object : StbIOCallbacks {
                override fun read(data: ByteBuffer): Int {
                    val size = data.limit()
                    val buff = ByteArray(size)
                    val len = d.read(buff)
                    data.put(buff)
                    return len
                }

                override fun skip(n: Int) {
                    d.skip(n.toLong())
                }

                override fun eof(): Int {
                    d.close()
                    return 1
                }
            }).also {
                assertEquals(kWidth, it.width)
                assertEquals(kHeight, it.height)
                assertEquals(kChannel, it.channel.ordinal)
            }
        }
    }

    @Test
    fun testIs16BitFromMemory() {
        val d = Files.newInputStream(Paths.get(kImage)).use {
            ByteBuffer.allocateDirect(it.available()).apply {
                put(ByteArray(it.available()).apply { it.read(this) })
            }
        }.rewind()
        assertTrue(d.isDirect)
        assertDoesNotThrow {
            val i = stb.is16BitFromMemory(d)
            assertFalse(i)
        }
    }

    @Test
    fun testIs16BitFromCallbacks() {
        val d = Files.newInputStream(Paths.get(kImage))
        assertDoesNotThrow {
            val i = StbImage.is16BitFromCallbacks(object : StbIOCallbacks {
                override fun read(data: ByteBuffer): Int {
                    val size = data.limit()
                    val buff = ByteArray(size)
                    val len = d.read(buff)
                    data.put(buff)
                    return len
                }

                override fun skip(n: Int) {
                    d.skip(n.toLong())
                }

                override fun eof(): Int {
                    d.close()
                    return 1
                }
            })
            assertFalse(i)
        }
    }

    @Test
    fun testLoad() {
        val d = Files.newInputStream(Paths.get(kImage))
        assertDoesNotThrow {
            val i = stb.load(d)
            assertEquals(kImgSize, i.buffer.capacity())
            assertEquals(kWidth, i.info.width)
            assertEquals(kHeight, i.info.height)
            assertEquals(kChannel, i.info.channel.ordinal)
        }
    }

    @Test
    fun testLoadFromMemory() {
        val d = Files.newInputStream(Paths.get(kImage)).use {
            ByteBuffer.allocateDirect(it.available()).apply {
                put(ByteArray(it.available()).apply { it.read(this) })
            }
        }.rewind()
        assertTrue(d.isDirect)
        assertDoesNotThrow {
            val i = stb.loadFromMemory(d)
            assertEquals(kImgSize, i.buffer.capacity())
            assertEquals(kWidth, i.info.width)
            assertEquals(kHeight, i.info.height)
            assertEquals(kChannel, i.info.channel.ordinal)
        }
    }

    @Test
    fun testLoadFromCallbacks() {
        val d = Files.newInputStream(Paths.get(kImage))
        assertDoesNotThrow {
            val i = StbImage.loadFromCallback(object : StbIOCallbacks {
                override fun read(data: ByteBuffer): Int {
                    val size = data.limit()
                    val buff = ByteArray(size)
                    val len = d.read(buff)
                    data.put(buff)
                    return len
                }

                override fun skip(n: Int) {
                    d.skip(n.toLong())
                }

                override fun eof(): Int {
                    d.close()
                    return 1
                }
            })
            assertEquals(kImgSize, i.buffer.capacity())
            assertEquals(kWidth, i.info.width)
            assertEquals(kHeight, i.info.height)
            assertEquals(kChannel, i.info.channel.ordinal)
        }
    }
}
