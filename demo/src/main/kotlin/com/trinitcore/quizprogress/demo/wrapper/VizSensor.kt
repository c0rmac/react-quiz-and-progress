package com.trinitcore.quizprogress.demo.wrapper

import com.ccfraser.muirwik.components.utils.createStyled
import react.*

interface VizSensorProps : Props {
    var onChange: ((isVisible: Boolean) -> Unit)?
}

@JsModule("react-visibility-sensor")
private external val reactVisibilitySensorModule: dynamic
@Suppress("UnsafeCastFromDynamic") private val reactVisibilitySensorComponent: ComponentType<VizSensorProps> = reactVisibilitySensorModule.default
/** Viz Sensor */
fun RBuilder.vizSensor(className: String? = null, handler: RHandler<VizSensorProps>) = createStyled(
    reactVisibilitySensorComponent, className, handler) {

}