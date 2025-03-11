package tech.thdev.composable.architecture.app.feature.main

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import tech.thdev.composable.architecture.action.system.FlowCaActionStream
import tech.thdev.composable.architecture.alert.system.CaAlertAction
import tech.thdev.composable.architecture.base.CaViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    flowCaActionStream: FlowCaActionStream,
) : CaViewModel<Action>(flowCaActionStream, Action::class) {

    private val _sideEffect = Channel<SideEffect>(Channel.BUFFERED)
    internal val sideEffect = _sideEffect.receiveAsFlow()

    override suspend fun reducer(action: Action) {
        when (action) {
            is Action.ShowToast -> {
                _sideEffect.send(SideEffect.ShowToast)
            }

            is Action.ShowAlert -> {
                nextAction(
                    CaAlertAction.Dialog(
                        icon = action.icon,
                        title = action.title,
                        message = action.message,
                        confirmButtonText = action.confirmButtonText,
                        onConfirmButtonAction = CaAlertAction.Snack(
                            message = "Confirm",
                        ),
                        dismissButtonText = action.dismissButtonText,
                        onDismissButtonAction = CaAlertAction.Snack(
                            message = "Dismiss",
                        ),
                    )
                )
            }
        }
    }
}
