package com.bksd.journal.presentation.journal

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.domain.repository.MomentRepository
import com.bksd.journal.domain.usecase.GetMomentsByDateUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var getMomentsUseCase: GetMomentsByDateUseCase
    private lateinit var syncMomentsUseCase: SyncMomentsUseCase

    private val testClock = object : Clock {
        override fun now() = kotlin.time.Instant.parse("2024-05-01T10:00:00Z")
    }
    private val testTimeZone = TimeZone.UTC

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val fakeRepository = object : MomentRepository {
            override fun observeMoments(date: LocalDate) = flowOf(emptyList<Moment>())
            override suspend fun syncMoments(date: LocalDate): Result<Unit, AppError> =
                Result.Success(Unit)

            override suspend fun getMoment(id: String): Result<Moment, AppError> = Result.Success(
                Moment(
                    id = "1",
                    body = null,
                    createdAt = Clock.System.now(),
                    mood = Mood.REFLECTIVE
                )
            )

            override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> =
                Result.Success(Unit)
        }

        getMomentsUseCase = GetMomentsByDateUseCase(fakeRepository)
        syncMomentsUseCase = SyncMomentsUseCase(fakeRepository)

        viewModel = JournalViewModel(
            getMomentsByDateUseCase = getMomentsUseCase,
            syncMomentsUseCase = syncMomentsUseCase,
            clock = testClock,
            timeZone = testTimeZone
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
