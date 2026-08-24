package com.example.data.repository

import com.example.data.model.LifelineState
import com.example.data.model.QuestionItem
import com.example.data.model.UserProfile

enum class MistakeType(
    val titleHindi: String,
    val titleEnglish: String,
    val descriptionHindi: String,
    val descriptionEnglish: String
) {
    MISREAD_CLUE(
        titleHindi = "सुराग की अनदेखी (Misread Clue)",
        titleEnglish = "Misread Clue / Incomplete Constraint",
        descriptionHindi = "दिए गए प्रत्यक्ष सुराग में उल्लिखित मुख्य शर्त को नजरअंदाज किया गया।",
        descriptionEnglish = "A key condition clearly stated in the prompt clues was overlooked."
    ),
    CALCULATION_ERROR(
        titleHindi = "गणना त्रुटि (Calculation Error)",
        titleEnglish = "Calculation / Arithmetic Inaccuracy",
        descriptionHindi = "सूत्र या मान प्रतिस्थापन के दौरान गणितीय त्रुटि हुई।",
        descriptionEnglish = "An arithmetic or substitution error occurred during value computation."
    ),
    PREMATURE_ASSUMPTION(
        titleHindi = "अपरिपक्व अनुमान (Premature Assumption)",
        titleEnglish = "Premature Assumption / Unwarranted Inference",
        descriptionHindi = "बिना पूर्ण सत्यापन के किसी एक संभावना को अंतिम मान लिया गया।",
        descriptionEnglish = "An unverified possibility was assumed as fact without testing counter-examples."
    ),
    INCORRECT_ELIMINATION(
        titleHindi = "गलत विलोपन (Incorrect Elimination)",
        titleEnglish = "Flawed Option Elimination",
        descriptionHindi = "मान्य विकल्प को खारिज कर दिया गया अथवा अमान्य को चुन लिया गया।",
        descriptionEnglish = "A logically sound candidate was prematurely discarded."
    ),
    PATTERN_MISIDENTIFICATION(
        titleHindi = "पैटर्न भ्रम (Pattern Misidentification)",
        titleEnglish = "Pattern / Structural Misidentification",
        descriptionHindi = "श्रृंखला या संरचना के अंतर्निहित नियम की गलत व्याख्या की गई।",
        descriptionEnglish = "The core underlying governing rule of the progression was misidentified."
    ),
    SEQUENCE_ERROR(
        titleHindi = "अनुक्रम त्रुटि (Sequence / Order Error)",
        titleEnglish = "Sequence / Precedence Violation",
        descriptionHindi = "शर्तों को लागू करने का सही क्रम उलट दिया गया।",
        descriptionEnglish = "The required order of operations or temporal sequence was reversed."
    ),
    LOGICAL_FALLACY(
        titleHindi = "तार्किक हेत्वाभास (Logical Fallacy)",
        titleEnglish = "Logical Fallacy (Formal / Informal)",
        descriptionHindi = "कथन और निष्कर्ष के बीच तार्किक संबंध में विरोधाभास उत्पन्न हुआ।",
        descriptionEnglish = "An invalid logical deduction violated the law of non-contradiction."
    ),
    SPATIAL_DIRECTION_ERROR(
        titleHindi = "दिशा / स्थानिक त्रुटि (Spatial Error)",
        titleEnglish = "Spatial / Vector Orientation Error",
        descriptionHindi = "दिशा परिवर्तन (दाएं/बाएं) या अंतिम स्थिति की गणना में विचलन हुआ।",
        descriptionEnglish = "A turn direction or vector displacement calculation drifted from the correct path."
    ),
    CONTEXT_ERROR(
        titleHindi = "तथ्यात्मक समझ त्रुटि (Context Error)",
        titleEnglish = "Contextual Interpretation Error",
        descriptionHindi = "प्रश्न के संदर्भ या तकनीकी शब्द की व्याख्या में त्रुटि हुई।",
        descriptionEnglish = "A domain-specific or contextual relationship was misinterpreted."
    )
}

data class MistakeAnalysis(
    val mistakeType: MistakeType,
    val fallacyName: String?,
    val mistakeSummaryHindi: String,
    val mistakeSummaryEnglish: String,
    val failedStepNumber: Int,
    val violatedClueNumber: Int,
    val chosenOptionLetter: String,
    val chosenOptionTextHindi: String,
    val chosenOptionTextEnglish: String,
    val correctOptionLetter: String,
    val correctOptionTextHindi: String,
    val correctOptionTextEnglish: String,
    val lifelineContextHindi: String? = null,
    val lifelineContextEnglish: String? = null
)

