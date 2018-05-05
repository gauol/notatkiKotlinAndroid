package pl.galczyk.reminder

import android.os.Handler
import android.os.HandlerThread

class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private var mWorkerHandler: Handler? = null

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        while (mWorkerHandler == null){}
        mWorkerHandler?.post(task)
    }
}