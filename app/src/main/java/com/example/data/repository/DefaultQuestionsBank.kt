package com.example.data.repository

import com.example.data.api.GeminiApiClient
import com.example.data.model.QuestionItem

/**
 * Built-in bank of meticulously verified Pure Logic & Reasoning questions.
 * Every question contains complete context, clues, step-by-step mathematical/logical deduction proof,
 * 50-50 elimination proofs, non-spoiler expert advice, and diagram/audio metadata.
 */
object DefaultQuestionsBank {

    fun getQuestionsForTier(qNumber: Int, isStudent: Boolean = false): List<QuestionItem> {
        val list = if (isStudent) {
            val junior = getJuniorStudentQuestions().filter { it.qNumber == qNumber }
            if (junior.isNotEmpty()) junior else getAllQuestions().filter { it.qNumber == qNumber }
        } else {
            getAllQuestions().filter { it.qNumber == qNumber }
        }
        return if (list.isNotEmpty()) list else listOf(getFallbackGenericQuestion(qNumber))
    }

    private fun getAllQuestions(): List<QuestionItem> {
        return listOf(
            // ==========================================
            // Q1: Very Easy (60s) - Spatial & Direct Clue Logic
            // ==========================================
            createQuestion(
                qNumber = 1,
                category = "Spatial Coordinate Vector",
                questionHindi = "एक व्यक्ति बिंदु A से उत्तर की ओर 10 किमी चलता है, फिर दाएँ मुड़कर 5 किमी चलता है। अब वह अपने प्रारंभिक बिंदु A से किस दिशा में स्थित है?",
                questionEnglish = "A person walks 10 km North from point A, then turns right (East) and walks 5 km. In which direction is he now located with respect to his starting point A?",
                cluesHindi = listOf(
                    "दिशा 1: धनात्मक Y-अक्ष (उत्तर) = +10 किमी",
                    "दिशा 2: दाएँ मुड़ना अर्थात धनात्मक X-अक्ष (पूर्व) = +5 किमी",
                    "अंतिम स्थिति (+5, +10) प्रथम चतुर्थांश में है।"
                ),
                cluesEnglish = listOf(
                    "Movement 1: Along positive Y-axis (North) = +10 km",
                    "Movement 2: Right turn leads along positive X-axis (East) = +5 km",
                    "Final coordinate (+5, +10) lies strictly in Quadrant I."
                ),
                optionsHindi = listOf("उत्तर-पूर्व (North-East)", "दक्षिण-पूर्व (South-East)", "उत्तर-पश्चिम (North-West)", "केवल पूर्व (East only)"),
                optionsEnglish = listOf("North-East", "South-East", "North-West", "East only"),
                correctIndex = 0,
                deductionHindi = "प्रारंभिक बिंदु (0,0) से उत्तर जाने पर स्थिति (0,10) हुई। फिर दाएँ (पूर्व) मुड़ने पर अंतिम स्थिति (5,10) बनी। X > 0 और Y > 0 का संयोजन अनिवार्य रूप से 'उत्तर-पूर्व' दिशा निर्धारित करता है।",
                deductionEnglish = "From (0,0), walking North reaches (0,10). Turning right (East) reaches (5,10). Since both X and Y offsets are strictly positive, the relative vector points directly into the North-East quadrant.",
                elimReasonsHindi = listOf(
                    "सही उत्तर (दोनों अक्ष धनात्मक हैं)",
                    "गलत: दक्षिण जाने के लिए Y ऋणात्मक होना चाहिए था, जबकि व्यक्ति उत्तर गया है।",
                    "गलत: पश्चिम जाने के लिए X ऋणात्मक होना चाहिए था, जबकि दाएँ मुड़कर पूर्व गया है।",
                    "गलत: व्यक्ति 10 किमी उत्तर भी गया है, अतः केवल पूर्व में नहीं हो सकता।"
                ),
                elimReasonsEnglish = listOf(
                    "Correct: Coordinates (+5, +10) define North-East.",
                    "False: South requires negative Y coordinate, but movement was North.",
                    "False: West requires negative X coordinate, but turn was East.",
                    "False: Pure East requires zero Y displacement, but +10 km North displacement exists."
                ),
                expertHintHindi = "आंदोलन को X और Y निर्देशांकों (0,0) के सापेक्ष देखें। उत्तर = +Y, दाएँ मुड़ना = +X। दोनों धनात्मक मान किस चतुर्थांश को दर्शाते हैं?",
                expertHintEnglish = "Plot the two movements on a Cartesian grid from (0,0). North is +Y, East is +X. Identify which quadrant a (+, +) vector points to.",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "दक्षिण और पश्चिम दोनों दिशाएँ दिए गए धनात्मक विस्थापन के सीधे विपरीत हैं।",
                fiftyFiftyProofEnglish = "South and West are mathematically impossible because the traveler only made positive North and East displacements.",
                diagramType = "coordinate_path",
                diagramData = "{\"path\": [{\"dx\": 0, \"dy\": 10}, {\"dx\": 5, \"dy\": 0}], \"labels\": [\"Start (A)\", \"Turn Right\", \"End (B)\"]}"
            ),

            // Q1 Alternative (for Flip Lifeline)
            createQuestion(
                qNumber = 1,
                category = "Clock Angle Geometry",
                questionHindi = "एक एनालॉग घड़ी में ठीक 3:00 बजे घंटे की सुई और मिनट की सुई के बीच का आंतरिक कोण कितना होगा?",
                questionEnglish = "On a standard analog clock, what is the exact internal angle between the hour hand and minute hand at precisely 3:00?",
                cluesHindi = listOf(
                    "घड़ी का पूरा डायल 360° का होता है, जो 12 बराबर घंटों में बंटा है (प्रत्येक घंटा = 30°)।",
                    "3:00 बजे मिनट की सुई ठीक 12 पर और घंटे की सुई ठीक 3 पर होती है।",
                    "12 से 3 के बीच कुल 3 घंटे का अंतराल है।"
                ),
                cluesEnglish = listOf(
                    "A full clock circle is 360°, divided into 12 equal hours (30° per hour).",
                    "At 3:00, the minute hand points directly at 12 and the hour hand at 3.",
                    "The interval between 12 and 3 comprises exactly 3 hour blocks."
                ),
                optionsHindi = listOf("90° (समकोण)", "60°", "120°", "180° (सरल रेखा)"),
                optionsEnglish = listOf("90° (Right angle)", "60°", "120°", "180° (Straight line)"),
                correctIndex = 0,
                deductionHindi = "360° / 12 = 30° प्रति घंटा। 12 से 3 तक 3 घंटे हैं: 3 × 30° = 90°। अतः कोण ठीक 90° (समकोण) है।",
                deductionEnglish = "360° divided by 12 hours equals 30° per hour. The hands span 3 hour gaps: 3 × 30° = 90° (a right angle).",
                elimReasonsHindi = listOf("सही: 3 × 30° = 90°", "गलत: 60° केवल 2 घंटे (2:00) का अंतर होता है।", "गलत: 120° 4 घंटे (4:00) का अंतर होता है।", "गलत: 180° 6:00 बजे बनता है।"),
                elimReasonsEnglish = listOf("Correct: 3 × 30° = 90°", "False: 60° corresponds to 2:00.", "False: 120° corresponds to 4:00.", "False: 180° occurs at 6:00."),
                expertHintHindi = "पूरे 360 डिग्री चक्र को 12 से विभाजित करें। फिर 12 और 3 के बीच के घंटों की संख्या से गुणा करें।",
                expertHintEnglish = "Calculate degrees per single hour slot (360/12), then multiply by the count of hourly spaces between 12 and 3.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "60° (2 घंटे) और 180° (6 घंटे) स्पष्ट रूप से 3 बजे की स्थिति से मेल नहीं खाते।",
                fiftyFiftyProofEnglish = "60° represents 2 hours and 180° represents 6 hours, directly conflicting with 3:00.",
                diagramType = "clock_angle",
                diagramData = "{\"hour\": 3, \"minute\": 0}"
            ),

            // ==========================================
            // Q2: Very Easy (60s) - Visual Shadow Optics
            // ==========================================
            createQuestion(
                qNumber = 2,
                category = "Visual Shadow Optics",
                questionHindi = "सुबह 8:00 बजे, एक सीधे खड़े खंभे की परछाई जमीन पर ठीक पश्चिम दिशा में पड़ रही है। प्रकाश के सरल-रेखीय गमन के सिद्धांत के आधार पर सूर्य किस दिशा में स्थित है?",
                questionEnglish = "At 8:00 AM in the morning, a vertical pole casts its shadow directly towards the West on flat ground. Based on linear light ray propagation, in which direction is the Sun located?",
                cluesHindi = listOf(
                    "प्रकाश सीधी रेखा में गमन करता है।",
                    "किसी वस्तु की छाया सदैव प्रकाश स्रोत के ठीक विपरीत दिशा (180° उल्टी) में बनती है।",
                    "छाया की दिशा = पश्चिम (West)।"
                ),
                cluesEnglish = listOf(
                    "Light travels in straight lines (rectilinear propagation).",
                    "An opaque object casts its shadow in the direction exactly opposite (180°) to the light source.",
                    "Shadow vector = West."
                ),
                optionsHindi = listOf("पूर्व (East)", "उत्तर (North)", "दक्षिण (South)", "पश्चिम (West)"),
                optionsEnglish = listOf("East", "North", "South", "West"),
                correctIndex = 0,
                deductionHindi = "चूँकि छाया = प्रकाश स्रोत की विपरीत दिशा। यदि छाया पश्चिम में है, तो स्रोत = पश्चिम का 180° विपरीत = पूर्व (East)।",
                deductionEnglish = "Since shadow direction is directly opposite the light source: Opposite of West is East.",
                elimReasonsHindi = listOf("सही: पश्चिम का 180° विपरीत पूर्व होता है।", "गलत: यदि सूर्य उत्तर में होता तो परछाई दक्षिण में बनती।", "गलत: यदि सूर्य दक्षिण में होता तो परछाई उत्तर में बनती।", "गलत: सूर्य और छाया एक ही दिशा में कभी नहीं हो सकते।"),
                elimReasonsEnglish = listOf("Correct: Exactly opposite to West is East.", "False: North sun casts South shadow.", "False: South sun casts North shadow.", "False: Light source and shadow can never occupy the same vector."),
                expertHintHindi = "एक रेखा खींचें: सूर्य → खंभा → परछाई। यदि परछाई पश्चिम में समाप्त हो रही है, तो रेखा का शुरुआती बिंदु क्या होगा?",
                expertHintEnglish = "Imagine a collinear ray: Sun -> Pole -> Shadow. If the terminal shadow points West, the origin ray must start from the opposite cardinal point.",
                fiftyFiftyDiscards = listOf(2, 3),
                fiftyFiftyProofHindi = "दक्षिण और पश्चिम दोनों प्रकाशिकी के 180° विपरीत नियम का उल्लंघन करते हैं।",
                fiftyFiftyProofEnglish = "South and West directly contradict the 180-degree optical opposition rule.",
                diagramType = "shadow_sun",
                diagramData = "{\"sunDirection\": \"East\", \"shadowDirection\": \"West\", \"time\": \"8:00 AM\"}"
            ),

            // ==========================================
            // Q3: Easy (60s) - Syllogistic Logical Deduction
            // ==========================================
            createQuestion(
                qNumber = 3,
                category = "Syllogistic Deduction",
                questionHindi = "कथन 1: सभी धातुएं विद्युत की सुचालक हैं।\nकथन 2: X विद्युत का सुचालक नहीं है।\nइन दो कथनों से 100% निश्चित तार्किक निष्कर्ष क्या निकलता है?",
                questionEnglish = "Premise 1: All metals are electrical conductors.\nPremise 2: Substance X is not an electrical conductor.\nWhat is the 100% logically certain conclusion derived from these premises?",
                cluesHindi = listOf(
                    "यदि P (धातु) तो Q (सुचालक)। [P ⊆ Q]",
                    "कथन 2 बताता है कि ¬Q (X सुचालक नहीं है)।",
                    "मॉडस टोलेंस (Modus Tollens) नियम: यदि P → Q सत्य है और ¬Q सत्य है, तो ¬P (X धातु नहीं है) अनिवार्य रूप से सत्य होगा।"
                ),
                cluesEnglish = listOf(
                    "Rule 1: If P (metal), then Q (conductor). [P is a subset of Q]",
                    "Rule 2: Not Q (X is not a conductor).",
                    "Modus Tollens law of logic: If P implies Q, and Not Q is true, then Not P (X is NOT a metal) must be unconditionally true."
                ),
                optionsHindi = listOf("X एक धातु नहीं है (X is not a metal)", "X एक तरल धातु है", "X एक चुंबक है", "X सभी परिस्थितियों में गैस है"),
                optionsEnglish = listOf("X is not a metal", "X is a liquid metal", "X is a magnet", "X is always a gas"),
                correctIndex = 0,
                deductionHindi = "चूँकि समस्त धातुओं का समूह सुचालकों के अंदर समाहित है, और X सुचालकों के समूह से बाहर है, अतः X धातु समूह का सदस्य हो ही नहीं सकता।",
                deductionEnglish = "Since the set of all metals is strictly contained inside conductors, any non-conductor X is mathematically outside the set of metals.",
                elimReasonsHindi = listOf("सही: मॉडस टोलेंस तर्क के अनुसार 100% सत्य।", "गलत: तरल धातुएं (जैसे पारा) भी धातु होने के कारण सुचालक होती हैं।", "गलत: चुंबकत्व का दिए गए कथनों से कोई तार्किक संबंध नहीं है।", "गलत: X ठोस या तरल अ-धातु भी हो सकता है, गैस होना अनिवार्य नहीं।"),
                elimReasonsEnglish = listOf("Correct: Strictly validated by Modus Tollens deduction.", "False: Liquid metals are still metals and conduct electricity.", "False: Magnetism is an unmentioned extraneous property.", "False: X could be a solid insulator (plastic/wood); being gas is unsupported."),
                expertHintHindi = "वेन आरेख की कल्पना करें: 'धातु' का छोटा घेरा 'सुचालक' के बड़े घेरे के अंदर है। वस्तु X बड़े घेरे से बाहर है। क्या वह छोटे घेरे में हो सकती है?",
                expertHintEnglish = "Visualize Venn circles: The 'Metals' circle is inside the 'Conductors' circle. X is completely outside the Conductors circle. Can X be inside Metals?",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "तरल धातु और चुंबक दोनों दिए गए आधारभूत निषेध का खंडन करते हैं।",
                fiftyFiftyProofEnglish = "Liquid metal and magnet contradict or introduce irrelevant assumptions.",
                diagramType = "venn_logic",
                diagramData = "{\"outerSet\": \"Conductors\", \"innerSet\": \"Metals\", \"pointX\": \"Outside Conductors\"}"
            ),

            // ==========================================
            // Q4: Easy (60s) - Audio Rhythm Meter Analysis
            // ==========================================
            createQuestion(
                qNumber = 4,
                category = "Acoustic Rhythm Meter",
                questionHindi = "एक संगीत रचना में प्रत्येक आवर्त (Bar) में 3 समान समय-मात्राएँ हैं, जिसमें पहली मात्रा पर प्रबल आघात (Strong Accent) और अगली दो मात्राओं पर दुर्बल आघात (Weak Accent: 'धिन-तिन-तिन' / 'ONE-two-three') सुनाई देता है। यह कौन-सा तार्किक ताल-ढाँचा (Time Signature) है?",
                questionEnglish = "In a musical rhythm, each measure contains 3 equal beats with a strong accented pulse on beat 1 followed by two unaccented lighter pulses ('ONE-two-three, ONE-two-three'). What is this strict mathematical time signature?",
                cluesHindi = listOf(
                    "कुल मात्रा प्रति आवर्त = 3",
                    "आघात पैटर्न: प्रबल (1) - दुर्बल (2) - दुर्बल (3)",
                    "यह पारंपरिक वाल्ट्ज (Waltz) और 3-मात्रा रूपक छंद का गणितीय ढाँचा है।"
                ),
                cluesEnglish = listOf(
                    "Total beats per measure = 3 equal intervals.",
                    "Accent profile: Heavy (Beat 1) - Light (Beat 2) - Light (Beat 3).",
                    "This is the strict ternary metric structure of 3/4 Triple Meter (Waltz time)."
                ),
                optionsHindi = listOf("3/4 त्रि-मात्रा ताल (Triple / Waltz Meter)", "4/4 सम-चतुर्मात्रा ताल (Common Quadruple)", "2/4 द्वि-मात्रा मार्च ताल (Duple March)", "7/8 विषम-मिश्र ताल (Complex Odd Meter)"),
                optionsEnglish = listOf("3/4 Triple / Waltz Meter", "4/4 Quadruple Meter", "2/4 Duple March Meter", "7/8 Complex Odd Meter"),
                correctIndex = 0,
                deductionHindi = "प्रत्येक आवर्त में 3 मात्राएं हैं और आघात 1 पर है (1-2-3)। 4/4 में 4 मात्राएं, 2/4 में 2 मात्राएं और 7/8 में 7 उप-मात्राएं होती हैं। अतः 3 मात्राओं का समूह केवल 3/4 ही हो सकता है।",
                deductionEnglish = "Counting 3 distinct beats per bar with an initial primary pulse matches the exact mathematical definition of 3/4 meter. 4/4 has 4 beats, 2/4 has 2 beats, and 7/8 has 7 sub-beats.",
                elimReasonsHindi = listOf("सही: 3 मात्राओं का त्रि-मात्रिक ढांचा।", "गलत: 4/4 में प्रति आवर्त 4 मात्राएं (1-2-3-4) होती हैं।", "गलत: 2/4 में प्रति आवर्त केवल 2 मात्राएं (1-2) होती हैं।", "गलत: 7/8 में 7 द्रुत मात्राएं होती हैं।"),
                elimReasonsEnglish = listOf("Correct: 3 equal pulses per measure.", "False: 4/4 requires a 4-beat cycle.", "False: 2/4 is a 2-beat march cycle.", "False: 7/8 is an asymmetric 7-pulse meter."),
                expertHintHindi = "गिनती की लय सुनें: 'एक-दो-तीन, एक-दो-तीन'। प्रति चक्र में कुल कितने अंक दोहराए जा रहे हैं?",
                expertHintEnglish = "Count the cyclic repetition: '1 - 2 - 3 | 1 - 2 - 3'. What is the exact denominator count of pulses before the heavy accent restarts?",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "4/4 और 2/4 में क्रमशः 4 और 2 मात्राएं होती हैं, जो 3-मात्रा चक्र से मेल नहीं खातीं।",
                fiftyFiftyProofEnglish = "4/4 and 2/4 have 4 and 2 beats respectively, directly violating the observed 3-beat cycle.",
                diagramType = "audio_wave",
                diagramData = "{\"meter\": \"3/4\", \"beats\": [{\"beat\": 1, \"accent\": \"STRONG\"}, {\"beat\": 2, \"accent\": \"LIGHT\"}, {\"beat\": 3, \"accent\": \"LIGHT\"}]}",
                audioPatternType = "waltz_3_4"
            ),

            // ==========================================
            // Q5: 🏆 पहला पड़ाव (1st Checkpoint - ₹10,000) - Cryptic Logic Matrix
            // ==========================================
            createQuestion(
                qNumber = 5,
                category = "Matrix Pattern Logic",
                questionHindi = "निम्नलिखित 3x3 संख्या मैट्रिक्स के नियम को समझकर '?' के स्थान पर सही संख्या का तार्किक चयन करें:\n[ 4 | 9 | 2 ]\n[ 3 | 5 | 7 ]\n[ 8 | 1 | ? ]\n(सुराग: प्रत्येक पंक्ति, स्तंभ और दोनों विकर्णों का योग समान है)",
                questionEnglish = "Determine the missing number '?' in this classic 3x3 Magic Square matrix:\n[ 4 | 9 | 2 ]\n[ 3 | 5 | 7 ]\n[ 8 | 1 | ? ]\n(Clue: The sum of every row, every column, and both diagonals is strictly constant)",
                cluesHindi = listOf(
                    "पहली पंक्ति का योग: 4 + 9 + 2 = 15",
                    "दूसरी पंक्ति का योग: 3 + 5 + 7 = 15",
                    "तीसरी पंक्ति: 8 + 1 + ? = 15 होना अनिवार्य है।"
                ),
                cluesEnglish = listOf(
                    "Row 1 sum: 4 + 9 + 2 = 15",
                    "Row 2 sum: 3 + 5 + 7 = 15",
                    "Row 3 requirement: 8 + 1 + ? must equal exactly 15."
                ),
                optionsHindi = listOf("6", "4", "5", "9"),
                optionsEnglish = listOf("6", "4", "5", "9"),
                correctIndex = 0,
                deductionHindi = "प्रत्येक पंक्ति का नियत योग 15 है। तीसरी पंक्ति में: 8 + 1 = 9। 15 - 9 = 6। स्तंभ 3 से भी जाँच: 2 + 7 + 6 = 15। विकर्ण से भी: 4 + 5 + 6 = 15। अतः ? = 6।",
                deductionEnglish = "Row constant is 15. In row 3: 8 + 1 = 9. 15 - 9 = 6. Verifying column 3: 2 + 7 + 6 = 15. Verifying diagonal: 4 + 5 + 6 = 15. Therefore, ? = 6.",
                elimReasonsHindi = listOf("सही: 8 + 1 + 6 = 15 और विकर्ण 4 + 5 + 6 = 15।", "गलत: 4 रखने पर पंक्ति का योग 13 रह जाएगा।", "गलत: 5 रखने पर पंक्ति का योग 14 होगा और केंद्र में 5 पहले से है।", "गलत: 9 रखने पर पंक्ति का योग 18 हो जाएगा।"),
                elimReasonsEnglish = listOf("Correct: Exactly balances row, column, and diagonal to 15.", "False: 4 yields a sum of 13.", "False: 5 yields 14 and duplicates the center cell.", "False: 9 yields an excess sum of 18."),
                expertHintHindi = "पहली पंक्ति की सभी संख्याओं को जोड़ें। वही कुल योग तीसरी पंक्ति का भी होना चाहिए। 15 में से (8+1) घटाएं।",
                expertHintEnglish = "Calculate the invariant sum of Row 1. Apply that exact sum constraint to Row 3 and solve for ?.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "4 और 9 योग को क्रमशः 13 और 18 बना देते हैं, जो 15 के नियम का खंडन है।",
                fiftyFiftyProofEnglish = "4 and 9 produce row totals of 13 and 18, violating the invariant sum of 15.",
                diagramType = "matrix_grid",
                diagramData = "{\"rows\": [[4, 9, 2], [3, 5, 7], [8, 1, \"?\"]], \"targetSum\": 15}"
            ),

            // ==========================================
            // Q6: Medium (120s) - Financial Ledger & Zero-Sum Logic
            // ==========================================
            createQuestion(
                qNumber = 6,
                category = "Economic Balance Logic",
                questionHindi = "एक व्यापारी ने एक वस्तु ₹800 में खरीदी, उसे ₹900 में बेचा। बाद में उसी वस्तु को ₹1,000 में पुनः खरीदा और अंततः ₹1,100 में बेच दिया। इस पूरे सौदे में व्यापारी को कुल कितना शुद्ध लाभ या हानि हुई?",
                questionEnglish = "A trader buys an antique for ₹800 and sells it for ₹900. Later, he buys it back for ₹1,000 and sells it finally for ₹1,100. What is his exact net financial profit or loss from these transactions?",
                cluesHindi = listOf(
                    "सौदा 1: क्रय = ₹800, विक्रय = ₹900 -> लाभ 1 = 900 - 800",
                    "सौदा 2: क्रय = ₹1000, विक्रय = ₹1100 -> लाभ 2 = 1100 - 1000",
                    "कुल शुद्ध परिणाम = लाभ 1 + लाभ 2 (दोनों स्वतंत्र नकदी प्रवाह हैं)।"
                ),
                cluesEnglish = listOf(
                    "Trade 1: Inflow (+900) - Outflow (-800) = +₹100",
                    "Trade 2: Inflow (+1100) - Outflow (-1000) = +₹100",
                    "Net result = Cumulative Cash Flow (+100 + 100)."
                ),
                optionsHindi = listOf("₹200 का शुद्ध लाभ", "₹100 का लाभ", "कोई लाभ या हानि नहीं (₹0)", "₹100 की हानि"),
                optionsEnglish = listOf("₹200 Net Profit", "₹100 Profit", "Break-even (₹0)", "₹100 Loss"),
                correctIndex = 0,
                deductionHindi = "कुल खर्च (Outflow) = 800 + 1000 = ₹1,800। कुल आय (Inflow) = 900 + 1100 = ₹2,000। शुद्ध लाभ = 2,000 - 1,800 = ₹200।",
                deductionEnglish = "Total cash spent = 800 + 1000 = ₹1,800. Total cash received = 900 + 1100 = ₹2,000. Net profit = ₹2,000 - ₹1,800 = ₹200.",
                elimReasonsHindi = listOf("सही: दोनों चक्रों में ₹100 + ₹100 का संचयी लाभ।", "गलत: केवल एक चक्र की गणना है, दूसरे चक्र का लाभ छोड़ दिया गया।", "गलत: ₹900 से ₹1000 के बीच कोई नुकसान नहीं हुआ क्योंकि वस्तु उसके पास नहीं थी।", "गलत: कुल नकदी प्रवाह धनात्मक है, हानि असंभव है।"),
                elimReasonsEnglish = listOf("Correct: Sum of both +100 cash cycles = +200.", "False: Only accounts for a single cycle.", "False: Assumes false loss between sell and buyback.", "False: Total inflow exceeds total outflow."),
                expertHintHindi = "वस्तु के बारे में मत सोचिए; केवल व्यापारी की जेब से निकले कुल पैसों और वापस आए कुल पैसों का अंतर निकालिए।",
                expertHintEnglish = "Disregard the physical item; simply calculate Total Inflow minus Total Outflow across the two discrete trades.",
                fiftyFiftyDiscards = listOf(2, 3),
                fiftyFiftyProofHindi = "शून्य लाभ और हानि दोनों स्पष्ट रूप से कुल धनात्मक नकदी प्रवाह (+₹200) का खंडन करते हैं।",
                fiftyFiftyProofEnglish = "Zero profit and loss contradict the positive net balance sheet.",
                diagramType = "none"
            ),

            // ==========================================
            // Q7: Medium (120s) - Forensic Chronology Clue
            // ==========================================
            createQuestion(
                qNumber = 7,
                category = "Forensic Chronology",
                questionHindi = "चार गवाह A, B, C, D एक घटना के समय के बारे में बयान देते हैं:\n1. A कहता है: 'घटना 2:00 PM के बाद लेकिन 4:00 PM से पहले हुई।'\n2. B कहता है: 'घटना 3:00 PM के बाद लेकिन 5:00 PM से पहले हुई।'\n3. C कहता है: 'घटना 3:15 PM के बाद लेकिन 3:45 PM से पहले हुई।'\nयदि सभी गवाह 100% सच बोल रहे हैं, तो घटना किस समय सीमा में हुई?",
                questionEnglish = "Four witnesses provide truthful timing constraints for an event:\n1. Witness A: 'Between 2:00 PM and 4:00 PM.'\n2. Witness B: 'Between 3:00 PM and 5:00 PM.'\n3. Witness C: 'Between 3:15 PM and 3:45 PM.'\nAssuming all witnesses are 100% truthful, what is the exact valid time intersection?",
                cluesHindi = listOf(
                    "A का अंतराल: (14:00, 16:00)",
                    "B का अंतराल: (15:00, 17:00) -> A और B का उभयनिष्ठ = (15:00, 16:00)",
                    "C का अंतराल: (15:15, 15:45) -> तीनों का उभयनिष्ठ प्रतिच्छेदन (Intersection) = (15:15, 15:45)"
                ),
                cluesEnglish = listOf(
                    "Interval A: (14:00, 16:00)",
                    "Interval B: (15:00, 17:00) -> Intersection with A = (15:00, 16:00)",
                    "Interval C: (15:15, 15:45) -> Universal intersection = strictly between 3:15 PM and 3:45 PM."
                ),
                optionsHindi = listOf("3:15 PM से 3:45 PM के बीच", "2:00 PM से 5:00 PM के बीच", "3:00 PM से 4:00 PM के बीच", "ठीक 3:30 PM पर ही"),
                optionsEnglish = listOf("Between 3:15 PM and 3:45 PM", "Between 2:00 PM and 5:00 PM", "Between 3:00 PM and 4:00 PM", "Exactly at 3:30 PM only"),
                correctIndex = 0,
                deductionHindi = "सभी गवाहों के सत्य होने के लिए समय सभी अंतरालों का प्रतिच्छेदन (Intersection) होना चाहिए। Max(2:00, 3:00, 3:15) = 3:15 PM और Min(4:00, 5:00, 3:45) = 3:45 PM। अतः समय 3:15 और 3:45 PM के बीच है।",
                deductionEnglish = "For all statements to be simultaneously true, the time must be within the mathematical intersection of all sets: max(start times) = 3:15 PM and min(end times) = 3:45 PM.",
                elimReasonsHindi = listOf("सही: सभी तीन बयानों का उभयनिष्ठ भाग।", "गलत: 2:00 से 5:00 यूनियन (Union) है, उभयनिष्ठ नहीं।", "गलत: 3:00 से 3:15 के बीच C का बयान झूठा हो जाएगा।", "गलत: 3:20 या 3:40 भी संभव है, केवल 3:30 होना आवश्यक नहीं।"),
                elimReasonsEnglish = listOf("Correct: Mathematical set intersection.", "False: Union of sets, not intersection.", "False: 3:05 would violate Witness C.", "False: Any sub-minute within 3:15-3:45 is valid, not solely 3:30."),
                expertHintHindi = "संख्या रेखा पर तीनों अंतरालों को ओवरलैप करें। वह सबसे संकीर्ण हिस्सा कौन-सा है जो तीनों पट्टियों में एक साथ आता है?",
                expertHintEnglish = "Find the overlapping segment common to all three intervals simultaneously on a time number-line.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "यूनियन (2:00-5:00) और एकल बिंदु (3:30) दोनों समुच्चय सिद्धांत के अनुसार अमान्य हैं।",
                fiftyFiftyProofEnglish = "The full span (2-5) and singular point (3:30) violate mathematical intersection logic.",
                diagramType = "none"
            ),

            // ==========================================
            // Q8: Medium+ (120s) - Chemical Balance & Conservation
            // ==========================================
            createQuestion(
                qNumber = 8,
                category = "Conservation Science",
                questionHindi = "एक बंद वायुरोधी (Hermetically sealed) कांच के बर्तन का कुल वजन 500 ग्राम है। इसके अंदर 10 ग्राम ठोस मोमबत्ती जलकर पूरी तरह कार्बन डाइऑक्साइड और जलवाष्प में बदल जाती है। द्रव्यमान संरक्षण के नियम (Law of Conservation of Mass) के अनुसार जलने के बाद बर्तन का कुल वजन कितना होगा?",
                questionEnglish = "A completely sealed, airtight glass chamber weighs 500 grams total, containing air and a 10-gram candle. The candle burns completely, converting into CO2 and water vapor inside the closed vessel. By the Law of Conservation of Mass, what is the total weight of the sealed chamber after burning?",
                cluesHindi = listOf(
                    "सिस्टम पूरी तरह बंद (Closed System) है; कोई भी गैस या पदार्थ बाहर नहीं निकल सकता।",
                    "द्रव्यमान संरक्षण का नियम: किसी बंद निकाय में रासायनिक अभिक्रिया के पहले और बाद का कुल द्रव्यमान अपरिवर्तित रहता है।",
                    "मोम + ऑक्सीजन = CO2 + H2O (सभी उत्पाद बर्तन के अंदर ही मौजूद हैं)।"
                ),
                cluesEnglish = listOf(
                    "The system is strictly closed; zero matter enters or escapes.",
                    "Law of Conservation of Mass: In an isolated closed system, total mass is strictly conserved during chemical transformations.",
                    "Wax + O2 -> CO2 + H2O; all reactants and products remain inside the vessel."
                ),
                optionsHindi = listOf("ठीक 500 ग्राम (अपरिवर्तित)", "490 ग्राम (10 ग्राम मोम कम होने पर)", "510 ग्राम", "495 ग्राम"),
                optionsEnglish = listOf("Exactly 500 grams (Unchanged)", "490 grams (10g wax consumed)", "510 grams", "495 grams"),
                correctIndex = 0,
                deductionHindi = "चूँकि बर्तन वायुरोधी रूप से बंद है, मोम के जलने से उत्पन्न सभी गैसें (CO2 और H2O) बर्तन के अंदर ही विद्यमान हैं। द्रव्यमान संरक्षण नियम के तहत द्रव्यमान न तो नष्ट हो सकता है न नया उत्पन्न। अतः कुल वजन 500 ग्राम ही रहेगा।",
                deductionEnglish = "Because the container is sealed, the gaseous products remain trapped inside. Matter neither leaves nor enters, so total mass remains strictly invariant at 500 grams.",
                elimReasonsHindi = listOf("सही: बंद निकाय में कुल द्रव्यमान सदैव संरक्षित रहता है।", "गलत: 490 ग्राम तब होता जब बर्तन खुला होता और धुआं/गैस बाहर निकल जाती।", "गलत: कोई बाहरी द्रव्यमान नहीं जुड़ा है।", "गलत: द्रव्यमान का आंशिक नुकसान भी असंभव है।"),
                elimReasonsEnglish = listOf("Correct: Total mass is conserved in a closed container.", "False: 490g only applies to an open system where gases escape.", "False: No external mass entered.", "False: Arbitrary mass loss violates fundamental physics."),
                expertHintHindi = "ध्यान दें: बर्तन 'वायुरोधी और बंद' है। क्या कोई भी अणु या गैस बर्तन से बाहर जा सकती है?",
                expertHintEnglish = "Key constraint: The vessel is 'hermetically sealed'. Can any molecules escape into the outside atmosphere?",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "490 ग्राम और 510 ग्राम दोनों द्रव्यमान संरक्षण के सार्वभौमिक नियम का उल्लंघन करते हैं।",
                fiftyFiftyProofEnglish = "490g and 510g violate mass conservation in a closed thermodynamic system.",
                diagramType = "none"
            ),

            // ==========================================
            // Q9: Medium+ (120s) - Cryptic Logic Deductive Cipher
            // ==========================================
            createQuestion(
                qNumber = 9,
                category = "Deductive Cryptic Cipher",
                questionHindi = "एक कूट भाषा में:\n• 'RING' = '9-9-5-7' (R=18 -> 1+8=9, I=9, N=14 -> 1+4=5, G=7)\nउपरोक्त एकल-अंकीय योग (Digital Root) नियम के अनुसार 'TARK' का कूट क्या होगा?\n(वर्णमाला क्रमांक: T=20, A=1, R=18, K=11)",
                questionEnglish = "In a logical cipher:\n• 'RING' is encoded as '9-9-5-7' (R=18 -> 1+8=9, I=9, N=14 -> 1+4=5, G=7).\nUsing this exact sum-of-digits (Digital Root) rule, what is the code for 'TARK'?\n(Alphabet positions: T=20, A=1, R=18, K=11)",
                cluesHindi = listOf(
                    "T = 20 -> 2 + 0 = 2",
                    "A = 1 -> 1",
                    "R = 18 -> 1 + 8 = 9",
                    "K = 11 -> 1 + 1 = 2"
                ),
                cluesEnglish = listOf(
                    "T = position 20 -> 2 + 0 = 2",
                    "A = position 1 -> 1",
                    "R = position 18 -> 1 + 8 = 9",
                    "K = position 11 -> 1 + 1 = 2"
                ),
                optionsHindi = listOf("2-1-9-2", "2-1-8-2", "20-1-18-11", "2-1-9-11"),
                optionsEnglish = listOf("2-1-9-2", "2-1-8-2", "20-1-18-11", "2-1-9-11"),
                correctIndex = 0,
                deductionHindi = "नियम के अनुसार प्रत्येक अक्षर के वर्णमाला क्रमांक के अंकों का योग किया गया है: T(20)=2+0=2, A(1)=1, R(18)=1+8=9, K(11)=1+1=2। अतः 'TARK' = '2-1-9-2'।",
                deductionEnglish = "Each letter's numerical position is reduced to its single digital sum: T(20)->2, A(1)->1, R(18)->9, K(11)->2. Yields 2-1-9-2.",
                elimReasonsHindi = listOf("सही: 2-1-9-2 नियमों का 100% पालन करता है।", "गलत: R(18) का योग 8 नहीं बल्कि 9 होता है।", "गलत: अंकों का योग नहीं किया गया है।", "गलत: K(11) के अंकों का योग (1+1=2) अधूरा छोड़ दिया गया।"),
                elimReasonsEnglish = listOf("Correct: Exact digit sums.", "False: R=18 sums to 9, not 8.", "False: Raw unreduced positions.", "False: K(11) was not reduced."),
                expertHintHindi = "RING के प्रत्येक अक्षर के क्रमांक को देखें: R(18)=1+8=9, N(14)=1+4=5। TARK (20, 1, 18, 11) के लिए भी अंकों का योग करें।",
                expertHintEnglish = "Sum the individual digits of each letter's positional index: T(2+0), A(1), R(1+8), K(1+1).",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "2-1-8-2 और 20-1-18-11 दोनों डिजिटल रूट नियम का पालन नहीं करते।",
                fiftyFiftyProofEnglish = "Both violate the core digit-sum reduction constraint established in the example.",
                diagramType = "none"
            ),

            // ==========================================
            // Q10: 🏆 दूसरा पड़ाव (2nd Checkpoint - ₹3,20,000) - Game Theory & Probability
            // ==========================================
            createQuestion(
                qNumber = 10,
                category = "Bayesian Deduction Logic",
                questionHindi = "तीन बंद बक्से A, B, C हैं। एक में स्वर्ण पदक है और दो खाली हैं। आपने बक्सा A चुना। गेम मास्टर (जो जानता है पदक कहाँ है) बचे हुए दो बक्सों में से बक्सा C खोलता है जो खाली निकलता है। अब वह आपको विकल्प देता है: 'क्या आप बक्सा A पर टिके रहना चाहते हैं या बक्सा B पर स्विच करना चाहते हैं?' गणितीय संभावना के आधार पर कौन-सा निर्णय तार्किक रूप से श्रेष्ठ है?",
                questionEnglish = "There are 3 closed boxes A, B, C. One has gold, two are empty. You pick box A (1/3 chance). The host (who knows where gold is) opens box C, revealing it is empty. He offers: 'Do you stay with A or switch to B?' Mathematically (Monty Hall deduction), what is the optimal logical strategy?",
                cluesHindi = listOf(
                    "शुरुआत में बक्सा A चुनने पर जीतने की संभावना = 1/3",
                    "पदक बक्सा B या C में होने की संयुक्त संभावना = 2/3 थी।",
                    "जब होस्ट ने C (खाली) को हटा दिया, तो पूरी 2/3 संभावना अकेले बक्सा B पर केंद्रित हो गई।"
                ),
                cluesEnglish = listOf(
                    "Initial choice Box A probability of holding gold = 1/3.",
                    "Combined probability that gold is in {B or C} = 2/3.",
                    "Since host knowingly eliminated the empty Box C, the entire 2/3 probability shifts strictly to Box B."
                ),
                optionsHindi = listOf("बक्सा B पर स्विच करना (जीतने की संभावना 2/3 हो जाती है)", "बक्सा A पर ही बने रहना (संभावना अधिक है)", "दोनों में बराबर 50-50% संभावना है, कोई फर्क नहीं पड़ता", "स्विच करने से जीतने की संभावना घटकर 1/3 रह जाती है"),
                optionsEnglish = listOf("Switch to Box B (Winning probability becomes 2/3)", "Stay with Box A (Higher probability)", "Equal 50-50% chance, no difference", "Switching reduces chances to 1/3"),
                correctIndex = 0,
                deductionHindi = "प्रसिद्ध मोंटी हॉल प्रमेय (Monty Hall Theorem) के अनुसार: प्रारंभिक चयन (A) में 1/3 संभावना थी और शेष सेट (B+C) में 2/3 संभावना थी। होस्ट की जानकारीपूर्ण कार्रवाई C को शून्य कर देती है, जिससे B की संभावना 2/3 (66.7%) हो जाती है। अतः स्विच करना तार्किक रूप से 2 गुना बेहतर है।",
                deductionEnglish = "Under Bayes' theorem / Monty Hall logic: Initial pick A retains 1/3 probability. The remaining 2/3 probability pool collapses entirely onto unrevealed box B. Switching doubles winning odds to 2/3.",
                elimReasonsHindi = listOf("सही: बेयसियन प्रायिकता के अनुसार स्विच करने पर 2/3 संभावना।", "गलत: A में केवल 1/3 संभावना ही बनी रहती है।", "गलत: 50-50 भ्रम है क्योंकि होस्ट का विकल्प यादृच्छिक (random) नहीं बल्कि जानकारी-आधारित था।", "गलत: स्विच करने से संभावना घटती नहीं बल्कि दोगुनी होती है।"),
                elimReasonsEnglish = listOf("Correct: Switching yields 2/3 probability.", "False: Staying remains stuck at 1/3.", "False: 50-50 is a cognitive fallacy ignoring conditional probability.", "False: Switching doubles chances."),
                expertHintHindi = "प्रारंभिक चुनाव पर विचार करें: 3 में से 1 बार आप सही थे (1/3), 3 में से 2 बार सोना B या C में था (2/3)। होस्ट ने खाली बक्सा हटा दिया है।",
                expertHintEnglish = "Remember conditional probability: Your initial choice was right only 1/3 of the time, meaning 2/3 of the time the prize is in the other pair.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "A पर टिकना (1/3) और संभावना घटना दोनों बेयसियन नियम के प्रत्यक्ष विपरीत हैं।",
                fiftyFiftyProofEnglish = "Staying and reducing probability directly violate Bayes theorem calculations.",
                diagramType = "none"
            ),

            // ==========================================
            // Q11: Hard (No Limit) - Harmonic Acoustic Physics
            // ==========================================
            createQuestion(
                qNumber = 11,
                category = "Acoustic Physics Logic",
                questionHindi = "एक तनी हुई गिटार की तार की मूल आवृत्ति (Fundamental Frequency) 440 Hz (Note A4) है। यदि तार की लंबाई और तनाव को स्थिर रखते हुए केवल तार के रैखिक द्रव्यमान घनत्व (Linear Mass Density) को 4 गुना भारी कर दिया जाए, तो नई मूल आवृत्ति क्या होगी?\n(सूत्र: f = (1/2L) × √(T / μ))",
                questionEnglish = "A vibrating string produces a fundamental tone of 440 Hz. If the string's length (L) and tension (T) are kept strictly constant, but its linear mass density (μ) is increased by 4 times (4μ), what is the new fundamental frequency?\n(Formula: f ∝ 1 / √μ)",
                cluesHindi = listOf(
                    "आवृत्ति (f) द्रव्यमान घनत्व के वर्गमूल के व्युत्क्रमानुपाती होती है: f ∝ 1 / √μ",
                    "यदि μ' = 4μ, तो √μ' = √4 = 2",
                    "नई आवृत्ति f' = f / 2 = 440 / 2"
                ),
                cluesEnglish = listOf(
                    "Frequency is inversely proportional to square root of mass density: f ∝ 1 / √μ.",
                    "If μ' = 4μ, then √μ' = √4 = 2.",
                    "New frequency f' = f / 2 = 440 / 2."
                ),
                optionsHindi = listOf("220 Hz (ठीक आधी / एक अष्टक नीचे)", "110 Hz", "880 Hz (दोगुनी)", "440 Hz (अपरिवर्तित)"),
                optionsEnglish = listOf("220 Hz (Half / One octave lower)", "110 Hz", "880 Hz (Double)", "440 Hz (Unchanged)"),
                correctIndex = 0,
                deductionHindi = "चूँकि f ∝ 1/√μ, घनत्व 4 गुना होने पर हर (denominator) में √4 = 2 आ जाता है। f_new = 440 / 2 = 220 Hz।",
                deductionEnglish = "Since f ∝ 1/√μ, quadrupling mass density places √4 = 2 in the denominator: 440 / 2 = 220 Hz.",
                elimReasonsHindi = listOf("सही: 440 / √4 = 220 Hz", "गलत: 110 Hz घनत्व 16 गुना होने पर होता।", "गलत: 880 Hz तनाव 4 गुना बढ़ाने पर होता, भारी तार धीमा कंपन करता है।", "गलत: भारी तार की आवृत्ति अपरिवर्तित नहीं रह सकती।"),
                elimReasonsEnglish = listOf("Correct: Exactly 440 / 2 = 220 Hz.", "False: 110 Hz corresponds to 16x density.", "False: 880 Hz would require 4x tension, heavier strings vibrate slower.", "False: Frequency cannot remain constant."),
                expertHintHindi = "सूत्र में वर्गमूल (Square root) देखें: 4 का वर्गमूल 2 होता है। चूंकि घनत्व नीचे (हर में) है, मूल संख्या को 2 से भाग दें।",
                expertHintEnglish = "Notice the inverse square root relation: √4 = 2 in the denominator, so divide 440 by 2.",
                fiftyFiftyDiscards = listOf(2, 3),
                fiftyFiftyProofHindi = "880 Hz और 440 Hz दोनों भौतिकी के द्रव्यमान-कंपन नियम का खंडन करते हैं।",
                fiftyFiftyProofEnglish = "880 Hz and 440 Hz violate the inverse square root law of acoustic string physics.",
                diagramType = "none",
                audioPatternType = "harmonic_interval"
            ),

            // ==========================================
            // Q12: Hard+ (No Limit) - Spatial Topology & Graph Theory
            // ==========================================
            createQuestion(
                qNumber = 12,
                category = "Graph Theory Topology",
                questionHindi = "ऑयलर के नेटवर्क सिद्धांत (Eulerian Path Theorem) के अनुसार: किसी ग्राफ में बिना किसी रेखा को दोहराए और बिना पेन उठाए एक सतत रेखा में तभी घूमा जा सकता है, जब विषम कोटि (Odd Degree: जहाँ से विषम संख्या में रेखाएं निकलती हों) वाले शीर्षों (Vertices) की संख्या कितनी हो?",
                questionEnglish = "According to Euler's Graph Theorem (Königsberg Bridge Principle), an Eulerian Trail (traversing every edge exactly once without lifting the pen) is mathematically possible if and only if the number of odd-degree vertices in the graph is exactly:",
                cluesHindi = listOf(
                    "प्रत्येक आंतरिक पारगमन के लिए एक प्रवेश रेखा और एक निकास रेखा (सम संख्या) चाहिए।",
                    "यदि शुरुआत और अंत अलग-अलग बिंदुओं पर हों, तो केवल 2 विषम बिंदु (शुरुआत और अंत) हो सकते हैं।",
                    "यदि शुरुआत और अंत एक ही बिंदु पर हों (Eulerian Circuit), तो 0 विषम बिंदु हो सकते हैं।"
                ),
                cluesEnglish = listOf(
                    "Every intermediate pass-through vertex consumes 2 edges (1 entry + 1 exit).",
                    "If start and end vertices differ, exactly 2 vertices have odd degrees (start and finish).",
                    "If start and end coincide (closed circuit), exactly 0 vertices have odd degrees."
                ),
                optionsHindi = listOf("ठीक 0 या 2", "ठीक 1 या 3", "हमेशा 4", "कोई भी विषम संख्या"),
                optionsEnglish = listOf("Exactly 0 or 2", "Exactly 1 or 3", "Always 4", "Any odd integer"),
                correctIndex = 0,
                deductionHindi = "ऑयलर के प्रमेय के अनुसार: यदि सभी शीर्षों की कोटि सम है (0 विषम), तो यूलरियन परिपथ बनता है। यदि ठीक 2 शीर्षों की कोटि विषम है, तो यूलरियन पथ बनता है (एक विषम से शुरू होकर दूसरे पर समाप्त)। किसी भी अप्रत्यक्ष ग्राफ में विषम शीर्षों की कुल संख्या हमेशा सम (0, 2, 4...) ही होती है, अतः 1 या 3 असंभव हैं।",
                deductionEnglish = "Euler's fundamental theorem dictates that an open/closed Eulerian path exists if and only if the graph has exactly 0 or 2 vertices of odd degree.",
                elimReasonsHindi = listOf("सही: ऑयलर प्रमेय की सार्वभौमिक गणितीय शर्त।", "गलत: किसी भी ग्राफ में विषम शीर्षों की संख्या स्वयं विषम (1 या 3) कभी नहीं हो सकती (Handshaking Lemma)।", "गलत: 4 विषम बिंदुओं वाला ग्राफ कभी एक स्ट्रोक में नहीं खींचा जा सकता।", "गलत: विषम संख्या होना असंभव है।"),
                elimReasonsEnglish = listOf("Correct: Proven by Euler and the Handshaking Lemma.", "False: A graph cannot even possess an odd count of odd vertices.", "False: 4 odd vertices requires at least 2 distinct strokes.", "False: Violates basic graph invariant."),
                expertHintHindi = "सोचिए: हर बार जब आप किसी बिंदु से गुजरते हैं, तो 2 रेखाएं इस्तेमाल होती हैं (आना + जाना)। केवल प्रस्थान और अंतिम गंतव्य बिंदु ही विषम हो सकते हैं।",
                expertHintEnglish = "Consider that every intermediate vertex requires paired in-and-out edges. Only the starting and ending nodes can be unbalanced.",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "हैंडशेकिंग लेम्मा के अनुसार 1 या 3 विषम शीर्ष वाला ग्राफ अस्तित्व में ही नहीं हो सकता।",
                fiftyFiftyProofEnglish = "Odd vertex count parity theorem proves 1, 3, or 4 are impossible.",
                diagramType = "none"
            ),

            // ==========================================
            // Q13: Very Hard (No Limit) - Recursive Mathematical Induction
            // ==========================================
            createQuestion(
                qNumber = 13,
                category = "Recursive Combinatorics",
                questionHindi = "टावर ऑफ हनोई (Tower of Hanoi) पहेली में 5 विभिन्न आकारों की डिस्क को नियमबद्ध तरीके से (बड़ी डिस्क छोटी के ऊपर कभी नहीं रखी जा सकती) खंभा A से खंभा C पर ले जाने के लिए आवश्यक न्यूनतम चालों (Minimum Moves) की संख्या क्या होगी?\n(सुराग: n डिस्कों के लिए न्यूनतम चाल = 2ⁿ - 1)",
                questionEnglish = "In the classic Tower of Hanoi puzzle with n = 5 disks, moving all disks from peg A to peg C following the strict rule that a larger disk can never be placed on top of a smaller one requires a minimum of how many moves?\n(Formula: M(n) = 2ⁿ - 1)",
                cluesHindi = listOf(
                    "पुनरावृत्ति संबंध: M(n) = 2 × M(n-1) + 1",
                    "n = 1 के लिए: 2¹ - 1 = 1",
                    "n = 5 के लिए: 2⁵ - 1 = 32 - 1"
                ),
                cluesEnglish = listOf(
                    "Recurrence relation: M(n) = 2 × M(n-1) + 1.",
                    "Base case n=1: 2¹ - 1 = 1 move.",
                    "For n=5: calculate 2⁵ - 1 = 32 - 1."
                ),
                optionsHindi = listOf("31 चालें", "25 चालें", "32 चालें", "63 चालें"),
                optionsEnglish = listOf("31 moves", "25 moves", "32 moves", "63 moves"),
                correctIndex = 0,
                deductionHindi = "n डिस्कों के लिए न्यूनतम चाल = 2ⁿ - 1। n = 5 के लिए: 2⁵ = 32। 32 - 1 = 31 चालें।",
                deductionEnglish = "The minimum move formula for n disks is 2ⁿ - 1. For n = 5: 2⁵ = 32. 32 - 1 = 31 moves.",
                elimReasonsHindi = listOf("सही: 2⁵ - 1 = 32 - 1 = 31।", "गलत: 25 केवल 5² है, जो गलत सूत्र है।", "गलत: 32 में -1 नहीं घटाया गया।", "गलत: 63 चालें 6 डिस्कों (2⁶ - 1) के लिए होती हैं।"),
                elimReasonsEnglish = listOf("Correct: 2⁵ - 1 = 31.", "False: 25 is 5², mathematically invalid.", "False: 32 forgot to subtract 1.", "False: 63 corresponds to n = 6 disks."),
                expertHintHindi = "2 की घात 5 (2 × 2 × 2 × 2 × 2) की गणना करें और परिणाम में से 1 घटाएं।",
                expertHintEnglish = "Compute 2 to the power of 5 (32) and subtract 1.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "25 और 63 क्रमशः 5² और 2⁶ - 1 हैं, जो n=5 के लिए अमान्य हैं।",
                fiftyFiftyProofEnglish = "25 and 63 are mathematically mismatched powers.",
                diagramType = "none"
            ),

            // ==========================================
            // Q14: Very Hard (No Limit) - Forensic Logic & Truth-Tellers
            // ==========================================
            createQuestion(
                qNumber = 14,
                category = "Knights & Knaves Logic",
                questionHindi = "एक द्वीप पर दो प्रकार के निवासी हैं: 'सत्यवादी' (जो सदैव 100% सच बोलते हैं) और 'मिथ्यावादी' (जो सदैव 100% झूठ बोलते हैं)।\nआप निवासी A और B से मिलते हैं।\nA कहता है: 'हम दोनों में से कम से कम एक मिथ्यावादी है।'\nइस कथन के तार्किक विश्लेषण से A और B का वास्तविक चरित्र क्या सिद्ध होता है?",
                questionEnglish = "On a logic island, Knights always tell the truth, Knaves always lie. You meet inhabitants A and B.\nA states: 'At least one of us is a Knave.'\nThrough strict deductive logic, what are the true identities of A and B?",
                cluesHindi = listOf(
                    "मान लीजिए A मिथ्यावादी (झूठा) है: तो उसका कथन 'कम से कम एक झूठा है' सच हो जाएगा! लेकिन झूठा सच नहीं बोल सकता (विरोधाभास / Contradiction)। अतः A झूठा नहीं हो सकता; A सत्यवादी है।",
                    "चूँकि A सत्यवादी है, उसका कथन 'कम से कम एक मिथ्यावादी है' 100% सच होना चाहिए।",
                    "चूँकि A स्वयं सत्यवादी है, अतः वह 'कम से कम एक मिथ्यावादी' केवल B ही हो सकता है।"
                ),
                cluesEnglish = listOf(
                    "Hypothesis 1: If A is a Knave (liar), his statement 'At least one of us is a Knave' would be TRUE. But a Knave cannot tell the truth (Contradiction). Therefore, A MUST be a Knight.",
                    "Since A is a Knight, his statement 'At least one of us is a Knave' MUST be factually true.",
                    "Since A is a Knight, the required Knave must be B."
                ),
                optionsHindi = listOf("A सत्यवादी है, B मिथ्यावादी है", "दोनों सत्यवादी हैं", "दोनों मिथ्यावादी हैं", "A मिथ्यावादी है, B सत्यवादी है"),
                optionsEnglish = listOf("A is a Knight, B is a Knave", "Both are Knights", "Both are Knaves", "A is a Knave, B is a Knight"),
                correctIndex = 0,
                deductionHindi = "यदि A झूठा होता तो उसका कथन सच साबित होता जो असंभव है। अतः A सत्यवादी है। A का कथन सच होने के लिए समूह में एक झूठा होना जरूरी है, अतः B अनिवार्य रूप से मिथ्यावादी है।",
                deductionEnglish = "A cannot be a Knave without creating a paradox. Thus A is a Knight. For A's true statement to hold, B must be a Knave.",
                elimReasonsHindi = listOf("सही: तार्किक विरोधाभास निवारण द्वारा सिद्ध।", "गलत: यदि दोनों सत्यवादी होते तो 'कम से कम एक झूठा है' का कथन असत्य हो जाता।", "गलत: A झूठा हो ही नहीं सकता।", "गलत: A का झूठा होना असंभव है।"),
                elimReasonsEnglish = listOf("Correct: Unique logically consistent state.", "False: If both were Knights, A's statement would be false, making A a liar (contradiction).", "False: Knave A speaking truth is impossible.", "False: A cannot be a Knave."),
                expertHintHindi = "मानकर देखिए कि क्या A झूठ बोल सकता है? यदि A झूठ बोल रहा है, तो 'कम से कम एक झूठा है' सच कैसे हो गया?",
                expertHintEnglish = "Test the hypothesis 'A is a liar'. Notice that if A is a liar, his claim that someone is a liar becomes true, creating an impossible contradiction.",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "दोनों सत्यवादी या दोनों मिथ्यावादी होना कथन में सीधा तार्किक विरोधाभास पैदा करता है।",
                fiftyFiftyProofEnglish = "Both Knights and Both Knaves produce immediate semantic contradictions.",
                diagramType = "none"
            ),

            // ==========================================
            // Q15: Expert (No Limit - ₹1 Crore) - Relativity & Time Dilation
            // ==========================================
            createQuestion(
                qNumber = 15,
                category = "Relativistic Physics Deduction",
                questionHindi = "विशेष सापेक्षता (Special Relativity) के अनुसार लॉरेंट्ज़ कारक γ = 1 / √(1 - v²/c²) है। यदि एक अंतरिक्ष यान प्रकाश की चाल के 60% (v = 0.6c) वेग से गति करता है, तो पृथ्वी पर स्थिर प्रेक्षक के 10 घंटे अंतरिक्ष यान की घड़ी में कितने घंटों के बराबर दर्ज होंगे?\n(सुराग: Δt' = Δt × √(1 - v²/c²))",
                questionEnglish = "Under Special Relativity, time dilation factor is given by √(1 - v²/c²). If a spacecraft travels at 60% the speed of light (v = 0.6c), how many hours elapse on the spaceship's clock during 10 hours recorded on Earth?",
                cluesHindi = listOf(
                    "v/c = 0.6 -> (v/c)² = 0.36",
                    "1 - (v/c)² = 1 - 0.36 = 0.64",
                    "√(0.64) = 0.8",
                    "यान का समय = 10 × 0.8"
                ),
                cluesEnglish = listOf(
                    "Velocity fraction: v/c = 0.6 -> (0.6)² = 0.36.",
                    "Invariant term: 1 - 0.36 = 0.64.",
                    "Square root: √(0.64) = 0.8.",
                    "Proper ship time = 10 × 0.8."
                ),
                optionsHindi = listOf("8 घंटे", "6 घंटे", "12.5 घंटे", "10 घंटे (समान)"),
                optionsEnglish = listOf("8 hours", "6 hours", "12.5 hours", "10 hours (Identical)"),
                correctIndex = 0,
                deductionHindi = "√(1 - 0.6²) = √(1 - 0.36) = √0.64 = 0.8। यान का उचित समय (Proper time) Δt' = 10 × 0.8 = 8 घंटे।",
                deductionEnglish = "√(1 - 0.6²) = √0.64 = 0.8. Ship's dilated proper time = 10 × 0.8 = 8 hours.",
                elimReasonsHindi = listOf("सही: 10 × √(1 - 0.36) = 10 × 0.8 = 8 घंटे।", "गलत: 6 घंटे v = 0.8c के लिए होता।", "गलत: 12.5 घंटे पृथ्वी के समय का व्युत्क्रम है।", "गलत: सापेक्ष वेग में समय कभी अपरिवर्तित नहीं रहता।"),
                elimReasonsEnglish = listOf("Correct: 10 × 0.8 = 8 hours.", "False: 6 hours corresponds to v = 0.8c.", "False: 12.5 hours is inverse frame scaling.", "False: Time dilation is non-zero at 0.6c."),
                expertHintHindi = "0.6 का वर्ग 0.36 होता है। 1 में से 0.36 घटाने पर 0.64 बचता है। 0.64 का वर्गमूल 0.8 है। 10 को 0.8 से गुणा करें।",
                expertHintEnglish = "Square 0.6 (0.36). Subtract from 1 (0.64). Take square root (0.8). Multiply 10 by 0.8.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "6 घंटे और 10 घंटे दोनों लॉरेंट्ज़ रूपांतरण समीकरण की गणना का खंडन करते हैं।",
                fiftyFiftyProofEnglish = "6h and 10h contradict the Pythagorean relativity ratio 3:4:5.",
                diagramType = "none"
            ),

            // ==========================================
            // Q16: 🏆 तीसरा पड़ाव (3rd Checkpoint - ₹3 Crore) - Information Entropy & Optimal Binary Search
            // ==========================================
            createQuestion(
                qNumber = 16,
                category = "Information Theory Logic",
                questionHindi = "शैनन सूचना सिद्धांत (Shannon Information Entropy) के अनुसार: 1 से 1024 के बीच की एक अज्ञात संख्या को केवल 'हाँ/नहीं' प्रश्नों द्वारा 100% निश्चितता से खोजने के लिए न्यूनतम कितने इष्टतम (Optimal Binary Search) प्रश्नों की आवश्यकता होगी?\n(सुराग: 1024 = 2¹⁰, सूचना सामग्री I = log₂(N))",
                questionEnglish = "By Shannon Information Theory & Optimal Binary Search, to identify an unknown integer between 1 and 1024 with 100% mathematical certainty using only 'Yes/No' queries, what is the absolute minimum number of questions required in the worst-case?\n(Formula: Information bits = log₂(1024))",
                cluesHindi = listOf(
                    "प्रत्येक 'हाँ/नहीं' प्रश्न संभावना स्थान को ठीक 2 से विभाजित (आधा) करता है: 1 बिट सूचना।",
                    "1024 = 2 × 2 × 2 × 2 × 2 × 2 × 2 × 2 × 2 × 2 = 2¹⁰",
                    "log₂(1024) = 10"
                ),
                cluesEnglish = listOf(
                    "Each optimal binary query halves the remaining search space, providing exactly 1 bit of Shannon information.",
                    "1024 = 2¹⁰.",
                    "log₂(1024) = 10 bits."
                ),
                optionsHindi = listOf("10 प्रश्न", "9 प्रश्न", "11 प्रश्न", "512 प्रश्न"),
                optionsEnglish = listOf("10 questions", "9 questions", "11 questions", "512 questions"),
                correctIndex = 0,
                deductionHindi = "प्रत्येक प्रश्न से सर्च स्पेस आधा होता है: 1024 -> 512 -> 256 -> 128 -> 64 -> 32 -> 16 -> 8 -> 4 -> 2 -> 1। कुल 10 विभाजन। 2¹⁰ = 1024। अतः न्यूनतम 10 प्रश्न पर्याप्त और आवश्यक हैं।",
                deductionEnglish = "Binary partitioning: 1024 / 2¹⁰ = 1 unique state. log₂(1024) = 10 optimal questions guarantee isolating the number.",
                elimReasonsHindi = listOf("सही: 2¹⁰ = 1024, log₂(1024) = 10।", "गलत: 9 प्रश्नों से 2⁹ = 512 संख्याएं ही जांची जा सकती हैं, 1 संख्या में अस्पष्टता रह जाएगी।", "गलत: 11 प्रश्न अतिरिक्त हैं, 10 में ही काम पूरा हो जाता है।", "गलत: 512 रैखिक खोज (Linear search) का औसत है, बाइनरी नहीं।"),
                elimReasonsEnglish = listOf("Correct: log₂(1024) = 10.", "False: 9 questions only disambiguate 2⁹ = 512 states.", "False: 11 is redundant.", "False: 512 represents naive linear search."),
                expertHintHindi = "1024 को 2 की किस घात के रूप में लिखा जा सकता है? 2 × 2 × 2... कितनी बार 1024 बनाता है?",
                expertHintEnglish = "What power of 2 equals 1024? Count how many times you must divide 1024 by 2 to reach 1.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "9 प्रश्न केवल 512 राज्यों को हल कर सकते हैं और 512 प्रश्न बाइनरी सर्च के सिद्धांत का खंडन है।",
                fiftyFiftyProofEnglish = "9 is insufficient (2⁹=512) and 512 ignores binary search optimization.",
                diagramType = "none"
            ),

            // ==========================================
            // Q17: 👑 अंतिम महा-तर्क (Final Crown - ₹7 Crore) - Gödel-Turing Incomputability & Fixed-Point Logic
            // ==========================================
            createQuestion(
                qNumber = 17,
                category = "Meta-Logic & Diagonalization",
                questionHindi = "केंटर के विकर्ण प्रमेय (Cantor's Diagonalization) और ट्यूरिंग के हॉल्टिंग प्रमेय (Turing Halting Problem) के मूल तर्क के अनुसार:\nयदि हम मान लें कि एक प्रोग्राम 'H(P, I)' है जो किसी भी प्रोग्राम P के इनपुट I पर रुकने (Halt) या अनंत लूप में जाने का 100% सही निर्णय दे सकता है।\nअब हम एक विरोधी प्रोग्राम 'D(X)' बनाते हैं: 'यदि H(X, X) कहता है कि X रुकेगा, तो D अनंत लूप में चला जाए; और यदि H(X, X) कहता है कि लूप में जाएगा, तो D तुरंत रुक जाए।'\nजब हम 'D(D)' चलाते हैं, तो उत्पन्न होने वाला तार्किक निष्कर्ष क्या सिद्ध करता है?",
                questionEnglish = "Under the Cantor Diagonalization & Turing Halting Theorem: Assume a universal program H(P,I) exists that decides whether any program P halts on input I. We construct an adversarial program D(X): 'If H(X,X) says X halts, D loops forever; if H(X,X) says X loops, D halts immediately.' When evaluating D(D), what fundamental mathematical truth is proven?",
                cluesHindi = listOf(
                    "यदि D(D) रुकता है, तो D की परिभाषा के अनुसार उसे अनंत लूप में जाना होगा (विरोधाभास!)।",
                    "यदि D(D) लूप में जाता है, तो D की परिभाषा के अनुसार उसे तुरंत रुकना होगा (विरोधाभास!)।",
                    "चूँकि दोनों स्थितियाँ विरोधाभास पैदा करती हैं, अतः हमारी प्रारंभिक मान्यता (कि ऐसा सार्वभौमिक प्रोग्राम H अस्तित्व में हो सकता है) असत्य सिद्ध होती है।"
                ),
                cluesEnglish = listOf(
                    "Case 1: If D(D) halts, D's code forces it to loop forever -> Contradiction.",
                    "Case 2: If D(D) loops, D's code forces it to halt immediately -> Contradiction.",
                    "Since both mutually exhaustive cases yield a direct logical paradox, the initial assumption that a universal Halting decider H can exist is proven FALSE."
                ),
                optionsHindi = listOf(
                    "सार्वभौमिक हॉल्टिंग विश्लेषक प्रोग्राम H का अस्तित्व असंभव है (हॉल्टिंग समस्या अनिर्णायक है)",
                    "कंप्यूटर हमेशा के लिए हैंग हो जाएगा और हार्डवेयर जल जाएगा",
                    "D(D) केवल 50% समय सही चलेगा",
                    "यह प्रोग्राम केवल क्वांटम कंप्यूटर पर हल हो सकता है"
                ),
                optionsEnglish = listOf(
                    "A universal Halting decider program H cannot logically exist (Halting Problem is Undecidable)",
                    "The computer hardware will overheat and melt",
                    "D(D) will run correctly exactly 50% of the time",
                    "This paradox can be resolved only on a quantum computer"
                ),
                correctIndex = 0,
                deductionHindi = "प्रसिद्ध ट्यूरिंग प्रमेय (1936): D(D) का मूल्यांकन एक स्व-विरोधाभासी तर्क (Self-referential paradox: H(D,D) = Halt ⇔ D(D) Loops) उत्पन्न करता है। रिडक्टियो ऐड एब्जर्डम (Reductio ad absurdum) द्वारा यह गणितीय रूप से सिद्ध होता है कि कोई भी ऐसा एल्गोरिथ्म संभव नहीं है जो सभी प्रोग्रामों के हॉल्टिंग व्यवहार का सामान्य निर्णय दे सके।",
                deductionEnglish = "By Reductio ad Absurdum (diagonal proof): Evaluating D(D) yields D(D) halts if and only if D(D) loops. This paradox mathematically proves that the Halting Problem is strictly undecidable for any computational system (classical or quantum).",
                elimReasonsHindi = listOf(
                    "सही: कंप्यूटर विज्ञान और गणित का आधारभूत अनिर्णायकता प्रमेय (Undecidability Theorem)।",
                    "गलत: यह तार्किक सीमा है, भौतिक हार्डवेयर दोष नहीं।",
                    "गलत: यह 50% प्रायिकता नहीं बल्कि पूर्ण सैद्धांतिक असंभवता है।",
                    "गलत: क्वांटम कंप्यूटर भी ट्यूरिंग-समतुल्य (Turing-equivalent) होते हैं और हॉल्टिंग समस्या को हल नहीं कर सकते।"
                ),
                elimReasonsEnglish = listOf(
                    "Correct: Foundational theorem of computer science proving computational limits.",
                    "False: Pure logic boundary, not hardware damage.",
                    "False: Pure paradox, not a probabilistic distribution.",
                    "False: Quantum computers are Turing-equivalent and cannot solve the Halting problem."
                ),
                expertHintHindi = "विरोधाभास पर ध्यान दें: यदि यह रुकेगा तो लूप करेगा, यदि लूप करेगा तो रुकेगा। जब कोई परिकल्पना स्वयं का खंडन कर दे, तो प्रारंभिक मान्यता (प्रोग्राम H का अस्तित्व) गलत सिद्ध होती है।",
                expertHintEnglish = "Analyze the self-referential paradox: If Halts -> Loops; If Loops -> Halts. A premise that logically refutes itself proves the non-existence of the assumed solver H.",
                fiftyFiftyDiscards = listOf(1, 2),
                fiftyFiftyProofHindi = "हार्डवेयर खराबी और 50% संभावना दोनों तार्किक अनिर्णायकता प्रमेय का खंडन करते हैं।",
                fiftyFiftyProofEnglish = "Hardware failure and 50% probability are distractors ignoring mathematical proof by contradiction.",
                diagramType = "none"
            )
        )
    }

