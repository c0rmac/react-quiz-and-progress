package com.trinitcore.quizprogress.demo

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