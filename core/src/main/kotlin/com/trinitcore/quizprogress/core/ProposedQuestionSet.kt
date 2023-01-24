package com.trinitcore.quizprogress.core

data class ProposedQuestionSet(
    val questions: Array<Question>,
    val answerSet: MutableMap<Int, Int?>,
    val defaultAnswerSet: Map<Int, Int?>?,
) {

}