    private fun createQuestion(
        qNumber: Int,
        category: String,
        questionHindi: String,
        questionEnglish: String,
        cluesHindi: List<String>,
        cluesEnglish: List<String>,
        optionsHindi: List<String>,
        optionsEnglish: List<String>,
        correctIndex: Int,
        deductionHindi: String,
        deductionEnglish: String,
        elimReasonsHindi: List<String>,
        elimReasonsEnglish: List<String>,
        expertHintHindi: String,
        expertHintEnglish: String,
        fiftyFiftyDiscards: List<Int>,
        fiftyFiftyProofHindi: String,
        fiftyFiftyProofEnglish: String,
        diagramType: String = "none",
        diagramData: String = "",
        audioPatternType: String? = null
    ): QuestionItem {
        val meta = GeminiApiClient.getTierDetails(qNumber)
        val fingerprint = "default_q_${qNumber}_${category.replace(" ", "_")}"
        return QuestionItem(
            id = "seed_$fingerprint",
            qNumber = qNumber,
            difficultyTitle = meta.difficultyTitle,
            timeLimitSeconds = meta.timeLimitSeconds,
            prizePoints = meta.prizePoints,
            prizeFormatted = meta.prizeFormatted,
            isCheckpoint = meta.isCheckpoint,
            checkpointTitle = meta.checkpointTitle,
            category = category,
            questionHindi = questionHindi,
            questionEnglish = questionEnglish,
            cluesHindi = cluesHindi,
            cluesEnglish = cluesEnglish,
            optionsHindi = optionsHindi,
            optionsEnglish = optionsEnglish,
            correctAnswerIndex = correctIndex,
            deductionPathHindi = deductionHindi,
            deductionPathEnglish = deductionEnglish,
            eliminationReasonsHindi = elimReasonsHindi,
            eliminationReasonsEnglish = elimReasonsEnglish,
            expertAdviceHindi = expertHintHindi,
            expertAdviceEnglish = expertHintEnglish,
            fiftyFiftyDiscardIndices = fiftyFiftyDiscards,
            fiftyFiftyProofHindi = fiftyFiftyProofHindi,
            fiftyFiftyProofEnglish = fiftyFiftyProofEnglish,
            diagramType = diagramType,
            diagramData = diagramData,
            audioPatternType = audioPatternType,
            semanticFingerprint = fingerprint
        )
    }

