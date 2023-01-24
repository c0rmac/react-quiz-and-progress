package com.trinitcore.quizprogress.demo.wrapper

import com.ccfraser.muirwik.components.utils.*
import org.w3c.dom.events.Event
import react.*
import styled.StyledHandler

@JsModule("@material-ui/core/ButtonBase")
private external val buttonBaseModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val buttonBaseComponent: ComponentType<MButtonBaseProps> = buttonBaseModule.default

interface MButtonBaseProps: StyledPropsWithCommonAttributes {
    var centerRipple: Boolean
    var component: String
    var disabled: Boolean
    var disableRipple: Boolean
    var focusRipple: Boolean
    var onKeyboardFocus: (Event) -> Unit

    @JsName("TouchRippleProps")
    var touchRippleProps: Props?
}

fun RBuilder.buttonBase(
    disabled: Boolean = false,
    onClick: ((Event) -> Unit)? = null,
    hRefOptions: HRefOptions? = null,

    addAsChild: Boolean = true,
    className: String? = null,
    handler: StyledHandler<MButtonBaseProps>? = null
) {
    createStyled(buttonBaseComponent, className, handler) {
        attrs.disabled = disabled
        hRefOptions?.let { setHRefTargetNoOpener(attrs, it) }
        onClick?.let { attrs.onClick = onClick }
    }
}