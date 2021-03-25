package com.mumumusuc.generate

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.badlogic.gdx.jnigen.NativeCodeGenerator

@CompileStatic
class GenerateJni extends DefaultTask {
    String sourceDir = project.file('src/main/java')
    String classpath = project.file('build/classes/java/main')
    String jniDir =  "${project.projectDir.path}/src/main/jni"

    @TaskAction
    void generate() {
        project.delete {
            println("delete jni/*")
            it.delete("$jniDir")
        }
        // Generate h/cpp files for JNI
        new NativeCodeGenerator(false).generate(sourceDir, classpath, jniDir)
    }
}
