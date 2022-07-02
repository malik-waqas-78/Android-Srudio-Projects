package com.smartswitch.phoneclone.utills

import android.os.Environment
import android.os.StatFs
import java.io.File


class CAPPMemoryStatus {


    companion object {
        val ERROR = -1
        fun externalMemoryAvailable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        fun getAvailableInternalMemorySize(): Long {
            val path: File = Environment.getDataDirectory()
            val stat = StatFs(path.getPath())
            val blockSize = stat.blockSize.toLong()
            val availableBlocks = stat.availableBlocks.toLong()
            return availableBlocks * blockSize
        }

        fun getTotalInternalMemorySize(): Long {
            val path: File = Environment.getDataDirectory()
            val stat = StatFs(path.getPath())
            val blockSize = stat.blockSize.toLong()
            val totalBlocks = stat.blockCount.toLong()
            return totalBlocks * blockSize
        }

        fun getAvailableExternalMemorySize(): Double {
            return if (externalMemoryAvailable()) {
                val path: File = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.getPath())
                val blockSize = stat.blockSize.toDouble()
                val availableBlocks = stat.availableBlocks.toDouble()
                availableBlocks * blockSize
            } else {
                ERROR.toDouble()
            }
        }

        fun getTotalExternalMemorySize(): Double{
            return if (externalMemoryAvailable()) {
                val path: File = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.getPath())
                val blockSize = stat.blockSize.toDouble()
                val totalBlocks = stat.blockCount.toLong()
                totalBlocks * blockSize
            } else {
                ERROR.toDouble()
            }
        }

        fun formatSize(size: Long): String? {
            var size = size
            var suffix: String? = null
            if (size >= 1024) {
                suffix = "KiB"
                size /= 1024
                if (size >= 1024) {
                    suffix = "MiB"
                    size /= 1024
                }
            }
            val resultBuffer = StringBuilder(java.lang.Long.toString(size))
            var commaOffset = resultBuffer.length - 3
            while (commaOffset > 0) {
                resultBuffer.insert(commaOffset, ',')
                commaOffset -= 3
            }
            if (suffix != null) resultBuffer.append(suffix)
            return resultBuffer.toString()
        }
    }
}