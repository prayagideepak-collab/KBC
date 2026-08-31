package com.example.util

import com.example.data.model.QuestionItem

data class ResolvedQuestionContent(
    val questionText: String,
    val clues: List<String>,
    val options: List<String>,
    val hintText: String,
    val deductionPath: String,
    val eliminationReasons: List<String>,
    val expertAdvice: String,
    val fiftyFiftyProof: String,
    val languageMode: String
)

object LanguageResolver {

    fun validateLanguageMode(mode: String?): String {
        val normalized = mode?.uppercase()?.trim() ?: "HINDI"
        require(normalized in listOf("HINDI", "ENGLISH", "BILINGUAL")) {
            "Invalid or unauthorized language mode: '$mode'. Allowed values: HINDI, ENGLISH, BILINGUAL."
        }
        return normalized
    }

    fun resolve(question: QuestionItem, languageMode: String): ResolvedQuestionContent {
        val mode = validateLanguageMode(languageMode)

        return when (mode) {
            "HINDI" -> ResolvedQuestionContent(
                questionText = question.questionHindi.ifBlank { question.questionEnglish },
                clues = question.cluesHindi.ifEmpty { question.cluesEnglish },
                options = question.optionsHindi.ifEmpty { question.optionsEnglish },
                hintText = question.hintHindi.ifBlank { question.hintEnglish },
                deductionPath = question.deductionPathHindi.ifBlank { question.deductionPathEnglish },
                eliminationReasons = question.eliminationReasonsHindi.ifEmpty { question.eliminationReasonsEnglish },
                expertAdvice = question.expertAdviceHindi.ifBlank { question.expertAdviceEnglish },
                fiftyFiftyProof = question.fiftyFiftyProofHindi.ifBlank { question.fiftyFiftyProofEnglish },
                languageMode = mode
            )
            "ENGLISH" -> ResolvedQuestionContent(
                questionText = question.questionEnglish.ifBlank { question.questionHindi },
                clues = question.cluesEnglish.ifEmpty { question.cluesHindi },
                options = question.optionsEnglish.ifEmpty { question.optionsHindi },
                hintText = question.hintEnglish.ifBlank { question.hintHindi },
                deductionPath = question.deductionPathEnglish.ifBlank { question.deductionPathHindi },
                eliminationReasons = question.eliminationReasonsEnglish.ifEmpty { question.eliminationReasonsHindi },
                expertAdvice = question.expertAdviceEnglish.ifBlank { question.expertAdviceHindi },
                fiftyFiftyProof = question.fiftyFiftyProofEnglish.ifBlank { question.fiftyFiftyProofHindi },
                languageMode = mode
            )
            else -> { // BILINGUAL
                val qH = question.questionHindi
                val qE = question.questionEnglish
                val qCombined = if (qH.isNotBlank() && qE.isNotBlank()) "$qH\n$qE" else (qH + qE)

                val cluesCombined = question.cluesHindi.zip(question.cluesEnglish).map { "${it.first}\n${it.second}" }
                    .ifEmpty { question.cluesHindi + question.cluesEnglish }

                val optionsCombined = question.optionsHindi.zip(question.optionsEnglish).map { "${it.first} / ${it.second}" }
                    .ifEmpty { question.optionsHindi }

                val hintCombined = if (question.hintHindi.isNotBlank() && question.hintEnglish.isNotBlank()) "${question.hintHindi}\n${question.hintEnglish}" else (question.hintHindi + question.hintEnglish)

                val deductionCombined = if (question.deductionPathHindi.isNotBlank() && question.deductionPathEnglish.isNotBlank()) "${question.deductionPathHindi}\n${question.deductionPathEnglish}" else ""

                val elimCombined = question.eliminationReasonsHindi.zip(question.eliminationReasonsEnglish).map { "${it.first} / ${it.second}" }

                val expertCombined = if (question.expertAdviceHindi.isNotBlank() && question.expertAdviceEnglish.isNotBlank()) "${question.expertAdviceHindi}\n${question.expertAdviceEnglish}" else ""

                val proofCombined = if (question.fiftyFiftyProofHindi.isNotBlank() && question.fiftyFiftyProofEnglish.isNotBlank()) "${question.fiftyFiftyProofHindi}\n${question.fiftyFiftyProofEnglish}" else ""

                ResolvedQuestionContent(
                    questionText = qCombined,
                    clues = cluesCombined,
                    options = optionsCombined,
                    hintText = hintCombined,
                    deductionPath = deductionCombined,
                    eliminationReasons = elimCombined,
                    expertAdvice = expertCombined,
                    fiftyFiftyProof = proofCombined,
                    languageMode = mode
                )
            }
        }
    }

    fun validateQuestionContent(question: QuestionItem, languageMode: String): Boolean {
        val mode = validateLanguageMode(languageMode)
        return when (mode) {
            "HINDI" -> question.questionHindi.isNotBlank() && question.optionsHindi.size == 4
            "ENGLISH" -> question.questionEnglish.isNotBlank() && question.optionsEnglish.size == 4
            "BILINGUAL" -> question.questionHindi.isNotBlank() && question.questionEnglish.isNotBlank() &&
                    question.optionsHindi.size == 4 && question.optionsEnglish.size == 4 &&
                    question.correctAnswerIndex in 0..3
            else -> false
        }
    }
}
