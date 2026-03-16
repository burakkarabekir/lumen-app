package com.bksd.core.data.media

import dev.gitlive.firebase.storage.Data

expect fun createStorageData(bytes: ByteArray): Data