    private fun getJuniorStudentQuestions(): List<QuestionItem> {
        return listOf(
            // Junior Q1: Primary Solar Logic
            createQuestion(
                qNumber = 1,
                category = "Junior Solar System Logic",
                questionHindi = "सौरमंडल में सूर्य से बढ़ती दूरी के क्रम में: बुध (1st), शुक्र (2nd), पृथ्वी (3rd), मंगल (4th)। यदि एक अंतरिक्ष यान पृथ्वी से सीधे सूर्य की ओर 1 ग्रह कदम पीछे जाता है, तो वह किस ग्रह पर पहुंचेगा?",
                questionEnglish = "In the Solar System from the Sun outward: Mercury (1st), Venus (2nd), Earth (3rd), Mars (4th). If a spacecraft moves 1 planet step backward from Earth towards the Sun, which planet will it reach?",
                cluesHindi = listOf(
                    "पृथ्वी सूर्य से तीसरा (3rd) ग्रह है।",
                    "सूर्य की ओर 1 कदम पीछे जाने का अर्थ है: 3 - 1 = दूसरा (2nd) ग्रह।",
                    "दूसरे स्थान पर स्थित ग्रह 'शुक्र' (Venus) है।"
                ),
                cluesEnglish = listOf(
                    "Earth is the 3rd planet from the Sun.",
                    "Moving 1 step backward towards the Sun = (3 - 1) = 2nd planet.",
                    "The 2nd planet from the Sun is Venus."
                ),
                optionsHindi = listOf("शुक्र (Venus)", "बुध (Mercury)", "मंगल (Mars)", "बृहस्पति (Jupiter)"),
                optionsEnglish = listOf("Venus", "Mercury", "Mars", "Jupiter"),
                correctIndex = 0,
                deductionHindi = "पृथ्वी का क्रम = 3। सूर्य की ओर जाने पर क्रम संख्या घटेगी: 3 - 1 = 2। क्रम 2 पर शुक्र ग्रह स्थित है।",
                deductionEnglish = "Earth order index is 3. Moving towards the Sun decrements position by 1 (3 - 1 = 2). Position 2 corresponds strictly to Venus.",
                elimReasonsHindi = listOf("सही उत्तर (शुक्र दूसरा ग्रह है)", "गलत: बुध पहला ग्रह है (2 कदम पीछे)", "गलत: मंगल सूर्य से दूर (आगे) है", "गलत: बृहस्पति 5वाँ ग्रह है"),
                elimReasonsEnglish = listOf("Correct: Venus is 2nd.", "False: Mercury is 1st.", "False: Mars is outward.", "False: Jupiter is 5th."),
                expertHintHindi = "कक्षा में याद की गई सौरमंडल की ग्रह पंक्ति को सूर्य से क्रमबद्ध करें: बुध -> शुक्र -> पृथ्वी -> मंगल।",
                expertHintEnglish = "Recall the planetary sequence outward from the Sun: Mercury -> Venus -> Earth -> Mars.",
                fiftyFiftyDiscards = listOf(2, 3),
                fiftyFiftyProofHindi = "मंगल और बृहस्पति सूर्य से दूर की ओर हैं, अंदर की ओर नहीं।",
                fiftyFiftyProofEnglish = "Mars and Jupiter lie outward away from the Sun, not inward."
            ),
            // Junior Q2: Balance Scale Logic
            createQuestion(
                qNumber = 2,
                category = "Balance Scale Weight Logic",
                questionHindi = "तराजू के एक पलड़े पर 1 तरबूज = 3 सेब हैं। दूसरे पलड़े पर 1 सेब = 2 संतरे हैं। तो 1 तरबूज का वजन कितने संतरों के बराबर होगा?",
                questionEnglish = "On a balance scale, 1 Watermelon = 3 Apples. Also, 1 Apple = 2 Oranges. How many Oranges balance exactly 1 Watermelon?",
                cluesHindi = listOf(
                    "नियम 1: 1 तरबूज (W) = 3 सेब (A)",
                    "नियम 2: 1 सेब (A) = 2 संतरे (O)",
                    "प्रतिस्थापन: W = 3 × (2 संतरे)"
                ),
                cluesEnglish = listOf(
                    "Rule 1: 1 Watermelon (W) = 3 Apples (A)",
                    "Rule 2: 1 Apple (A) = 2 Oranges (O)",
                    "Substitution: W = 3 × (2 Oranges)"
                ),
                optionsHindi = listOf("6 संतरे (6 Oranges)", "5 संतरे (5 Oranges)", "4 संतरे (4 Oranges)", "8 संतरे (8 Oranges)"),
                optionsEnglish = listOf("6 Oranges", "5 Oranges", "4 Oranges", "8 Oranges"),
                correctIndex = 0,
                deductionHindi = "W = 3 × A। चूँकि A = 2 O, अतः W = 3 × 2 = 6 संतरे। यह गणितीय गुणन नियम द्वारा 100% सिद्ध है।",
                deductionEnglish = "W = 3 × A. Substituting A = 2 O gives W = 3 × 2 = 6 Oranges. Derived through direct algebraic substitution.",
                elimReasonsHindi = listOf("सही: 3 × 2 = 6 संतरे।", "गलत: जोड़ (3+2=5) नहीं, गुणा होगा।", "गलत: कम है।", "गलत: अधिक है।"),
                elimReasonsEnglish = listOf("Correct: 3 * 2 = 6.", "False: Addition error (3+2).", "False: Too low.", "False: Too high."),
                expertHintHindi = "प्रत्येक सेब की जगह 2-2 संतरे रखकर कुल संतरों की गिनती करें।",
                expertHintEnglish = "Substitute 2 oranges in place of each of the 3 apples.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "5 संतरे जोड़ की गलती है और 8 संतरे 4 सेबों का मान होता।",
                fiftyFiftyProofEnglish = "5 is an addition error, and 8 corresponds to 4 apples."
            ),
            // Junior Q3: Clock Hand Angle Logic
            createQuestion(
                qNumber = 3,
                category = "Clock Geometry Logic",
                questionHindi = "एक दीवार घड़ी में ठीक 3:00 बजे हैं। घंटे की सुई (3 पर) और मिनट की सुई (12 पर) के बीच का छोटा कोण कितने डिग्री का होता है?",
                questionEnglish = "A wall clock displays exactly 3:00. What is the smaller angle between the hour hand (at 3) and minute hand (at 12)?",
                cluesHindi = listOf(
                    "घड़ी का पूरा डायल = 360° (12 घंटे में विभाजित)।",
                    "प्रत्येक 1 घंटे का अंतराल = 360° / 12 = 30°।",
                    "12 से 3 के बीच कुल 3 घंटे के अंतराल हैं = 3 × 30°।"
                ),
                cluesEnglish = listOf(
                    "Full clock circle = 360° divided into 12 hours.",
                    "Each 1-hour division = 360° / 12 = 30°.",
                    "From 12 to 3, there are exactly 3 hour segments = 3 × 30°."
                ),
                optionsHindi = listOf("90° (समकोण / Right Angle)", "60°", "120°", "180° (सरल कोण)"),
                optionsEnglish = listOf("90° (Right Angle)", "60°", "120°", "180° (Straight line)"),
                correctIndex = 0,
                deductionHindi = "3 घंटे के अंतराल × 30° प्रति घंटा = 90°। 3:00 बजे दोनों सुइयां एक-दूसरे पर लंबवत (समकोण) होती हैं।",
                deductionEnglish = "3 hour divisions × 30° per hour = 90°. At 3:00, the hands are strictly perpendicular (right angle).",
                elimReasonsHindi = listOf("सही: 3 × 30° = 90° समकोण।", "गलत: 60° ठीक 2:00 बजे बनता है।", "गलत: 120° ठीक 4:00 बजे बनता है।", "गलत: 180° ठीक 6:00 बजे बनता है।"),
                elimReasonsEnglish = listOf("Correct: 3 * 30° = 90°.", "False: 60° occurs at 2:00.", "False: 120° occurs at 4:00.", "False: 180° occurs at 6:00."),
                expertHintHindi = "घड़ी में 12 से 3 तक एक चौथाई (1/4) वृत्त बनता है। 360° का 1/4 ज्ञात करें।",
                expertHintEnglish = "From 12 to 3 is 1/4th of the full 360° circle.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "60° और 180° क्रमशः 2:00 और 6:00 बजे के कोण हैं।",
                fiftyFiftyProofEnglish = "60° and 180° correspond to 2:00 and 6:00 respectively.",
                diagramType = "clock_angle",
                diagramData = "3:00"
            ),
            // Junior Q4: Food Chain Ecosystem Logic
            createQuestion(
                qNumber = 4,
                category = "Food Chain Ecosystem Logic",
                questionHindi = "एक घास के मैदान में खाद्य श्रृंखला: 'घास -> टिड्डा -> मेंढक -> सांप -> चील'। यदि कीटनाशक के कारण सभी टिड्डे समाप्त हो जाएँ, तो तुरंत किस जीव की आबादी भोजन की कमी से घटेगी?",
                questionEnglish = "In a grassland food chain: 'Grass -> Grasshopper -> Frog -> Snake -> Eagle'. If all grasshoppers vanish, which organism's population will decrease immediately due to food shortage?",
                cluesHindi = listOf(
                    "खाद्य श्रृंखला में मेंढक सीधे टिड्डे का भक्षण करता है (प्राथमिक उपभोक्ता -> द्वितीयक उपभोक्ता)।",
                    "टिड्डे समाप्त होने पर मेंढक का मुख्य भोजन स्रोत तुरंत शून्य हो जाएगा।",
                    "सांप मेंढक को खाता है, अतः सांप पर प्रभाव बाद में पड़ेगा, किंतु मेंढक पर तुरंत प्रभाव पड़ेगा।"
                ),
                cluesEnglish = listOf(
                    "In the food chain, frogs feed directly on grasshoppers.",
                    "If grasshoppers disappear, the frog's immediate food supply drops to zero.",
                    "Snakes eat frogs, so their impact is secondary, whereas frogs suffer immediately."
                ),
                optionsHindi = listOf("मेंढक (Frog)", "घास (Grass)", "चील (Eagle)", "कोई परिवर्तन नहीं"),
                optionsEnglish = listOf("Frog", "Grass", "Eagle", "No change"),
                correctIndex = 0,
                deductionHindi = "श्रृंखला 'टिड्डा -> मेंढक' में मेंढक टिड्डे का प्रत्यक्ष शिकारी है। शिकार समाप्त होते ही मेंढक सीधे भुखमरी से घटेंगे जबकि घास बढ़ेगी।",
                deductionEnglish = "Frogs are the direct predators of grasshoppers. Vanishing grasshoppers directly starves the frog population first.",
                elimReasonsHindi = listOf("सही: मेंढक सीधे टिड्डों पर निर्भर हैं।", "गलत: टिड्डे न होने से घास बढ़ेगी, घटेगी नहीं।", "गलत: चील शीर्ष शिकारी है, तुरंत नहीं घटेगी।", "गलत: पारिस्थितिकी तंत्र में संतुलन बदलेगा।"),
                elimReasonsEnglish = listOf("Correct: Frogs feed directly on grasshoppers.", "False: Grass will actually increase.", "False: Eagles are top predators, not immediate.", "False: Food chain collapses."),
                expertHintHindi = "खाद्य श्रृंखला में टिड्डे के ठीक आगे आने वाले तीर (->) वाले जीव को पहचानें।",
                expertHintEnglish = "Identify the organism directly following the arrow after grasshopper.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "घास घटेगी नहीं बल्कि बढ़ेगी, और संतुलन में परिवर्तन अवश्य होगा।",
                fiftyFiftyProofEnglish = "Grass will increase, not decrease, and change is inevitable."
            ),
            // Junior Q5: Calendar Day Logic (Checkpoint Tier)
            createQuestion(
                qNumber = 5,
                category = "Calendar Cyclic Math",
                questionHindi = "यदि किसी सामान्य माह की 3 तारीख को 'मंगलवार' है, तो उसी माह की 24 तारीख को कौन सा दिन होगा?",
                questionEnglish = "If the 3rd day of a month is a Tuesday, which day of the week will the 24th day of the same month be?",
                cluesHindi = listOf(
                    "दिनों का चक्र हर 7 दिन बाद दोहराता है (+7 दिन = वही वार)।",
                    "3 तारीख (मंगलवार) + 7 = 10 (मंगलवार) + 7 = 17 (मंगलवार) + 7 = 24 तारीख।",
                    "अंतर: 24 - 3 = 21 दिन। 21 को 7 से भाग देने पर शेषफल = 0 बचता है।"
                ),
                cluesEnglish = listOf(
                    "Weekdays repeat every 7 days (+7 days = same day).",
                    "3rd (Tue) + 7 = 10th (Tue) + 7 = 17th (Tue) + 7 = 24th (Tue).",
                    "Total difference: 24 - 3 = 21 days. 21 mod 7 = 0 remainder."
                ),
                optionsHindi = listOf("मंगलवार (Tuesday)", "बुधवार (Wednesday)", "सोमवार (Monday)", "गुरुवार (Thursday)"),
                optionsEnglish = listOf("Tuesday", "Wednesday", "Monday", "Thursday"),
                correctIndex = 0,
                deductionHindi = "तारीखों का अंतर = 24 - 3 = 21 दिन। 21 = 7 × 3 (सटीक 3 सप्ताह)। शेषफल 0 होने के कारण 24 तारीख को भी 'मंगलवार' ही होगा।",
                deductionEnglish = "Date difference = 24 - 3 = 21 days = exactly 3 weeks (remainder 0). Hence, the 24th is strictly a Tuesday.",
                elimReasonsHindi = listOf("सही: 21 दिन = 3 पूरे सप्ताह, दिन समान रहेगा।", "गलत: शेष 1 दिन होने पर बुधवार होता।", "गलत: सोमवार एक दिन पीछे है।", "गलत: 2 दिन आगे है।"),
                elimReasonsEnglish = listOf("Correct: 21 mod 7 = 0.", "False: Wednesday requires +1 remainder.", "False: Monday is -1.", "False: Thursday is +2."),
                expertHintHindi = "3 में 7 जोड़ते जाएं: 3 + 7 = 10, 10 + 7 = 17, 17 + 7 = 24।",
                expertHintEnglish = "Add 7 repeatedly to 3: 3 -> 10 -> 17 -> 24.",
                fiftyFiftyDiscards = listOf(1, 3),
                fiftyFiftyProofHindi = "बुधवार और गुरुवार के लिए क्रमशः 1 और 2 विषम दिन होने चाहिए थे।",
                fiftyFiftyProofEnglish = "Wednesday and Thursday would require odd remainders 1 and 2."
            )
        )
    }

