package com.trinitcore.quizprogress.demo

import com.trinitcore.quizprogress.core.Answer
import com.trinitcore.quizprogress.core.ProposedQuestionSet
import com.trinitcore.quizprogress.core.Question
import com.trinitcore.quizprogress.core.QuestionSetProgressController
import kotlinx.browser.window
import react.dom.client.createRoot
import react.dom.render
import react.router.dom.hashRouter
import react.router.dom.route
import web.dom.document

fun main() {
    window.onload = {
        val container = document.getElementById("root") ?: error("Couldn't find root container!")
        console.log("Container", container)
        render(container) {
            hashRouter {
                route("/") {
                    welcomeWithRouter.invoke {
                        attrs.name = "Kotlin/JS"
                    }
                }
            }
        }
    }
}

fun test() {
    val controller = QuestionSetProgressController(
        proposedQuestionSet = ProposedQuestionSet(
            questions = arrayOf(
                Question(
                    id = 1, question = "Question 1",
                    dependentAnswerIds = emptySet(), dependentQuestionID = null,
                    answers = listOf(
                        Answer(id = 1, answer = "Answer 1"),
                        Answer(id = 2, answer = "Answer 2"),
                        Answer(id = 3, answer = "Answer 3")
                    )
                ),
                Question(
                    id = 2, question = "Question 1.1",
                    dependentAnswerIds = setOf(2, 3), dependentQuestionID = 1,
                    answers = listOf(
                        Answer(id = 1, answer = "Answer 1"),
                        Answer(id = 2, answer = "Answer 2"),
                        Answer(id = 3, answer = "Answer 3")
                    )
                ),
                Question(
                    id = 3, question = "Question 2",
                    dependentAnswerIds = emptySet(), dependentQuestionID = null,
                    answers = listOf(
                        Answer(id = 1, answer = "Answer 1"),
                        Answer(id = 2, answer = "Answer 2"),
                        Answer(id = 3, answer = "Answer 3")
                    )
                ),
                Question(
                    id = 4, question = "Question 3",
                    dependentAnswerIds = emptySet(), dependentQuestionID = null,
                    answers = listOf(
                        Answer(id = 1, answer = "Answer 1"),
                        Answer(id = 2, answer = "Answer 2"),
                        Answer(id = 3, answer = "Answer 3")
                    )
                ),
                Question(
                    id = 5, question = "Question 3.1",
                    dependentAnswerIds = setOf(1), dependentQuestionID = 4,
                    answers = listOf(
                        Answer(id = 1, answer = "Answer 1"),
                        Answer(id = 2, answer = "Answer 2"),
                        Answer(id = 3, answer = "Answer 3")
                    )
                )
            ), answerSet = mutableMapOf(), defaultAnswerSet = null
        ),
        currentQuestionID = 1,
        questionOnChange = { question ->

        },
        questionSetOnFulfilled = {
            print("Question Set Fulfilled")
        }
    )

    // 1
    controller.tryAnsQuesAndGoToNxtQues(answerFieldID = 2) { question, answerFieldID ->
        println("${question.question}, $answerFieldID")
    }
    // 1.1
    controller.tryAnsQuesAndGoToNxtQues(answerFieldID = 1) { question, answerFieldID ->
        println("${question.question}, $answerFieldID")
    }
    // 2
    controller.tryAnsQuesAndGoToNxtQues(answerFieldID = 1) { question, answerFieldID ->
        println("${question.question}, $answerFieldID")
    }
    // 3
    controller.tryAnsQuesAndGoToNxtQues(answerFieldID = 1) { question, answerFieldID ->
        println("${question.question}, $answerFieldID")
    }
    // 3.1
    controller.tryAnsQuesAndGoToNxtQues(answerFieldID = 1) { question, answerFieldID ->
        println("${question.question}, $answerFieldID")
    }
}