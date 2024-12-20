package com.trinitcore.quizprogress.core

class QuestionSetProgressController(
    val proposedQuestionSet: ProposedQuestionSet,
    currentQuestionID: Int?,
    val questionOnChange: (question: Question) -> Unit,
    val questionSetOnFulfilled: () -> Unit
) {

    enum class Mode {
        STANDARD, FORGOTTEN_QUESTIONS
    }

    var mode = Mode.STANDARD

    var progress = 0.0
    val progressIncrsAppendedToQues = hashMapOf<Int, Double>()

    private val questionIdsToDependentQuesIds: MutableMap<Int, MutableSet<Int>>
    private val questionIdsToDirectDependentQuesIds: MutableMap<Int, MutableSet<Int>>
    private val questionIdsByPath: MutableMap<Int, String>
    private val questionsByIncr: MutableMap<Int, Int>
    private val questionsByID: MutableMap<Int, Question>
    val questions: Array<Question>
    get() = this.proposedQuestionSet.questions

    val answerSet: MutableMap<Int, Int?>
    get() = this.proposedQuestionSet.answerSet

    lateinit var depQuestionsByQaAPath: HashMap<String, MutableSet<Question>>

    private var curQusIncr: Int = 0
    val currentQuestion: Question
    get() = this.questions[curQusIncr]

    private var isIteratingForgQues = false

    /**
     * nextIncrementalQuestion
     */
    val nextIncrQues: Question?
    get() = this.questions[++curQusIncr]

    // Where the key is defined as a Dependent Question & Answer Path
    val progressIncrementRegistry: HashMap<String, Double>

    val showFlagsForDepQuestions = mutableListOf<Int>()

    fun buildPath(questionsByID: MutableMap<Int, Question>, question: Question): String {
        var path = mutableListOf(question.id.toString())
        val dependentQuestionID = question.dependentQuestionID
        if (dependentQuestionID != null) {
            val parentQuestion = questionsByID[dependentQuestionID] ?: throw IllegalStateException("No dependent question with id ${dependentQuestionID}")
            path.add(0, buildPath(questionsByID, parentQuestion))
        }
        return path.joinToString("-")
    }

    init {
        // TUS : Sort Questions
        val questionsByID = mutableMapOf<Int, Question>()
        val questionsByIncr = mutableMapOf<Int, Int>()
        val questionIdsByPath = mutableMapOf<Int, String>()
        val questionIdsToDependentQuesIds = mutableMapOf<Int, MutableSet<Int>>()
        val questionIdsToDirectDependentQuesIds = mutableMapOf<Int, MutableSet<Int>>()
        this.questions.forEachIndexed { i, it ->
            questionsByID[it.id] = it
            questionsByIncr[it.id] = i
        }

        fun assignQues(question: Question) {
            if (questionIdsToDependentQuesIds[question.dependentQuestionID] == null) {
                questionIdsToDependentQuesIds[question.dependentQuestionID!!] = mutableSetOf()
            }
            questionIdsToDependentQuesIds[question.dependentQuestionID]!!.add(question.id)
            /*
            question.dependentQuestionID?.let {
                val parentQuestion = questionsByID[question.dependentQuestionID] ?: throw IllegalStateException("No parent question with ID = ${question.dependentQuestionID}")
                if (parentQuestion != question && parentQuestion.dependentQuestionID != null)
                    assignQues(parentQuestion)
            }
             */
        }

        this.questions.forEach { it ->
            questionIdsByPath[it.id] = buildPath(questionsByID, it)

            if (it.dependentQuestionID != null) {
                assignQues(it)
                /*
                if (questionIdsToDirectDependentQuesIds[it.dependentQuestionID] == null) {
                    questionIdsToDirectDependentQuesIds[it.dependentQuestionID] = mutableSetOf()
                }
                questionIdsToDirectDependentQuesIds[it.dependentQuestionID]!!.add(it.id)
                 */
            }
        }
        this.questionIdsByPath = questionIdsByPath
        this.questionIdsToDependentQuesIds = questionIdsToDependentQuesIds
        this.questionIdsToDirectDependentQuesIds = questionIdsToDirectDependentQuesIds

        // Where the key is defined as a Dependent Question & Answer Path
        val depQuestionsByQaAPath = hashMapOf<String, MutableSet<Question>>()
        val independentQuestions = mutableListOf<Question>()

        for (question in this.questions) {
            val dependentQuestionID = question.dependentQuestionID
            if (dependentQuestionID != null) {
                val dependentQuestion = questionsByID[dependentQuestionID]// ?: throw DependentQuestionNotFoundException(dependentQuestionID)

                if (dependentQuestion != null) {
                    fun assignDependentQuestions(ids: Set<Int>, answers: List<Answer>) {
                        for (answer in answers) {
                            val answerFieldID = answer.id
                            val dependentQuestionsWhenAnswer = depQuestionsByQaAPath["${dependentQuestion.path}-${answerFieldID}"]

                            if (ids.contains(answer.id)) {
                                if (dependentQuestionsWhenAnswer != null) {
                                    dependentQuestionsWhenAnswer.add(question)
                                } else {
                                    depQuestionsByQaAPath["${dependentQuestion.path}-${answerFieldID}"] = mutableSetOf(question)
                                }
                            } else {
                                if (dependentQuestionsWhenAnswer == null)
                                    depQuestionsByQaAPath["${dependentQuestion.path}-${answerFieldID}"] = mutableSetOf()
                            }
                        }
                    }

                    for (dependentAnswerId in question.dependentAnswerIds) {
                        assignDependentQuestions(question.dependentAnswerIds, question.answers)
                    }
                } else {
                    console.log("Orphaned Question @ID = $dependentQuestionID")
                }
            } else {
                independentQuestions.add(question)
            }
        }
        // DEIREADH : Sort Questions

        // TUS : Build increment registry
        val progressIncrementRegistry: HashMap<String, Double> = hashMapOf()

        /**
         * @param parentAnsFieldString Leave unspecified to define independent entry points to accessing the questions
         */
        fun buildIncrReg(childrnQuestions: List<Question>, extParentIncr: Double, parentAnsFieldString: String = ":") {
            for (childQues in childrnQuestions) {
                fun assignIncr(answerFieldID: Int) {
                    val qAAPath = "${childQues.path}-${answerFieldID}"
                    val extQaAPath = "${childQues.path}${parentAnsFieldString}${answerFieldID}"

                    val a = depQuestionsByQaAPath[qAAPath] ?: listOf<Question>()
                    val aIncr = calculateIncrement(a.size.toDouble(), extParentIncr)
                    progressIncrementRegistry[extQaAPath] = aIncr
                    buildIncrReg(a.toList(), aIncr, "$parentAnsFieldString$answerFieldID-")
                }

                for (answer in childQues.answers) {
                    assignIncr(answer.id)
                }
            }
        }

        buildIncrReg(independentQuestions, 1.0 / independentQuestions.size.toDouble())
        // DEIREADH : Build increment registry

        this.progressIncrementRegistry = progressIncrementRegistry

        this.depQuestionsByQaAPath = depQuestionsByQaAPath
        this.questionsByID = questionsByID
        this.questionsByIncr = questionsByIncr

        if (currentQuestionID != null) {
            // Session restoration
            setQuestionWithID(currentQuestionID)
            // this.progress = this.getCurrentProgress(currentQuestionID)
        } else {
            val ID = this.questions.first().id
            setQuestionWithID(ID)
            this.questionOnChange(this.currentQuestion)
        }
    }

    private fun calculateIncrement(m: Double, extendedParentIncrement: Double) = extendedParentIncrement * (1.0 / (m + 1.0))

    private fun buildProgressIncrementRegistry() {

    }

    fun setQuestionWithID(ID: Int) {
        this.questionsByIncr[ID]?.let {
            this.curQusIncr = it
        }
    }

    private fun getForgottenQuesIncr(): Int? {
        for ((i, ques) in this.questions.withIndex()) {
            if (this.answerSet[ques.id] == null
                && (this.showFlagsForDepQuestions.contains(ques.id) || ques.dependentQuestionID == null)
            ) return i
        }
        return null
    }

    fun getDepQuestionsFieldIDs(curQues: Question): String {
        val curQuesPathSegments = curQues.path.split("-")
        val depQuestionsFieldIDs = mutableListOf<Int>()
        for ((i, curQuesPathSeg) in curQuesPathSegments.withIndex()) {
            if (i == curQuesPathSegments.size - 1) break

            val depQuesAnsFieldID = this.answerSet[curQuesPathSeg.toInt()]
            if (depQuesAnsFieldID != null) {
                depQuestionsFieldIDs.add(depQuesAnsFieldID)
            } else {

            }
        }
        return depQuestionsFieldIDs.joinToString("-")
    }

    fun tryAnsQuesAndGoToNxtQues(answerId: Int, answerOnSuccess: (question: Question, answerFieldID: Int) -> Unit) {
        val curQues = this.currentQuestion // @ curQuesI

        /**
         * Ar fíor dó:
         * is é an chéad rud eile ab chóir a dhíona
         * this.questionChange a chur i bhfeidhm nú ar
         * theip don úsáideoir freagra a shonrú i gcás chuile ceist,
         * taispeán sraith ceisteannaí nach raibh freagraí.
         *
         * Ar bréagach dó:
         * is féidir this.questionSetOnFulfilled a chur i bhfeidhm
         * */
        fun goToNextQuestion(dependentQuesSkip: Int = 1): Boolean {
            val nextIncrQues = this.nextIncrQues // @ curQuesI + 1
            val showFlagsForDepQuestions = this.showFlagsForDepQuestions

            if (nextIncrQues != null && mode == Mode.STANDARD) {
                val depQuesID = nextIncrQues.dependentQuestionID
                return if (depQuesID != null) {
                    if (this.showFlagsForDepQuestions.contains(nextIncrQues.id)) {
                        answerOnSuccess(curQues, answerId)
                        this.questionOnChange(nextIncrQues)
                        true
                    } else {
                        goToNextQuestion(dependentQuesSkip + 1)
                    }
                } else {
                    answerOnSuccess(curQues, answerId)
                    this.questionOnChange(nextIncrQues)
                    true
                }
            } else {
                val forgottenQuesIncr = this.getForgottenQuesIncr()
                if (forgottenQuesIncr != null) {
                    this.mode = Mode.FORGOTTEN_QUESTIONS
                    this.curQusIncr = forgottenQuesIncr
                    answerOnSuccess(curQues, answerId)
                    this.questionOnChange(this.currentQuestion)
                    return true
                } else {
                    this.mode = Mode.STANDARD
                    answerOnSuccess(curQues, answerId)
                    // TUS : CLEAN_UP - Since the next question does not exist, role back to previous pos incr
                    this.curQusIncr -= dependentQuesSkip
                    console.log(this.curQusIncr)
                    // DEIREADH : CLEAN_UP
                    this.questionSetOnFulfilled()
                    return false
                }
            }
        }

        // TUS : Serialize curQues.path to IDs
        val curQuesPathSegments = curQues.path.split("-")
        val depQuestionsFieldIDs = mutableListOf<Int>()
        for ((i, curQuesPathSeg) in curQuesPathSegments.withIndex()) {
            if (i == curQuesPathSegments.size - 1) break

            val depQuesAnsFieldID = this.answerSet[curQuesPathSeg.toInt()]
            if (depQuesAnsFieldID != null) {
                depQuestionsFieldIDs.add(depQuesAnsFieldID)
            } else {
                /* ABORT - The user is not supposed to be on this question. Bring them
                * to the next question & let the forgotten question handler re-ask the question. */
                goToNextQuestion()
                return
            }
        }
        // DEIREAIDH : Serialize curQues.path to IDs and construct an Extended QaA path

        val quesByExtQaAPath = curQues.path + ":" + depQuestionsFieldIDs.joinToString("-") +
                (if (depQuestionsFieldIDs.isEmpty()) "" else "-") + answerId

        console.log("progressIncrementRegistry keys", this.progressIncrementRegistry.keys.toTypedArray())
        console.log("progressIncrementRegistry values", this.progressIncrementRegistry.values.toTypedArray())

        if (this.answerSet[currentQuestion.id] == null) {
            // Trigger increment
            val incr = this.progressIncrementRegistry[quesByExtQaAPath] ?: throw Exception("The progress increment ${quesByExtQaAPath} was not found.")
            this.progress += incr

            this.progressIncrsAppendedToQues[curQues.id] = incr
        } else {
            val incr = this.progressIncrementRegistry[quesByExtQaAPath] ?: throw Exception("The progress increment ${quesByExtQaAPath} was not found.")
            val assignedIncr = this.progressIncrsAppendedToQues[curQues.id] ?: throw Exception("Expected increment here.")

            var childQuestionsIncr = 0.0
            val childQuestions = this.questionIdsToDependentQuesIds[curQues.id] ?: mutableSetOf()

            fun resetQuestionProgressAtId(id: Int) {
                childQuestionsIncr += this.progressIncrsAppendedToQues[id] ?: 0.0
                this.progressIncrsAppendedToQues[id] = 0.0
                this.answerSet[id] = null
                this.showFlagsForDepQuestions.remove(id)
            }

            fun resetSubQuestionProgress(question: Question) {
                val childQuestions = this.questionIdsToDependentQuesIds[question.id] ?: mutableSetOf()
                childQuestions.forEach {
                    resetQuestionProgressAtId(it)

                    resetSubQuestionProgress(this.questionsByID[it]!!)
                }
            }

            childQuestions.forEach {
                val ques = this.questionsByID[it]!!
                if (ques.dependentAnswerIds.contains(answerId)) {
                } else {
                    resetQuestionProgressAtId(it)
                    resetSubQuestionProgress(ques)
                }
            }

            val subtraction = assignedIncr + childQuestionsIncr
            val newIncr = (incr - subtraction)
            this.progress += newIncr
            this.progressIncrsAppendedToQues[curQues.id] = incr
        }

        val quesByQaAPath = curQues.path + "-" + answerId

        this.answerSet[currentQuestion.id] = answerId

        // TODO: Implement unAnsweredQuestion
        val depQuestions = this.depQuestionsByQaAPath[quesByQaAPath]

        // TUS : Faigh réidh le haon flags atá i láthair cheanna féin
        fun eraseFlagsForCurQues(depQuestionsWhenAnsAny: MutableSet<Question>?) {
            depQuestionsWhenAnsAny?.forEach { this.showFlagsForDepQuestions.remove(it.id) }
        }

        for (answer in curQues.answers) {
            val depQuestionsWhenAnsA = this.depQuestionsByQaAPath[curQues.path + "-" + answer.id]
            eraseFlagsForCurQues(depQuestionsWhenAnsA)
        }
        // DEIREADH : Faigh réidh le haon flags atá i láthair cheanna féin

        // Socraigh flag de réir an fhreagra sonraithe
        depQuestions?.forEach { this.showFlagsForDepQuestions.add(it.id) }


        goToNextQuestion()
    }

    fun getRootQuestion(question: Question): Question {
        return question.dependentQuestionID?.let { questionsByID[it] ?: throw IllegalStateException("No parent question with id = ${it}.") } ?: question
        // return question.dependentQuestionID?.let { getRootQuestion(questionsByID[it] ?: throw IllegalStateException("No parent question with id = ${it}.")) } ?: question
    }

    val Question.path get() = this@QuestionSetProgressController.questionIdsByPath[this.id] ?: throw IllegalStateException("Question with id = ${this.id} does not have an associated path.")

    val Question.rootQuestion get() = getRootQuestion(this)
}