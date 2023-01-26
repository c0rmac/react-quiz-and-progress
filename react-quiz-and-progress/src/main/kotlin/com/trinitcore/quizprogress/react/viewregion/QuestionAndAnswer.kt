package com.trinitcore.quizprogress.react.viewregion

import com.trinitcore.quizprogress.MAX_VP_WIDTH
import com.trinitcore.quizprogress.core.Answer
import com.trinitcore.quizprogress.core.Question
import com.trinitcore.quizprogress.core.QuestionSetProgressController
import com.trinitcore.quizprogress.react.comp.AnswerButton
import kotlinx.css.*
import react.*
import react.dom.h6
// import react.router.dom.withRouter
import styled.StyleSheet
import styled.css
import styled.styledDiv
import styled.styledH3

// val questionAndAnswerWithRouter = withRouter(QuestionAndAnswer::class)

interface QuestionAndAnswerProps : Props {
    var showForgettenQuesWarn: QuestionSetProgressController.Mode?
    var question: Question
    var answerID: Int?

    var handleQuestionAnswered: (matrixAnswerID: Int) -> Unit
    var backButtonOnClick: () -> Unit

    var answerButtonRender: RBuilder.(answer: Answer, ansStateControl: AnswerButton.StateControl) -> Unit
    var backButtonRender: RBuilder.(backButtonOnClick: () -> Unit) -> Unit
}

class QuestionAndAnswerState(
    var ansStateControl: List<AnswerButton.StateControl> = emptyList()
) : State

class QuestionAndAnswer : RComponent<QuestionAndAnswerProps, QuestionAndAnswerState>() {

    init {
        state = QuestionAndAnswerState()
    }

    object Style : StyleSheet("com-trinitcore-quizprogress-react-viewregion-QuestionAndAnswer", isStatic = true) {

        val qAndAGroup by css {
            marginLeft = LinearDimension.auto
            marginRight = LinearDimension.auto
            paddingLeft = 4.pct
            paddingRight = 4.pct
            maxWidth = MAX_VP_WIDTH
        }

        val questionTitle by css {
            marginBottom = 20.px
        }

        val answerGroupWrapper by css {
            overflowY = Overflow.auto
        }

        val answerGroupInner by css {
            display = Display.grid
            marginLeft = LinearDimension.auto
            marginRight = LinearDimension.auto
        }

        val answerButton by css {
            put("margin-bottom", "6px !important")
            put("margin-left", "auto !important")
            put("margin-right", "auto !important")
        }

    }

    override fun componentWillMount() {
        //console.log("componentWillMount")
        state.ansStateControl = props.question.answers.map {
            AnswerButton.StateControl(isSelected = false, answer = it)
        }
        if (props.answerID != null) {
            state.ansStateControl.forEach {
                it.isSelected = (props.answerID == it.answer.id)
            }
        }
    }

    val didAnswerQuestion: Boolean
    get() = this.state.ansStateControl.any { it.isSelected }

    private fun RBuilder.answerButtonOnClick(selectedButtonStateControl: AnswerButton.StateControl, matrixAnswerFieldID: Int) {
        setState {
            this.ansStateControl.forEach {
                if (selectedButtonStateControl != it) it.isSelected = false
            }
        }

        props.handleQuestionAnswered(matrixAnswerFieldID)
    }

    // TUS : EVENT HANDLERS

    // DEIREADH : EVENT HANDLERS

    override fun RBuilder.render() {
        //console.log("render")
        styledDiv {
            css { +Style.qAndAGroup }
            if (props.showForgettenQuesWarn == QuestionSetProgressController.Mode.FORGOTTEN_QUESTIONS)
                h6 {
                    +"Some previous questions were not answered. You are being asked them now."
                }
            styledH3 {
                css { +Style.questionTitle }
                +props.question.question
            }

            styledDiv {
                css { +Style.answerGroupWrapper }
                styledDiv {
                    css { +Style.answerGroupInner }
                    for ((i, answer) in props.question.answers.withIndex()) {
                        props.answerButtonRender(this, answer, state.ansStateControl[i])
                        /*
                        answerButton(label = answer.answer, matrixAnswerFieldID = 1, stateControl = state.ansStateControl[i], onClick = { answerButtonOnClick(it, 1) }) { css(
                            Style.answerButton
                        ) }
                         */
                    }
                }
            }
            /*
            mIconButton(color = MColor.primary, onClick = { props.backButtonOnClick() }) {
                css {
                    put("margin-top", "8px !important")
                }
                // css { put("margin-left", "4px !important"); put("margin-top", "4px !important") }
                mArrowBackIosIcon(addAsChild = true) { css { paddingLeft = 8.px } }
            }
             */
            props.backButtonRender(this, props.backButtonOnClick)
        }
    }

}