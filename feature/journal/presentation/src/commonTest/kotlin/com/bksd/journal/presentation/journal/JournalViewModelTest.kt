package com.bksd.journal.presentation.journal

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.repository.MomentRepository
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetPagedMomentsUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import com.bksd.journal.domain.usecase.UpdateMomentUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: JournalViewModel
    private lateinit var getPagedMomentsUseCase: GetPagedMomentsUseCase
    private lateinit var syncMomentsUseCase: SyncMomentsUseCase
    private lateinit var deleteMomentUseCase: DeleteMomentUseCase

    private val testClock = object : Clock {
        override fun now() = kotlin.time.Instant.parse("2024-05-01T10:00:00Z")
    }
    private val testTimeZone = TimeZone.UTC

    private val fakeAudioPlayer = object : AudioPlayer {
        override val playbackState = MutableStateFlow(PlaybackState.STOPPED)
        override val currentPositionMs = MutableStateFlow(0L)
        override val durationMs = MutableStateFlow(0L)
        override val playbackAmplitudes = MutableStateFlow(emptyList<Float>())
        override suspend fun play(filePath: String): Result<Unit, AppError> = Result.Success(Unit)
        override suspend fun pause() {}
        override suspend fun resume() {}
        override suspend fun stop() {}
        override suspend fun seekTo(positionMs: Long) {}
        override fun release() {}
    }

    private val fakeSessionStorage = object : SessionStorage {
        override fun observeAuthState() = flowOf(true)
        override fun isLoggedIn() = true
        override suspend fun awaitReady() {}
        override fun getProfilePhotoUrl(): String? = null
        override fun observeProfilePhotoUrl() = flowOf<String?>(null)
        override suspend fun setRememberMe(enabled: Boolean) {}
        override fun isRememberMeEnabled() = flowOf(false)
        override suspend fun getLocalDataOwner(): String? = null
        override suspend fun setLocalDataOwner(uid: String?) {}
        override suspend fun setFirstLoginPending() {}
        override suspend fun consumeFirstLoginPending(): Boolean = false
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val fakeRepository = object : MomentRepository {
            override fun observeMomentsPaged(limit: Int, offset: Int) =
                flowOf(emptyList<Moment>())

            override fun observeAllMoments() = flowOf(emptyList<Moment>())

            override suspend fun syncMomentsPaged(limit: Int, offset: Int): Result<Unit, AppError> =
                Result.Success(Unit)

            override suspend fun syncAllMoments(): Result<Unit, AppError> =
                Result.Success(Unit)

            override suspend fun getMoment(id: String): Result<Moment, AppError> = Result.Success(
                Moment(
                    id = "1",
                    title = "Test Moment",
                    body = null,
                    createdAt = testClock.now(),
                    moods = listOf(Mood.REFLECTIVE)
                )
            )

            override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> =
                Result.Success(Unit)

            override suspend fun deleteMoment(id: String): Result<Unit, AppError> =
                Result.Success(Unit)
        }

        getPagedMomentsUseCase = GetPagedMomentsUseCase(fakeRepository)
        syncMomentsUseCase = SyncMomentsUseCase(fakeRepository)
        deleteMomentUseCase = DeleteMomentUseCase(fakeRepository)

        viewModel = JournalViewModel(
            getPagedMomentsUseCase = getPagedMomentsUseCase,
            syncMomentsUseCase = syncMomentsUseCase,
            sessionStorage = fakeSessionStorage,
            clock = testClock,
            timeZone = testTimeZone,
            audioPlayer = fakeAudioPlayer,
            deleteMomentUseCase = deleteMomentUseCase,
            updateMomentUseCase = UpdateMomentUseCase(fakeRepository)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState_visibleDateIsCorrect() {
        val expectedDate = LocalDate(2024, 5, 1)
        assertEquals(expectedDate, viewModel.state.value.visibleDate)
    }

    @Test
    fun testOnSearchQueryChange_updatesState() {
        val collectJob = CoroutineScope(testDispatcher).launch { viewModel.state.collect {} }
        viewModel.onAction(JournalAction.OnSearchQueryChange("test query"))
        assertEquals("test query", viewModel.state.value.searchQuery)
        collectJob.cancel()
    }
}
