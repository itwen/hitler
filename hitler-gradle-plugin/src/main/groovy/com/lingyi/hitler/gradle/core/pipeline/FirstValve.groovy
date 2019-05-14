package com.lingyi.hitler.gradle.core.pipeline

import com.android.utils.FileUtils
import com.lingyi.hitler.gradle.utils.FilePathUtil
import com.lingyi.hitler.gradle.utils.LogUtils
import org.apache.commons.codec.digest.DigestUtils

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class FirstValve implements Valve{

    public static final def DOT_CLASS = '.class'
    public static final def DOT_JAR = '.jar'
    public static final def DOT_ZIP = '.zip'

    private Valve next
    @Override
    Valve getNext() {
        return next
    }

    @Override
    void setNext(Valve valve) {
        this.next = valve
    }

    @Override
    void invoke(Handling handling) {
        processFile(handling,{String classPath,byte [] buff ->
            handling.classPath = classPath
            handling.bytes = buff
            getNext().invoke(handling)
            return handling.bytes
        })
    }

    @Override
    String name() {
        return "FirstValve"
    }

    public  void processFile(Handling handling, Closure callback){
        if (handling != null && handling.inputFile != null){
            if (handling.inputFile.isFile() && (handling.inputFile.name.endsWith(DOT_ZIP) || handling.inputFile.name.endsWith(DOT_JAR))){
                processJar(handling,callback)
            }else if (handling.inputFile.isDirectory()){
                processDirectory(handling,callback)
            }
        }
    }

    private  boolean processJar(Handling handling, Closure callback){
        boolean  hasProcess = false
        JarFile jf = new JarFile(handling.inputFile)
        Enumeration<JarEntry> jarEntrys = jf.entries()

        def buildTempFile = FilePathUtil.getHitlerBuildPath() + File.separator +handling.context.variant.name + File.separator + "jar"
        LogUtils.instance.log("build_temple_file:"+buildTempFile)
        File outFile = new File(buildTempFile)
        if (!outFile.exists()){
            LogUtils.instance.log("file not exsit")
            outFile.mkdirs()
        }

        outFile = new File(buildTempFile,handling.inputFile.name)
        outFile.createNewFile()

        JarOutputStream jos = new JarOutputStream(new FileOutputStream(outFile))

        while (jarEntrys.hasMoreElements()) {

            JarEntry jarEntry = jarEntrys.nextElement()
            ZipEntry zipEntry = new ZipEntry(jarEntry.getName())

            InputStream inputStream = jf.getInputStream(jarEntry)

            ByteArrayOutputStream output = new ByteArrayOutputStream()
            byte[] buffer = new byte[16384]
            int n = 0
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n)
            }
            buffer = output.toByteArray()

            buffer  = callback.call(jarEntry.name,buffer)

            jos.putNextEntry(zipEntry)
            jos.write(buffer)
            jos.closeEntry()
        }

        jos.close()
        jf.close()
        handling.inputFile = outFile
        return hasProcess
    }


    private  boolean processDirectory(Handling handling, Closure callback){
        def inputFile = handling.inputFile
        def context = handling.context
        def tempDirectory = FilePathUtil.getHitlerBuildPath() + File.separator + context.variant.name+ File.separator+ "Directory"

        File tempBuildFile = new File(tempDirectory)
        if (!tempBuildFile.exists()){
            tempBuildFile.mkdirs()
        }
        Files.walkFileTree(inputFile.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                def fileName = file.toFile().name
                def md5 = DigestUtils.md5Hex(file.toFile().absolutePath)
                def parent = file.toFile().getParentFile().getAbsolutePath()

                FileInputStream inputStream = new FileInputStream(file.toFile())
                File temp = new File(parent+File.separator+md5+fileName)
                if (!temp.exists()){
                    temp.createNewFile()
                }
                FileOutputStream fos = new FileOutputStream(temp)

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

                byte[] buffer = new byte[16384]
                int n = 0
                while (-1 != ( n = inputStream.read(buffer))) {
                    outputStream.write(buffer, 0, n)
                }

                buffer = outputStream.toByteArray()


                buffer = callback.call(file.toFile().absolutePath,buffer)

                fos.write(buffer)

                fos.flush()
                inputStream.close()
                fos.close()
                outputStream.close()

                file.toFile().delete()
                temp.renameTo(file.toFile().absolutePath)

                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return super.postVisitDirectory(dir, exc)
            }
        })

        FileUtils.copyDirectory(handling.inputFile,tempBuildFile)
        handling.inputFile = tempBuildFile
    }
}