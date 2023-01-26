package com.trinitcore.quizprogress.demo

import com.trinitcore.quizprogress.demo.wrapper.glamorCss
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.AnimatedSwitchProps
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.animatedSwitch
import com.trinitcore.quizprogress.demo.wrapper.reactroutertransition.spring
import kotlinext.js.js
import react.RBuilder
import react.RHandler

// TUS : Router animation configuration
private val hOCSwitchRule = glamorCss(js {
    this.position = "relative"
    this["& > div"] = js {
        this.position = "absolute"
    }
})

private fun glide(value: Double): dynamic {
    return spring(value, js("{ stiffness: 174, damping: 24 }"))
}

// DEIREADH

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