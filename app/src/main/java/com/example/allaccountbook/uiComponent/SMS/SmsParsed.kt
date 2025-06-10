
package com.example.allaccountbook.uiComponent.SMS

import com.example.allaccountbook.database.entity.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

data class SmsParsed(
    val name: String,
    val price: Int,
    val date: String,
    val place: String
)

fun parseSmsMessage(msg: String): SmsParsed? {
    // 1. 삼성카드
    val samsungRegex = Regex("""승인\s+([^\n]+)\n([0-9,]+)원[^\n]*\n(\d{2}/\d{2} \d{2}:\d{2})\s+([^\n]+)""")
    samsungRegex.find(msg)?.let { m ->
        return SmsParsed(
            name = m.groupValues[1].trim(),
            price = m.groupValues[2].replace(",", "").toInt(),
            date = m.groupValues[3],
            place = m.groupValues[4].trim()
        )
    }

    // 2. KB국민
    val kbMultiLine = Regex(
        """KB국민체크\([^\)]+\)\n([^\n]+)\n(\d{2}/\d{2} \d{2}:\d{2})\n([0-9,]+)원\n([^\n]+) 사용"""
    )
    kbMultiLine.find(msg)?.let { m ->
        return SmsParsed(
            name = m.groupValues[1].trim(),
            price = m.groupValues[3].replace(",", "").toInt(),
            date = m.groupValues[2],
            place = m.groupValues[4].trim()
        )
    }

    // 3. KB국민체크
    val kbSimple = Regex(
        """([^\n]+)\n(\d{2}/\d{2} \d{2}:\d{2})\n([0-9,]+)원\n([^\n]+) 사용"""
    )
    kbSimple.find(msg)?.let { m ->
        return SmsParsed(
            name = m.groupValues[1].trim(),
            price = m.groupValues[3].replace(",", "").toInt(),
            date = m.groupValues[2],
            place = m.groupValues[4].trim()
        )
    }

    return null
}

// -> ExpenseEntity
fun smsParsedToExpenseEntity(parsed: SmsParsed, transactionId: Int = 0, category: String): ExpenseEntity {

    val thisYear = Calendar.getInstance().get(Calendar.YEAR)
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA)
    val fullDateStr = "$thisYear/${parsed.date}"
    val dateObj = try { sdf.parse(fullDateStr) } catch (e: Exception) { Date() }
    return ExpenseEntity(
        transactionId = transactionId,
        price = parsed.price,
        name = parsed.place,
        date = dateObj,
        category = category
    )
}
