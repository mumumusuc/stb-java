package com.mumumusuc.native

import com.mumumusuc.stb.StbImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

object NativeLoader {
    fun load(lib: String) {
        try {
            System.loadLibrary(lib)
        } catch (e: UnsatisfiedLinkError) {
            val clz = Class.forName(Throwable().stackTrace[1].className)
            val pkg = clz.getPackage().name
            val libName = resolveLibName(lib)
            val libPath = libName.run {
                Paths.get(pkg.replace('.', '/'))
                    .resolve("natives")
                    .resolve(OS.name)
                    .resolve(OS.arch)
                    .resolve(libName)
                    .toFile().path
            }
            val tmpPath = libName.run {
                Paths.get(pkg).resolve("${clz.hashCode()}")
                    .resolve("natives")
                    .resolve(OS.name)
                    .resolve(OS.arch)
                    .toFile().path
            }
            println("try extract `$libName` from `$libPath` to `$tmpPath`")
            try {
                StbImage::class.java.classLoader.getResourceAsStream(libPath).use {
                    checkNotNull(it) { "stb native lib not found in path `$libPath`" }
                    val tmpDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve(tmpPath)
                    tmpDir.toFile().mkdirs()
                    tmpDir.resolve(lib).run {
                        Files.copy(it, this, StandardCopyOption.REPLACE_EXISTING)
                        @Suppress("UnsafeDynamicallyLoadedCode")
                        System.load(toFile().absolutePath)
                    }
                }
            } catch (e: IOException) {
                throw UnsatisfiedLinkError("try load `$libName` failed.\n" + e.stackTraceToString())
            }
        }
    }

    private fun resolveLibName(lib: String): String =
        when {
            OS.isWindows -> "$lib.dll"
            OS.isMacOS -> "lib$lib.dylib"
            else -> "lib$lib.so"
        }
}

private object OS {
    private val name_ by lazy { System.getProperty("os.name").toLowerCase(Locale.ROOT) }
    private val arch_ by lazy { System.getProperty("os.arch").toLowerCase(Locale.ROOT) }

    val name get() = name_
    val arch
        get() = when {
            isX86 -> "x86"
            isX64 -> "x86_64"
            isArm -> "arm"
            else -> arch_
        }

    val isWindows = name_.contains("win")
    val isMacOS = name_.contains("mac")
    val isLinux = name_.contains("linux")

    val isX86 = arch_ == "x86"
    val isX64 = arch_.contains("64")
    val isArm = arch_.contains("arm")
}