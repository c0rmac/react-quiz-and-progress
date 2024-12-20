package com.trinitcore.quizprogress.demo

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.styles.PaletteMode
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createTheme
import com.ccfraser.muirwik.components.styles.mode
import com.ccfraser.muirwik.components.utils.Colors
import com.ccfraser.muirwik.components.utils.ControlColor
import com.trinitcore.quizprogress.MAX_VP_WIDTH
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

    object Style : StyleSheet("com-trinitcore-quizprogress-demo-DemoAnswerButton", isStatic = true) {

        val wrapper by css {
            maxWidth = 441.px
            width = 100.pct
            padding(4.px)
        }

        val inner by css {
            display = Display.flex
            minHeight = 52.px
            backgroundColor = Color("#EAE9E9")
            width = 100.pct

            children("*") {
                float = kotlinx.css.Float.left
                marginTop = LinearDimension.auto
                marginBottom = LinearDimension.auto
            }
        }

        val label by css {
            textAlign = TextAlign.left
            paddingTop = 6.px
            paddingBottom = 6.px
            paddingRight = 4.px
        }
    }

    override fun RBuilder.render() {
        child(AnswerButton::class) {
            attrs.label = props.answer.answer
            attrs.stateControl = props.ansStateControl
            // attrs.onClick = { stateControl -> props.onClick() }
            attrs.render = { isSelected, label ->
                buttonBase(onClick = { props.onClick() }) {
                    css(Style.wrapper)
                    // if (props.className != null) css { +props.className!! }
                    styledDiv {
                        css { +Style.inner }
                        console.log("isSelected", isSelected)
                        radio(color = ControlColor.primary, checked = isSelected) {

                        }
                        styledH4 {
                            css { +Style.label }
                            +label
                        }
                    }
                }
            }
        }
    }
}

interface DemoQuestionAndAnswerProps : Props {
    var question: Question
    var answerId: Int?
    var handleQuestionAnswered: (matrixAnswerID: Int) -> Unit
    var backButtonOnClick: () -> Unit
}

class DemoQuestionAndAnswer : RComponent<DemoQuestionAndAnswerProps, State>() {
    override fun RBuilder.render() {
        child(QuestionAndAnswer::class) {
            attrs.showForgettenQuesWarn = QuestionSetProgressController.Mode.STANDARD
            attrs.question = props.question
            attrs.answerID = props.answerId
            attrs.handleQuestionAnswered = props.handleQuestionAnswered
            attrs.backButtonOnClick = props.backButtonOnClick
            attrs.answerButtonRender = { answer, ansStateControl ->
                child(DemoAnswerButton::class) {
                    attrs.answer = answer
                    attrs.ansStateControl = ansStateControl
                    attrs.onClick = { props.handleQuestionAnswered(ansStateControl.answer.id) }
                }
            }
            attrs.backButtonRender = { backButtonOnClick ->

            }

        }
    }
}

val welcomeWithRouter = withRouter(Welcome::class)

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(
            props.name,
            QuestionSetProgressController(
                proposedQuestionSet = ProposedQuestionSet(
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

    fun questionOnChange(question: Question) {
        this.props.history.push("/" + question.id.toString())
    }

    fun questionSetOnFulfilled() {
        /*
        this.props.history.push("/1")
        this.state.controller.progress = 0.0
        this.state.controller.setQuestionWithID(1)
        this.state.controller.answerSet.clear()
         */
    }

    object Style : StyleSheet("com-trinitcore-quizprogress-demo-Welcome") {
        val progressIndicatorContainer by css {
            marginTop = 20.px
            marginBottom = 20.px
            paddingLeft = 28.px
            paddingRight = 28.px

        }

        val progressIndicator by css {
            maxWidth = MAX_VP_WIDTH
            put("margin-left", "auto !important")
            put("margin-right", "auto !important")
            put("height", "9px !important")
        }
    }

    private var currentRouterQuestionID: Int? = null

    private fun handleMatrixQuestionRouteOnChange(visible: Boolean, question: Question) {
        // console.log("handleMatrixQuestionRouteOnChange", visible, currentRouterQuestionID, question.id)
        if (visible && currentRouterQuestionID != question.id) {
            currentRouterQuestionID = question.id

            state.controller?.setQuestionWithID(question.id)
        }
    }

    private fun handleMatrixQuestionAnswered(answerId: Int) {
        // console.log("handleMatrixQuestionAnswered")
        setState {
            this.controller?.tryAnsQuesAndGoToNxtQues(answerId) { ques, answerId ->
            }
        }
    }

    private fun handleMatrixQuestionButtonOnClick() {
        // props.history.goBack()
    }

    override fun RBuilder.render() {
        mCssBaseline()
        val themeOptions: ThemeOptions = js("({palette: { mode: 'placeholder', primary: {main: 'placeholder'}}})")
        themeOptions.palette?.mode = PaletteMode.light
        themeOptions.palette?.primary?.main = Colors.Blue.shade500.toString()
        div {
            styledDiv {
                css { +Style.progressIndicatorContainer }
                linearProgress(
                    variant = LinearProgressVariant.determinate,
                    value = state.controller.progress * 100.0
                ) {
                    css(Style.progressIndicator)
                }
            }

                themeProvider(createTheme(themeOptions)) {
                    animatedSwitch {
                        state.controller.questions.forEach { question ->
                            route("/${question.id}") {
                                vizSensor {
                                    this.attrs.onChange =
                                        { visible: Boolean -> handleMatrixQuestionRouteOnChange(visible, question) }
                                    child(DemoQuestionAndAnswer::class) {
                                        attrs.answerId = state.controller.answerSet[question.id]
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
}
