@file:JsModule("react-router-transition")

package com.trinitcore.quizprogress.demo.wrapper.reactroutertransition

import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.AnimatedSwitchProps
import react.Component
import react.ReactElement
import react.State


@JsName("AnimatedSwitch")
external class AnimatedSwitch : Component<AnimatedSwitchProps, State> {
    override fun render(): ReactElement?
}

@JsName("spring")
external fun spring(value: Double, parameters: dynamic) : dynamic