package com.example.gpfaqchatbot.util

import android.content.Context
import com.example.gpfaqchatbot.FAQ
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVParserBuilder



fun readFAQsFromCSV(context: Context, fileName: String): List<FAQ> {
    val faqs = mutableListOf<FAQ>()
    val assetManager = context.assets
    assetManager.open(fileName).bufferedReader().use { reader ->
        val csvReader = CSVReaderBuilder(reader).withCSVParser(CSVParserBuilder().withSeparator(',').withIgnoreQuotations(false).build()).build()
        csvReader.forEach { line ->
            if (line.size == 2) {
                faqs.add(FAQ(line[0].trim().removeSurrounding("\""), line[1].trim().removeSurrounding("\"")))
            } else {
                println("Malformed line: ${line.joinToString(",")}")
            }
        }
    }
    return faqs
//    val faqs = mutableListOf<FAQ>()
//    val inputStream = context.assets.open(fileName)
//    inputStream.bufferedReader().useLines { lines ->
//        lines.drop(1).forEach { line ->
//            println("Processing line: $line") // Log each line being processed
//            val parts = line.split(",")
//            if (parts.size == 2) {
//                val question = parts[0].trim()
//                val answer = parts[1].trim()
//                faqs.add(FAQ(question, answer))
//                println("Loaded FAQ: $question -> $answer")
//            } else {
//                println("Skipped malformed line: $line") // Log skipped lines
//            }
//        }
//    }
//    println("Total FAQs loaded: ${faqs.size}")
//    return faqs
//    inputStream.bufferedReader().useLines { lines ->
//        lines.drop(1).forEach { line ->
//            val parts = line.split(",")
//            if (parts.size == 2) {
//                faqs.add(FAQ(parts[0].trim(), parts[1].trim()))
//            }
//        }
//    }
//    return faqs
}

