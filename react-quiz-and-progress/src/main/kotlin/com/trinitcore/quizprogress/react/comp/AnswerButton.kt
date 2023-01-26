package com.trinitcore.quizprogress.react.comp

import com.trinitcore.quizprogress.core.Answer
import com.trinitcore.trinreactaux.TStateControl
import kotlinx.css.*
import org.w3c.dom.events.Event
import react.*
import styled.*

interface AnswerButtonProps : StyledProps {
    // var matrixAnswerFieldID: Int
    var label: String
    // var onClick: (AnswerButton.StateControl) -> Unit

    var stateControl: AnswerButton.StateControl

    var render: RBuilder.(isSelected: Boolean, label: String) -> Unit
}

interface AnswerButtonState : State {

}

class AnswerButton : RComponent<AnswerButtonProps, AnswerButtonState>() {

    /* TUS : SUB_COMPONENTS */

    /* DEIREADH : SUB_COMPONENTS */

    // TUS : STATE_CONTROL
    class StateControl(var isSelected: Boolean, val answer: Answer)
    // DEIREADH : STATE_CONTROL

    // TUS : EVENT HANDLERS
    private fun handleOnClick(event: Event) {
        // props.onClick(props.stateControl)
        // props.stateControl.manager.setState { props.stateControl.isSelected = true }
    }
    // DEIREADH : EVENT HANDLERS

    override fun RBuilder.render() {
        /*
        mButtonBase(className = props.className, onClick = ::handleOnClick) {
            css(Style.wrapper)
            // if (props.className != null) css { +props.className!! }
            styledDiv {
                css { +Style.inner }
                mRadio(color = MOptionColor.primary, checked = props.stateControl.isSelected) {

                }
                styledH4 {
                    css { +Style.label }
                    +props.label
                }
            }
        }
         */
        props.render(this, props.stateControl.isSelected, props.label)
    }

}

/*
fun RBuilder.answerButton(
        label: String,
        matrixAnswerFieldID: Int,
        onClick: (AnswerButton.StateControl) -> Unit,

        stateControl: AnswerButton.StateControl,
        addAsChild: Boolean = true,
        className: String? = null,
        handler: StyledHandler<AnswerButtonProps>? = null) =
        createStyled(AnswerButton::class as KClass<out RComponent<AnswerButtonProps, State>>, addAsChild) {
            attrs.stateControl = stateControl
            attrs.label = label
            attrs.onClick = onClick
            attrs.matrixAnswerFieldID = matrixAnswerFieldID
    setStyledPropsAndRunHandler(className, handler)
}
 */