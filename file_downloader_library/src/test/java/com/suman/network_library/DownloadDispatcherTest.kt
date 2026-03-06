import com.suman.network_library.FakeHttpClient
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import com.suman.network_library.internal.DownloadDispatchers
import com.suman.network_library.internal.DownloadRequest
import com.suman.network_library.local_storage.DownloadStates

class DownloadDispatcherTest {

    private val dispatcher = DownloadDispatchers(FakeHttpClient())

    @Test
    fun testEnqueue() = runBlocking {
        val request = DownloadRequest.Builder(
            url = "https://fake.url/testfile.bin",
            dirPath = "/tmp",
            fileName = "enqueue_test.bin"
        ).build()

        var started = false
        var completed = false
        request.onStart = { started = true }
        request.onComplete = { completed = true }

        dispatcher.enqueue(request)

        while (!completed) delay(100)
        assertTrue("onStart should be called", started)
        assertTrue("onComplete should be called", completed)
    }

    @Test
    fun testPauseResumeCancel() = runBlocking {
        val request = DownloadRequest.Builder(
            url = "https://fake.url/testfile.bin",
            dirPath = "/tmp",
            fileName = "pause_resume_cancel.bin"
        ).build()

        var paused = false
        var resumed = false
        var canceled = false

        request.onPause = { paused = true }
        request.onResume = { _, _ -> resumed = true }
        request.onCancel = { canceled = true }

        dispatcher.enqueue(request)
        delay(100)
        dispatcher.pause(request)
        delay(100)
        dispatcher.resume(request)
        delay(100)
        dispatcher.cancel(request)
        delay(100)

        assertTrue("Pause should be triggered", paused)
        assertTrue("Resume should be triggered", resumed)
        assertTrue("Cancel should be triggered", canceled || request.state == DownloadStates.STATUS_FAILED)
    }
}
