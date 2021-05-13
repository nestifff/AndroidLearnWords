package com.example.learnenglishwordssecondtry.model

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.*

object WordsInFromFile {

    // Manifest.permission.WRITE_EXTERNAL_STORAGE
    // Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun pushWordsInFile(words: List<Word>) {

        try {
            val rootPath: String = Environment.getExternalStorageDirectory()
                    .absolutePath.toString() + "/Documents/"

            val f = File(rootPath + "pushedWords.txt")
            f.createNewFile()
            val out = FileOutputStream(f)

            for (word: Word in words) {
                out.write((word.wordRus + ", " + word.wordEng + "\n").toByteArray())
            }

            out.flush()
            out.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun pullWordsFromFile(): List<Word> {

        val words = mutableListOf<Word>()
        val rootPath: String = Environment.getExternalStorageDirectory()
                .absolutePath.toString() + "/Documents/"

        val file = File(rootPath + "pullWords.txt")

        try {

            var rus: String?
            var eng: String?
            val br = BufferedReader(FileReader(file))
            var line: String?

            while (br.readLine().also { line = it } != null) {
                rus = line?.let {it.split(",".toRegex(), 0)[0].trim()}
                eng = line?.let {it.split(",".toRegex(), 0)[1].trim()}
                words.add(Word(eng, rus))
            }
            br.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return words
    }


    private fun writeAllWords(words: List<Word>, out: FileOutputStream) {

        for (word: Word in words) {
            out.write((word.wordRus + ", " + word.wordEng + "\n").toByteArray())
        }

        out.flush()
        out.close()
    }

    private suspend fun askPermission(
            permission: String,
            activity: Activity): Boolean {

        if (ContextCompat.checkSelfPermission(activity, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "permission \"$permission\" requested", Toast.LENGTH_SHORT).show()

            coroutineScope {
                launch {
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), 1)
                }
            }

            return ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED

        }
        return true
    }
}