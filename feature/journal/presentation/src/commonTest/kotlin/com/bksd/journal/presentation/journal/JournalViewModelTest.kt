package com.bksd.journal.presentation.journal

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.domain.repository.MomentRepository
import com.bksd.journal.domain.usecase.GetMomentsByRangeUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var getMomentsByRangeUseCase: GetMomentsByRangeUseCase
    private lateinit var syncMomentsUseCase: SyncMomentsUseCase

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

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val fakeRepository = object : MomentRepository {
            override fun observeMoments(date: LocalDate) = flowOf(emptyList<Moment>())
            override fun observeMomentsByRange(startDate: LocalDate, endDate: LocalDate) =
                flowOf(emptyList<Moment>())

            override suspend fun syncMoments(date: LocalDate): Result<Unit, AppError> =
                Result.Success(Unit)

            override suspend fun getMoment(id: String): Result<Moment, AppError> = Result.Success(
                Moment(
                    id = "1",
                    body = null,
                    createdAt = testClock.now(),
                    mood = Mood.REFLECTIVE
                )
            )

            override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> =
                Result.Success(Unit)
        }

        getMomentsByRangeUseCase = GetMomentsByRangeUseCase(fakeRepository)
        syncMomentsUseCase = SyncMomentsUseCase(fakeRepository)

        viewModel = JournalViewModel(
            getMomentsByRangeUseCase = getMomentsByRangeUseCase,
            syncMomentsUseCase = syncMomentsUseCase,
            clock = testClock,
            timeZone = testTimeZone,
            audioPlayer = fakeAudioPlayer
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState_selectedDateIsCorrect() {
        val expectedDate = LocalDate(2024, 5, 1)
        assertEquals(expectedDate, viewModel.state.value.selectedDate)
    }

    @Test
    fun testOnDateSelect_updatesState() {
        val newDate = LocalDate(2024, 5, 1)
        viewModel.onAction(JournalAction.OnDateSelect(newDate))
        assertEquals(newDate, viewModel.state.value.selectedDate)
    }
}
