# React Quiz and Progress
Build a Questionnaire with a progress bar for React Js app.

## Motivation
In 2020, I was working on an application called [House of Costs](https://c0rmac.github.io/portfolio/house-of-costs/)
in which a user personalised questionnaire was asked to be fulfilled by
the end user. The questionnaire had various levels of complexity, as answering certain answers
to certain questions would trigger followup questions. This would give the impression
to the end-user that the Questionnaire could be a long slog, especially by
the fact that there was no indication as to how much more of the Questionnaire the
user would have to complete.

In turn, this motivated the idea of having a linear progress bar which would
indicate intuitively to the user how much of the questionnaire the user has completed.

As the user answers more questions, the more the progress bar progresses. For
questions that are "independent" (ie: do not depend on previous answers to previous questions),
a large progress increment is rewarded to the progress bar. For "dependent" questions,
a smaller increment is rewarded to the progress bar. For questions that are "dependent" questions
that are dependent on other questions (and that are dependent on other questions and so on),
an even smaller increment is rewarded to the progress bar.

## Technical Details
The plugin was developed in Kotlin React Js (since I use Multiplatform Kotlin by nature).
I will publish more of the technical details of my plugin on my blog soon.

## Overview

### QuestionSetProgressController
This class provides the state of the Questionnaire, such as progress made,
determining the next question and when the Questionnaire is complete. It
should be placed in the state class.

```kotlin
QuestionSetProgressController(
    proposedQuestionSet = ProposedQuestionSet(
        // Place all the questions you want to ask here. See the next section
        // for more specific details
        questions = arrayOf(
            Question(id = 1, question = "Question 1",
                dependentAnswerIds = emptySet(), dependentQuestionID = null,
                answers = listOf(
                    Answer(id = 1, answer = "Answer 1"),
                    Answer(id = 2, answer = "Answer 2"),
                    Answer(id = 3, answer = "Answer 3"),
                    Answer(id = 4, answer = "Answer 4"),
                    Answer(id = 5, answer = "Answer 5"),
                )
            )
        ), 
        // A map from Qustion.id to Answer.id. That is, if the end user
        // chooses the answer with id=3 for question with id=1, the map will be
        // assigned with 1->3
        answerSet = mutableMapOf(), 
        // Define a prefilled answer set, if needed
        defaultAnswerSet = null
    ),
    // Specifiy the current question the end user is on
    currentQuestionID = 1,
    // Some callbacks
    questionOnChange = ::questionOnChange,
    questionSetOnFulfilled = ::questionSetOnFulfilled
)
```

### Question
We can define questions and parameterize them as independent questions that will always
be asked no matter what, and dependent questions which depend on the answer given
to previous questions. Here is an example of a question set
```kotlin
arrayOf(
    // An example of an independent question
    Question(
        id = 1, question = "Question 1",
        dependentAnswerIds = emptySet(), 
        // The independence is characterised by the fact that dependentQuestionID = null
        dependentQuestionID = null,
        answers = listOf(
            Answer(id = 1, answer = "Answer 1"),
            Answer(id = 2, answer = "Answer 2"),
            Answer(id = 3, answer = "Answer 3"),
            Answer(id = 4, answer = "Answer 4"),
            Answer(id = 5, answer = "Answer 5"),
        )
    ),
    // An example of a dependent question
    Question(
        id = 2, question = "Question 1.1",
        // As opposed to the previous question, we have assigned dependentQuestionID = 1
        // and dependentAnswerIds is assigned a set.
        // That is, this question, question with id = 2, will be asked if and only if
        // answer with ids 2 or 3 is selected in question with id = 1.
        dependentQuestionID = 1,
        dependentAnswerIds = setOf(2, 3),
        answers = listOf(
            Answer(id = 1, answer = "Answer 1"),
            Answer(id = 2, answer = "Answer 2"),
            Answer(id = 3, answer = "Answer 3")
        )
    )
)
```

## Setup
There are various ways that you may choose to setup the questionnaire. I preferred
to setup the questionnaire using react router to present each question by its own "page".

I'd also like to warn you that I have not switched to using more modern
React Js coding conventions, so I apologise for my dinosaur code. 

As usual, install with 

### Javascript ES6 (Regular React Js project)
I will publish instructions on this in the future as I am no longer familiar with ES6 anymore.
But it should be similar. Help here would be very welcome!

As usual, you can retrieve the plugin by running the following

``` 
npm i react-quiz-and-progress
```

### Kotlin Js
In your build.gradle.kts file, add the following
```kotlin
implementation(npm("react-quiz-and-progress","1.0.0"))
```

Client.kt file
```kotlin
import kotlinx.browser.window
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
```

(Optional) AnimatedSwitch.kt
```kotlin
import com.trinitcore.quizprogress.demo.wrapper.glamorCss
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.AnimatedSwitchProps
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.animatedSwitch
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.spring
import kotlinext.js.js
import react.RBuilder
import react.RHandler

private val hOCSwitchRule = glamorCss(js {
    this.position = "relative"
    this["& > div"] = js {
        this.position = "absolute"
    }
})

private fun glide(value: Double): dynamic {
    return spring(value, js("{ stiffness: 174, damping: 24 }"))
}

fun RBuilder.animatedSwitch(handler: RHandler<AnimatedSwitchProps>)
        = this@animatedSwitch.animatedSwitch(className = "view-holder-container") {
    attrs {
        atEnter = js { }
        atEnter.asDynamic().offset = 100

        atLeave = js { }
        atLeave.asDynamic().offset = glide(-100.0)

        atActive = js { }
        atActive.asDynamic().offset = glide(0.0)

        switchRule = hOCSwitchRule
        mapStyles = { styles: dynamic ->
            js {
                if (styles.offset == 0) {
                    transform = "unset"
                } else transform = "translateX(" + styles.offset + "%)"
            }
        }
    }

    handler.invoke(this)
}
```

Welcome.kt file
```kotlin
import com.trinitcore.quizprogress.core.Answer
import com.trinitcore.quizprogress.core.ProposedQuestionSet
import com.trinitcore.quizprogress.core.Question
import com.trinitcore.quizprogress.core.QuestionSetProgressController
import com.trinitcore.quizprogress.demo.wrapper.buttonBase
import com.trinitcore.quizprogress.demo.wrapper.vizSensor
import com.trinitcore.quizprogress.react.comp.AnswerButton
import com.trinitcore.quizprogress.react.viewregion.QuestionAndAnswer
import kotlinx.css.*
import react.*
import react.dom.div
import react.router.dom.*
import styled.StyleSheet
import styled.css
import styled.styledDiv
import styled.styledH4

external interface WelcomeProps : Props, RouteComponentProps {
    var name: String
}

data class WelcomeState(
    val name: String,
    val controller: QuestionSetProgressController
) : State

interface DemoAnswerButtonProps : Props {
    var answer: Answer
    var ansStateControl: AnswerButton.StateControl
    var onClick: () -> Unit
}

class DemoAnswerButton : RComponent<DemoAnswerButtonProps, State>() {

    object Style : StyleSheet("yourappname-DemoAnswerButton", isStatic = true) {

    }

    override fun RBuilder.render() {
        child(AnswerButton::class) {
            attrs.label = props.answer.answer
            attrs.stateControl = props.ansStateControl
            attrs.render = { isSelected, label ->
                // Add your button render code here for your customised answer button.
            }
        }
    }
}

interface DemoQuestionAndAnswerProps : Props {
    var question: Question
    var handleQuestionAnswered: (matrixAnswerID: Int) -> Unit
    var backButtonOnClick: () -> Unit
}

class DemoQuestionAndAnswer : RComponent<DemoQuestionAndAnswerProps, State>() {
    override fun RBuilder.render() {
        child(QuestionAndAnswer::class) {
            attrs.showForgettenQuesWarn = QuestionSetProgressController.Mode.STANDARD
            attrs.question = props.question
            attrs.answerID = null
            attrs.handleQuestionAnswered = props.handleQuestionAnswered
            attrs.backButtonOnClick = props.backButtonOnClick
            attrs.answerButtonRender = { answer, ansStateControl ->
                // Specify the answer button to the question here.
                child(DemoAnswerButton::class) {
                    attrs.answer = answer
                    attrs.ansStateControl = ansStateControl
                    attrs.onClick = { props.handleQuestionAnswered(ansStateControl.answer.id) }
                }
            }
            attrs.backButtonRender = { backButtonOnClick ->
                // Specify a back button here.
            }

        }
    }
}

// Inject history props into class
val welcomeWithRouter = withRouter(Welcome::class)

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        // Initialise a react state with the Questionnaire Set Progress Controller.
        // As the user progresses through the questions, the Progress Controller updates
        // its values and should be reflected on the user interface.
        state = WelcomeState(
            props.name,
            QuestionSetProgressController(
                proposedQuestionSet = ProposedQuestionSet(
                    // Sample question set
                    questions = arrayOf(
                        Question(
                            id = 1, question = "Question 1",
                            dependentAnswerIds = emptySet(), dependentQuestionID = null,
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2"),
                                Answer(id = 3, answer = "Answer 3"),
                                Answer(id = 4, answer = "Answer 4"),
                                Answer(id = 5, answer = "Answer 5"),
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
                            id = 5, question = "Question 3A.1",
                            dependentAnswerIds = setOf(1), dependentQuestionID = 4,
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2"),
                                Answer(id = 3, answer = "Answer 3")
                            )
                        ),
                        Question(
                            id = 6, question = "Question 3B.1",
                            dependentAnswerIds = setOf(2), dependentQuestionID = 4,
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 7, question = "Question 3B.2",
                            dependentAnswerIds = setOf(2), dependentQuestionID = 4,
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 8, question = "Question 3BA.1",
                            dependentQuestionID = 7, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 9, question = "Question 3BAA.1",
                            dependentQuestionID = 8, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 10, question = "Question 3BAAA.1",
                            dependentQuestionID = 9, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 11, question = "Question 3BAAA.2",
                            dependentQuestionID = 9, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 12, question = "Question 3BAAA.3",
                            dependentQuestionID = 9, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        ),
                        Question(
                            id = 13, question = "Question 3BAAA.4",
                            dependentQuestionID = 9, dependentAnswerIds = setOf(2),
                            answers = listOf(
                                Answer(id = 1, answer = "Answer 1"),
                                Answer(id = 2, answer = "Answer 2")
                            )
                        )
                    ), answerSet = mutableMapOf(), defaultAnswerSet = null
                ),
                currentQuestionID = 1,
                questionOnChange = ::questionOnChange,
                questionSetOnFulfilled = ::questionSetOnFulfilled
            )
        )
    }

    // Specify your callback for when the question controller asks to go 
    // to the next question.
    fun questionOnChange(question: Question) {
        // Navigate to the next page with the next questtion
        this.props.history.push("/" + question.id.toString())
    }

    // Specify your callback for when the questionnaire has been completed.
    fun questionSetOnFulfilled() {
        // Reset the questionnaire to the start
        this.props.history.push("/1")
        this.state.controller.progress = 0.0
        this.state.controller.setQuestionWithID(1)
        this.state.controller.answerSet.clear()
    }

    object Style : StyleSheet("com-trinitcore-quizprogress-demo-Welcome") {

    }

    private var currentRouterQuestionID: Int? = null
    
    private fun handleMatrixQuestionRouteOnChange(visible: Boolean, question: Question) {
        if (visible && currentRouterQuestionID != question.id) {
            currentRouterQuestionID = question.id

            state.controller?.setQuestionWithID(question.id)
        }
    }

    // Handle when an answer is clicked.
    private fun handleMatrixQuestionAnswered(answerId: Int) {
        setState {
            this.controller?.tryAnsQuesAndGoToNxtQues(answerId) { ques, answerId ->

            }
        }
    }

    override fun RBuilder.render() {
        div {
            styledDiv {
                // Specify some linear progress bar here.
                linearProgress(
                    value = state.controller.progress * 100.0 // controller.progress is a value between 0 to 1.
                )
            }
            
            animatedSwitch { // Optional animation between questions. Remove animatedSwitch if you don't want any animation.
                state.controller.questions.forEach { question ->
                    route("/${question.id}") {
                        vizSensor {
                            this.attrs.onChange = { visible: Boolean -> handleMatrixQuestionRouteOnChange(visible, question) }
                            child(DemoQuestionAndAnswer::class) {
                                attrs.question = question
                                attrs.handleQuestionAnswered = ::handleMatrixQuestionAnswered
                            }
                        }
                    }
                }
            }
        }
    }
}
```