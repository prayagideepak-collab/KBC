package com.example.data.model

import org.json.JSONArray
import org.json.JSONObject

object QuestionSerializer {

    fun serializeQuestion(q: QuestionItem): JSONObject {
        return JSONObject().apply {
            put("id", q.id)
            put("qNumber", q.qNumber)
            put("difficultyLevel", q.difficultyLevel)
            put("difficultyTitle", q.difficultyTitle)
            if (q.timeLimitSeconds != null) {
                put("timeLimitSeconds", q.timeLimitSeconds)
            } else {
                put("timeLimitSeconds", JSONObject.NULL)
            }
            put("prizePoints", q.prizePoints)
            put("prizeFormatted", q.prizeFormatted)
            put("isCheckpoint", q.isCheckpoint)
            put("checkpointTitle", q.checkpointTitle ?: "")
            put("category", q.category)
            put("questionHindi", q.questionHindi)
            put("questionEnglish", q.questionEnglish)
            put("cluesHindi", JSONArray(q.cluesHindi))
            put("cluesEnglish", JSONArray(q.cluesEnglish))
            put("optionsHindi", JSONArray(q.optionsHindi))
            put("optionsEnglish", JSONArray(q.optionsEnglish))
            put("correctAnswerIndex", q.correctAnswerIndex)
            put("deductionPathHindi", q.deductionPathHindi)
            put("deductionPathEnglish", q.deductionPathEnglish)
            put("eliminationReasonsHindi", JSONArray(q.eliminationReasonsHindi))
            put("eliminationReasonsEnglish", JSONArray(q.eliminationReasonsEnglish))
            put("hintHindi", q.hintHindi)
            put("hintEnglish", q.hintEnglish)
            put("expertAdviceHindi", q.expertAdviceHindi)
            put("expertAdviceEnglish", q.expertAdviceEnglish)
            put("fiftyFiftyDiscardIndices", JSONArray(q.fiftyFiftyDiscardIndices))
            put("fiftyFiftyProofHindi", q.fiftyFiftyProofHindi)
            put("fiftyFiftyProofEnglish", q.fiftyFiftyProofEnglish)
            put("diagramType", q.diagramType)
            put("diagramData", q.diagramData)
            if (q.audioPatternType != null) {
                put("audioPatternType", q.audioPatternType)
            }
            put("semanticFingerprint", q.semanticFingerprint)
            put("isFlippedOrUsed", q.isFlippedOrUsed)
        }
    }

    fun deserializeQuestion(obj: JSONObject): QuestionItem {
        val timeLimit = if (obj.has("timeLimitSeconds") && !obj.isNull("timeLimitSeconds")) {
            obj.getInt("timeLimitSeconds")
        } else null

        val cluesH = mutableListOf<String>()
        val cluesE = mutableListOf<String>()
        val optsH = mutableListOf<String>()
        val optsE = mutableListOf<String>()
        val elimH = mutableListOf<String>()
        val elimE = mutableListOf<String>()
        val discards = mutableListOf<Int>()

        obj.optJSONArray("cluesHindi")?.let { arr ->
            for (i in 0 until arr.length()) cluesH.add(arr.optString(i))
        }
        obj.optJSONArray("cluesEnglish")?.let { arr ->
            for (i in 0 until arr.length()) cluesE.add(arr.optString(i))
        }
        obj.optJSONArray("optionsHindi")?.let { arr ->
            for (i in 0 until arr.length()) optsH.add(arr.optString(i))
        }
        obj.optJSONArray("optionsEnglish")?.let { arr ->
            for (i in 0 until arr.length()) optsE.add(arr.optString(i))
        }
        obj.optJSONArray("eliminationReasonsHindi")?.let { arr ->
            for (i in 0 until arr.length()) elimH.add(arr.optString(i))
        }
        obj.optJSONArray("eliminationReasonsEnglish")?.let { arr ->
            for (i in 0 until arr.length()) elimE.add(arr.optString(i))
        }
        obj.optJSONArray("fiftyFiftyDiscardIndices")?.let { arr ->
            for (i in 0 until arr.length()) discards.add(arr.optInt(i))
        }

        return QuestionItem(
            id = obj.optString("id", java.util.UUID.randomUUID().toString()),
            qNumber = obj.optInt("qNumber", 1),
            difficultyLevel = obj.optInt("difficultyLevel", obj.optInt("qNumber", 1)),
            difficultyTitle = obj.optString("difficultyTitle", "Standard"),
            timeLimitSeconds = timeLimit,
            prizePoints = obj.optLong("prizePoints", 1000L),
            prizeFormatted = obj.optString("prizeFormatted", "₹1,000"),
            isCheckpoint = obj.optBoolean("isCheckpoint", false),
            checkpointTitle = if (obj.has("checkpointTitle") && obj.getString("checkpointTitle").isNotBlank()) obj.getString("checkpointTitle") else null,
            category = obj.optString("category", "Logic"),
            questionHindi = obj.optString("questionHindi"),
            questionEnglish = obj.optString("questionEnglish"),
            cluesHindi = cluesH,
            cluesEnglish = cluesE,
            optionsHindi = optsH,
            optionsEnglish = optsE,
            correctAnswerIndex = obj.optInt("correctAnswerIndex", 0),
            deductionPathHindi = obj.optString("deductionPathHindi"),
            deductionPathEnglish = obj.optString("deductionPathEnglish"),
            eliminationReasonsHindi = elimH,
            eliminationReasonsEnglish = elimE,
            hintHindi = obj.optString("hintHindi"),
            hintEnglish = obj.optString("hintEnglish"),
            expertAdviceHindi = obj.optString("expertAdviceHindi"),
            expertAdviceEnglish = obj.optString("expertAdviceEnglish"),
            fiftyFiftyDiscardIndices = discards,
            fiftyFiftyProofHindi = obj.optString("fiftyFiftyProofHindi"),
            fiftyFiftyProofEnglish = obj.optString("fiftyFiftyProofEnglish"),
            diagramType = obj.optString("diagramType", "none"),
            diagramData = obj.optString("diagramData", ""),
            audioPatternType = if (obj.has("audioPatternType") && !obj.isNull("audioPatternType")) obj.getString("audioPatternType") else null,
            semanticFingerprint = obj.optString("semanticFingerprint", ""),
            isFlippedOrUsed = obj.optBoolean("isFlippedOrUsed", false)
        )
    }

    fun serializeQuestionList(list: List<QuestionItem>): String {
        val array = JSONArray()
        list.forEach { array.put(serializeQuestion(it)) }
        return array.toString()
    }

    fun deserializeQuestionList(json: String): List<QuestionItem> {
        val result = mutableListOf<QuestionItem>()
        if (json.isBlank()) return result
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i) ?: continue
                result.add(deserializeQuestion(obj))
            }
        } catch (_: Exception) {}
        return result
    }
}
