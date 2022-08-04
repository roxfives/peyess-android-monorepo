package com.peyess.salesapp.base

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel

abstract class MavericksViewModel<S : MavericksState>(initialState: S):
    MavericksViewModel<S>(initialState)