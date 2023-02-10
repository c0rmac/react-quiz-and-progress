# React Quiz and Progress
Tóig Ceistneoir le barra foráis le haghaidh aip React Js

[Féach mo Phortfolio](https://c0rmac.github.io/ga/portfolio/) nó [Cuir tuilleadh aithne](https://c0rmac.github.io/ga/portfolio/react-quiz-and-progress/) ar React Quiz and Progress.

## Inspreagadh
I 2020, bhí mé ag oibriú ar fheidhmchlár ar a dtugtar [House of Costs](https://c0rmac.github.io/portfolio/house-of-costs/)
inar fiafraíodh den úsáideoir ceistneoir pearsantaithe a chomhlíonadh.
D'eascair castacht éagsúil ar struchtúr na gceisteanna, de bharr gur nocht freagruithe áirithe
ar cheisteanna áirithe fo-cheisteanna nua ina ndiadih. Thabharfadh siúd tuiscint
go mbeadh an t-uafas ceisteanna le freagairt ag an úsáideoir, agus ní mó ná maith é nach raibh
comhartha ar bith le haghaidh an méid a bheadh fágtha ag úsáideoir le freagairt.

Dhá bharr sin, d'inspreag seo mo choincheap-sa - barra líneach foráis a thabharfadh comhartha
iomasach don úsáideoir ar an méid den cheistneoir a bheadh freagraithe ag an úsáideoir.

De réir mar a fhreagraíonns an t-úsáideoir ceisteanna, léirítear tuilleadh foráis ar
an mbarra líneach. I gcás ceisteanna atá "neamhspleách" (eadhon: nach mbraitheann ar
fhreagra ar a gceisteanna roimhe), bronntar méadú mór don bharra foráis.
I gcás ceisteanna atá "spleách", bronntar méadú laghdaithe don bharra foráis.
I gcás ceisteanna spleácha a bhraitheanns ar cheisteanna eile (agus a bhraitheanns ar cheisteanna eile agus mar sin de),
bronntar méadú laghdaithe aríst don bharra foráis.

## Taispeántas
Más suim leat triail a bhaint as an mbreiseán gan socrú ar bith a dhéanamh, molaim dhaoibh breathnú
ar chód [demo anseo](https://github.com/c0rmac/react-quiz-and-progress/tree/main/demo). 
Beidh oraibh an demo a oscailt in Intelij Idea mar is tionscadal Kotlin Js é. 
Úsáidim material-ui mar bhunús an comhéadain.

![Demo](https://raw.githubusercontent.com/c0rmac/react-quiz-and-progress/main/README/ezgif-3-629ea576ae.gif)

## Sonraí Teicniúla
Forbraíodh an breiseán i gKotlin React Js (mar gheall ar gur dual dom Kotlin Multiplatform).
Foilseod tuilleadh sonraí teicniúla ar mo bhreiseán ar mo bhlag go luath.

## Overview

### QuestionSetProgressController
Tugann an aicme seo stádas an Cheistneora (eolas cosúil le: forás bainte amach, an chéad cheist
eile a dhéanamh amach, an bhfuil an ceistneoir comhlíonta agus mar sin de). Ba cheart
go gcoinnítear é san aicme stádais.

```kotlin
QuestionSetProgressController(
    proposedQuestionSet = ProposedQuestionSet(
        // Cuir chuile cheist ba mhian leat a chur ar an úsáideoir anseo.
        // Féach an chéad roinn eile le haghaidh tuilleadh sonraí
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
        // Mapa ó Question.id go dtí Answer.id. Eadhon, má roghnaíonn an t-úsáideoir
        // freagra le id=3 ar cheist le id=1, sannfar 1->3 sa mapa
        answerSet = mutableMapOf(), 
        // Define a prefilled answer set, if needed
        // Sanntar tacar freagra ré-chomhlíonaithe, más gá
        defaultAnswerSet = null
    ),
    // Cuir in iúl an cheist ar a bhfuil an t-úsáideoir
    currentQuestionID = 1,
    // Aisghloacha éagsúla
    questionOnChange = ::questionOnChange,
    questionSetOnFulfilled = ::questionSetOnFulfilled
)
```

### Question
Is féidir ceisteanna a shannadh agus iad a pharaiméadarú mar cheisteanna
neamhspleácha a thabharfaidh freagra i gcónaí. Chomh maith leis sin, is feidir ceisteanna
a pharaiméadarú a bhraitheanns ar fhreagra ar a cheist roimhe. Ag seo sampla de thacar ceiste
```kotlin
arrayOf(
    // Sampla de cheist neamhspleách
    Question(
        id = 1, question = "Question 1",
        dependentAnswerIds = emptySet(), 
        // Ceist atá sannaithe neamhspleách de bharr go bhfuil dependentQuestionID = null
        dependentQuestionID = null,
        answers = listOf(
            Answer(id = 1, answer = "Answer 1"),
            Answer(id = 2, answer = "Answer 2"),
            Answer(id = 3, answer = "Answer 3"),
            Answer(id = 4, answer = "Answer 4"),
            Answer(id = 5, answer = "Answer 5"),
        )
    ),
    // Sampla de cheist spleách
    Question(
        id = 2, question = "Question 1.1",
        // Seachas an cheist roimhe, sanntar dependentQuestionID = 1
        // agus tá dependentAnswerIds sannaithe.
        // Eadhon, cuirfear an cheist seo (de id=2), ar an gcoinníoll amháin
        // go roghnaítear freagra (de id=2 nó id=3) ar cheist de id=1.
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

## Socrú
Tá bealaí éagsúla ann is féidir an Ceistneoir a shocrú. B'fhearr liom é a shocrú
ach leas a bhaint as react router nó go dtaispeántar gach ceist ina "leathanach" féin.

Cuirim fainic ort nach mbainim mórán leasa as teicnící nua-aimsearthacha React Js,
agus gabhaim mo leithscéal as an seanchód dá réir.

### Javascript ES6 (Gnáth-thionscadal React Js)
Foilseod treoracha air seo amach anseo mar tá mé éirithe as cleachtadh ar ES6.
Ach ba chóir go mbeadh sé cosúil le socrú Kotlin Js, ós rud é go bhfuil
do dhóthain cleachtaidh agat Kotlin bunúsach a léamh. Bheinn an-bhuíoch d'aon chúnamh!

Mar is gnáth, is féidir an breiseán a fháilt ach a leanas a rith

``` 
npm i react-quiz-and-progress
```

### Kotlin Js
I bhur gcomhad build.gradle.kts file, cuir leis an méid seo a leanas
```kotlin
// Required dependency
implementation(npm("react-quiz-and-progress","1.0.0"))

// Usual Kotlin React Js (legacy) dependencies
implementation("org.jetbrains.kotlin-wrappers:kotlin-react-legacy:18.2.0-pre.479")
implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-legacy:18.2.0-pre.479")
implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.6-pre.479")
implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:5.2.0-pre.256-kotlin-1.5.31")
implementation(npm("react-router-transition", "2.0.0"))
implementation(npm("glamor", "2.20.40"))
implementation(npm("react-visibility-sensor", "5.1.1"))
implementation(npm("react-animate-height", "2.0.21"))
```

Comhad Client.kt
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

AnimatedSwitch.kt (Roghnach)
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

Comhad Welcome.kt
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