    private fun getFallbackGenericQuestion(qNumber: Int): QuestionItem {

        val meta = GeminiApiClient.getTierDetails(qNumber)
        return QuestionItem(
            id = "fallback_$qNumber",
            qNumber = qNumber,
            difficultyTitle = meta.difficultyTitle,
            timeLimitSeconds = meta.timeLimitSeconds,
            prizePoints = meta.prizePoints,
            prizeFormatted = meta.prizeFormatted,
            isCheckpoint = meta.isCheckpoint,
            checkpointTitle = meta.checkpointTitle,
            category = "Logical Syllogism",
            questionHindi = "यदि सभी A, B हैं; और कोई B, C नहीं है; तो A और C के बीच क्या निश्चित संबंध है?",
            questionEnglish = "If All A are B, and No B is C, what is the mathematically certain relationship between A and C?",
            cluesHindi = listOf("समुच्चय A पूरी तरह समुच्चय B के अंदर समाहित है (A ⊆ B)", "समुच्चय B और C में कोई उभयनिष्ठ तत्व नहीं है (B ∩ C = ∅)", "अतः A और C भी पूर्णतः असंयुक्त होंगे।"),
            cluesEnglish = listOf("Set A is entirely contained within Set B (A ⊆ B).", "Set B and Set C share zero elements (B ∩ C = ∅).", "Therefore, A and C are strictly disjoint."),
            optionsHindi = listOf("कोई A, C नहीं है", "कुछ A, C हैं", "सभी A, C हैं", "कोई निश्चित संबंध नहीं"),
            optionsEnglish = listOf("No A is C", "Some A are C", "All A are C", "Cannot be determined"),
            correctAnswerIndex = 0,
            deductionPathHindi = "चूँकि A पूरी तरह B में है, और B का कोई हिस्सा C नहीं हो सकता, अतः A का कोई भी तत्व C नहीं हो सकता।",
            deductionPathEnglish = "Since A is a subset of B, and B is disjoint from C, A must also be disjoint from C.",
            eliminationReasonsHindi = listOf("सही: असंयुक्त समुच्चय सिद्धांत।", "गलत: कोई भी साझा तत्व नहीं है।", "गलत: पूर्णतः असंभव।", "गलत: 100% निश्चित है।"),
            eliminationReasonsEnglish = listOf("Correct: Disjoint set theorem.", "False: No intersection exists.", "False: Strictly false.", "False: Mathematically certain."),
            expertAdviceHindi = "वेन आरेख में B और C को दो अलग गोले बनाएं और A को B के अंदर रखें।",
            expertAdviceEnglish = "Draw two separate non-touching circles for B and C, placing A entirely inside B.",
            fiftyFiftyDiscardIndices = listOf(1, 2),
            fiftyFiftyProofHindi = "विकल्प 2 और 3 दिए गए असंयुक्त नियम का उल्लंघन करते हैं।",
            fiftyFiftyProofEnglish = "Options 2 and 3 violate the disjoint set condition.",
            semanticFingerprint = "fallback_fingerprint_$qNumber"
        )
    }
}
