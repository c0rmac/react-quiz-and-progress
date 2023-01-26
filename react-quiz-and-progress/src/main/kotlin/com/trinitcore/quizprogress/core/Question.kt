package com.trinitcore.quizprogress.core

class Answer(val id: Int, val answer: String)

class Question(
    val id: Int,
    val question: String,
    val answers: List<Answer>,
    val dependentQuestionID: Int?,
    val dependentAnswerIds: Set<Int>
) {
}