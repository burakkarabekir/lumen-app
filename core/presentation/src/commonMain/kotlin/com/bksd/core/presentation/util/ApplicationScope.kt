package com.bksd.core.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ApplicationScope : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default)