data class SolutionStep(
    val stepNumber: Int,
    val titleHindi: String,
    val titleEnglish: String,
    val detailHindi: String,
    val detailEnglish: String,
    val isUserFailurePoint: Boolean = false,
    val isEliminationStep: Boolean = false,
    val isValidationStep: Boolean = false
)

data class FullSolutionModel(
    val mistakeAnalysis: MistakeAnalysis,
    val steps: List<SolutionStep>,
    val finalConclusionHindi: String,
    val finalConclusionEnglish: String,
    val teacherVoiceScriptHindi: String,
    val teacherVoiceScriptEnglish: String,
    val visualDiagramType: String,
    val visualDiagramData: String,
    val isJuniorMode: Boolean
)

object SolutionExplanationEngine {

    fun generateWrongAnswerAnalysis(
        question: QuestionItem,
        selectedOptionIndex: Int,
        userProfile: UserProfile,
        lifelineState: LifelineState
    ): FullSolutionModel {
        val isJunior = userProfile.preparationDomain.contains("Student", true) || userProfile.isStudentMode || userProfile.age < 18
        val chosenLetter = when (selectedOptionIndex) {
            0 -> "A"; 1 -> "B"; 2 -> "C"; else -> "D"
        }
        val correctLetter = when (question.correctAnswerIndex) {
            0 -> "A"; 1 -> "B"; 2 -> "C"; else -> "D"
        }

        val chosenTextHi = question.optionsHindi.getOrElse(selectedOptionIndex) { "विकल्प $chosenLetter" }
        val chosenTextEn = question.optionsEnglish.getOrElse(selectedOptionIndex) { "Option $chosenLetter" }
        val correctTextHi = question.optionsHindi.getOrElse(question.correctAnswerIndex) { "विकल्प $correctLetter" }
        val correctTextEn = question.optionsEnglish.getOrElse(question.correctAnswerIndex) { "Option $correctLetter" }

        val elimReasonHi = question.eliminationReasonsHindi.getOrElse(selectedOptionIndex) { "यह विकल्प दी गई शर्तों को पूरा नहीं करता।" }
        val elimReasonEn = question.eliminationReasonsEnglish.getOrElse(selectedOptionIndex) { "This option violates the stated conditions." }

        // 1. Internal Mistake Classification
        val cat = question.category.lowercase()
        val mistakeType = when {
            cat.contains("spatial") || cat.contains("shadow") || cat.contains("direction") || cat.contains("vector") -> MistakeType.SPATIAL_DIRECTION_ERROR
            cat.contains("math") || cat.contains("speed") || cat.contains("ratio") || cat.contains("numeric") || cat.contains("calendar") || cat.contains("clock") -> MistakeType.CALCULATION_ERROR
            cat.contains("sequence") || cat.contains("chronology") || cat.contains("order") || cat.contains("arrangement") -> MistakeType.SEQUENCE_ERROR
            cat.contains("syllogism") || cat.contains("knights") || cat.contains("truth") || cat.contains("deduction") -> MistakeType.LOGICAL_FALLACY
            cat.contains("pattern") || cat.contains("rhythm") || cat.contains("coding") -> MistakeType.PATTERN_MISIDENTIFICATION
            cat.contains("current") || cat.contains("affairs") || cat.contains("science") -> MistakeType.MISREAD_CLUE
            else -> MistakeType.PREMATURE_ASSUMPTION
        }

        val fallacyName = when (mistakeType) {
            MistakeType.LOGICAL_FALLACY -> if (cat.contains("syllogism")) "हेत्वाभास: अनियंत्रित मध्य पद (Undistributed Middle)" else "विरोधाभासी अनुमान (Affirming Contradiction)"
            MistakeType.PREMATURE_ASSUMPTION -> "असमर्थित सामान्यीकरण (Hasty Generalization)"
            MistakeType.SEQUENCE_ERROR -> "क्रम विचलन (Temporal Inversion Trap)"
            else -> null
        }

        // 2. Lifeline Context
        val lifelineContextHi = when {
            lifelineState.is5050UsedInCurrentQ -> "आपने 50-50 लाइफलाइन का उपयोग किया था जिससे 2 गलत विकल्प हट गए थे। शेष 2 विकल्पों में से सुराग 2 और 3 को सख्ती से लागू करने पर आपका चुना हुआ विकल्प $chosenLetter खारिज हो जाता था।"
            lifelineState.isExpertUsedInCurrentQ -> "तर्क गुरु ने जिस मुख्य सुराग पर ध्यान केंद्रित करने की सलाह दी थी, उस शर्त को पूरा न करने के कारण विकल्प $chosenLetter असत्य सिद्ध होता है।"
            else -> null
        }

        val lifelineContextEn = when {
            lifelineState.is5050UsedInCurrentQ -> "You activated the 50-50 lifeline, leaving 2 options. Applying Clues 2 & 3 conclusively eliminates your chosen Option $chosenLetter."
            lifelineState.isExpertUsedInCurrentQ -> "Tark Guru highlighted the pivotal constraint, which directly contradicts chosen Option $chosenLetter."
            else -> null
        }

        // 3. Mistake Summary
        val failedStepNum = when (selectedOptionIndex) {
            0 -> 2
            1 -> 3
            2 -> 2
            else -> 4
        }
        val violatedClueNum = if (question.cluesHindi.size >= 2) 2 else 1

        val mistakeSummaryHi = if (isJunior) {
            "आपने विकल्प $chosenLetter चुना। इस विकल्प तक पहुँचने में गलती चरण $failedStepNum पर हुई, जहाँ सुराग $violatedClueNum की मुख्य शर्त छूट गई: $elimReasonHi"
        } else {
            "विकल्प $chosenLetter का चयन चरण $failedStepNum पर अमान्य हो जाता है। सुराग $violatedClueNum के प्रतिबंध का विश्लेषण: $elimReasonHi"
        }

        val mistakeSummaryEn = if (isJunior) {
            "You selected Option $chosenLetter. The error occurred at Step $failedStepNum where Clue $violatedClueNum was overlooked: $elimReasonEn"
        } else {
            "Selecting Option $chosenLetter fails at Step $failedStepNum. Constraint analysis on Clue $violatedClueNum: $elimReasonEn"
        }

        // 4. Constructing Complete Step-by-Step Educational Solution
        val steps = mutableListOf<SolutionStep>()

        // Step 1: Premise / Clue Extraction
        val clue1Hi = question.cluesHindi.getOrNull(0) ?: "प्रश्न की पहली स्थिति को समझें।"
        val clue1En = question.cluesEnglish.getOrNull(0) ?: "Identify the initial premise in Clue 1."
        steps.add(
            SolutionStep(
                stepNumber = 1,
                titleHindi = "चरण 1: मूल सुराग व शर्तों की पहचान",
                titleEnglish = "Step 1: Premise & Clue Extraction",
                detailHindi = "सुराग 1 का विश्लेषण: $clue1Hi",
                detailEnglish = "Clue 1 Extraction: $clue1En"
            )
        )

        // Step 2: Applying Governing Constraint (Failure Point if applicable)
        val clue2Hi = question.cluesHindi.getOrNull(1) ?: "दूसरी स्थिति का मिलान करें।"
        val clue2En = question.cluesEnglish.getOrNull(1) ?: "Apply the secondary condition."
        steps.add(
            SolutionStep(
                stepNumber = 2,
                titleHindi = "चरण 2: मुख्य प्रतिबंध व संबंधों का अनुप्रयोग",
                titleEnglish = "Step 2: Applying Governing Constraints",
                detailHindi = "सुराग 2 लागू करने पर: $clue2Hi",
                detailEnglish = "Applying Clue 2: $clue2En",
                isUserFailurePoint = (failedStepNum == 2)
            )
        )

        // Step 3: Targeted Elimination of User's Chosen Option
        steps.add(
            SolutionStep(
                stepNumber = 3,
                titleHindi = "चरण 3: अमान्य विकल्पों का तार्किक विलोपन (Elimination)",
                titleEnglish = "Step 3: Rigorous Option Elimination",
                detailHindi = "विकल्प $chosenLetter की जाँच: $elimReasonHi\nअतः विकल्प $chosenLetter असंभव है।",
                detailEnglish = "Evaluating Option $chosenLetter: $elimReasonEn\nTherefore, Option $chosenLetter is eliminated.",
                isUserFailurePoint = (failedStepNum == 3),
                isEliminationStep = true
            )
        )

        // Step 4: Branching & Verification
        val clue3Hi = question.cluesHindi.getOrNull(2) ?: "अंतिम निष्कर्ष से मिलान करें।"
        val clue3En = question.cluesEnglish.getOrNull(2) ?: "Verify against the final condition."
        steps.add(
            SolutionStep(
                stepNumber = 4,
                titleHindi = "चरण 4: शेष संभावनाओं का सत्यापन",
                titleEnglish = "Step 4: Branch Verification",
                detailHindi = "शेष विकल्पों पर सुराग 3 लागू करने पर: $clue3Hi",
                detailEnglish = "Testing remaining branches against Clue 3: $clue3En",
                isUserFailurePoint = (failedStepNum == 4)
            )
        )

        // Step 5: Final Deductive Proof
        steps.add(
            SolutionStep(
                stepNumber = 5,
                titleHindi = "चरण 5: अंतिम तार्किक प्रमाण (Final Deduction)",
                titleEnglish = "Step 5: Conclusive Proof",
                detailHindi = "तार्किक निष्पत्ति: ${question.deductionPathHindi}\nअतः केवल विकल्प $correctLetter ($correctTextHi) ही 100% वैध है।",
                detailEnglish = "Deductive Proof: ${question.deductionPathEnglish}\nThus, only Option $correctLetter ($correctTextEn) is conclusively valid.",
                isValidationStep = true
            )
        )

        val finalConclusionHi = "सही उत्तर: विकल्प $correctLetter ($correctTextHi) — ${question.deductionPathHindi}"
        val finalConclusionEn = "Correct Answer: Option $correctLetter ($correctTextEn) — ${question.deductionPathEnglish}"

        // 5. Educational Audio Scripts (Natural Pacing)
        val audioScriptHi = if (isJunior) {
            "अरे! आपका चुना हुआ उत्तर सही नहीं था। चलिए मिलकर देखते हैं कि कहाँ चूक हुई। आपने विकल्प $chosenLetter चुना था। $mistakeSummaryHi पूरा समाधान इस प्रकार है: पहले सुराग के अनुसार $clue1Hi। इसके बाद $clue2Hi। इसलिए विकल्प $chosenLetter खारिज हो जाता है। सही निष्कर्ष यह है कि केवल विकल्प $correctLetter ही सही है।"
        } else {
            "उत्तर का तार्किक विश्लेषण: आपने विकल्प $chosenLetter चुना। इस विकल्प में चरण $failedStepNum पर तार्किक विरोधाभास है: $elimReasonHi। सुराग 1 और 2 के संयुक्त विश्लेषण से: ${question.deductionPathHindi}। अतः केवल विकल्प $correctLetter ही पूर्णतः सिद्ध उत्तर है।"
        }

        val audioScriptEn = if (isJunior) {
            "Let's see what went wrong. You selected Option $chosenLetter. $mistakeSummaryEn Here is the complete step-by-step solution: Clue 1 establishes $clue1En. Applying Clue 2 reveals why Option $chosenLetter cannot be true. The only logically valid answer is Option $correctLetter."
        } else {
            "Deductive Analysis: You selected Option $chosenLetter. This choice fails at Step $failedStepNum due to a constraint violation: $elimReasonEn. Step-by-step resolution: ${question.deductionPathEnglish}. Therefore, only Option $correctLetter is logically sound."
        }

        return FullSolutionModel(
            mistakeAnalysis = MistakeAnalysis(
                mistakeType = mistakeType,
                fallacyName = fallacyName,
                mistakeSummaryHindi = mistakeSummaryHi,
                mistakeSummaryEnglish = mistakeSummaryEn,
                failedStepNumber = failedStepNum,
                violatedClueNumber = violatedClueNum,
                chosenOptionLetter = chosenLetter,
                chosenOptionTextHindi = chosenTextHi,
                chosenOptionTextEnglish = chosenTextEn,
                correctOptionLetter = correctLetter,
                correctOptionTextHindi = correctTextHi,
                correctOptionTextEnglish = correctTextEn,
                lifelineContextHindi = lifelineContextHi,
                lifelineContextEnglish = lifelineContextEn
            ),
            steps = steps,
            finalConclusionHindi = finalConclusionHi,
            finalConclusionEnglish = finalConclusionEn,
            teacherVoiceScriptHindi = audioScriptHi,
            teacherVoiceScriptEnglish = audioScriptEn,
            visualDiagramType = question.diagramType,
            visualDiagramData = question.diagramData,
            isJuniorMode = isJunior
        )
    }
}
