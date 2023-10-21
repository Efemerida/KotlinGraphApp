package com.example.graphonkotlin.ui.services

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.example.graphonkotlin.ui.entities.Graph
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.Objects

class FIleService(val context: Context) {
    fun loadGraph(uri: Uri, context: Context): String {
        return readTextFromUri(uri, context)
    }

    fun saveGraph(graph: Graph, path: Uri) {
        val stringBuilder = StringBuilder()

        for (edge in graph.edgeList) {
            stringBuilder.append(edge.vertex1?.number)
                .append(" ")
                .append(edge.vertex2?.number)
                .append(" ")
                .append(edge.weight)
                .append("\n")
        }

        val graphString = stringBuilder.toString()

            val pfd: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(path, "w")
            val fileOutputStream: FileOutputStream = FileOutputStream(pfd?.fileDescriptor)
            fileOutputStream.write(graphString.toByteArray())
            fileOutputStream.close()
            pfd?.close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun readTextFromUri(uri: Uri, context: Context): String {
        val stringBuilder = StringBuilder()
        context.contentResolver.openInputStream(uri).use { inputStream ->
            BufferedReader(
                InputStreamReader(Objects.requireNonNull(inputStream))
            ).use { reader ->
                var line: String
                while (true) {
                    var line: String? = reader.readLine() ?: break;
                    stringBuilder.append(line)
                    stringBuilder.append("\n")
                }
            }
        }
        return stringBuilder.toString()
    }
}