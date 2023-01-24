package com.trinitcore.quizprogress.demo.wrapper.reactroutertransition

import react.*

interface AnimatedSwitchProps : Props {
    @Suppress("atEnter")
    var atEnter: Props
    @Suppress("atLeave")
    var atLeave: Props
    @Suppress("atActive")
    var atActive: Props

    var mapStyles: dynamic

    var className: String?

    var switchRule: dynamic
    // var css:
}

/**
 * Animated switch
 */
fun RBuilder.animatedSwitch(
    className: String? = null,
    handler: RHandler<AnimatedSwitchProps>
) = child(
    AnimatedSwitch::class
) {
    attrs {
        this.className = className
    }
    handler.invoke(this)
}