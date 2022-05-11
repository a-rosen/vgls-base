package com.vgleadsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var perfTracker: PerfTracker

    @JvmField
    @field:[Inject Named("RunningTest")]
    var isRunningTest: Boolean = false

    protected val idArgs: IdArgs by args()

    private var perfTrackingStarted = false

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getVglsFragmentTag(): String

    abstract fun getPerfSpec(): PerfSpec

    abstract fun getTrackingScreen(): TrackingScreen

    open fun getDetails() = getArgs()?.id?.toString() ?: ""

    open fun getPerfScreenName(): String {
        val details = getDetails()

        return if (details.isNotEmpty()) {
            "${getTrackingScreen()}:$details"
        } else {
            getTrackingScreen().toString()
        }
    }

    @Suppress("SwallowedException")
    open fun getArgs(): IdArgs? {
        return try {
            idArgs
        } catch (ex: IllegalArgumentException) {
            null
        } catch (ex: ClassCastException) {
            null
        }
    }

    open fun onBackPress() = false

    open fun disablePerfTracking() = false

    open fun getPerfTrackingMinScreenHeight() = 580

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    @SuppressWarnings("ReturnCount")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (disablePerfTracking() || isRunningTest) {
            Timber.d("Not starting perf tracker: Perf tracking is disabled for screen ${getPerfScreenName()}.")
            return
        }

        if (savedInstanceState != null) {
            Timber.i("Not starting perf tracker: Screen ${getPerfScreenName()} was recreated.")
            return
        }

        val displayMetrics = resources.displayMetrics
        val heightPixels = displayMetrics.heightPixels
        val heightDp = heightPixels.pxToDp(displayMetrics)
        val minHeightDp = getPerfTrackingMinScreenHeight()

        if (heightDp < minHeightDp) {
            Timber.i(
                "Not starting perf tracker: Screen height $heightDp dp too small " +
                    "for screen ${getPerfScreenName()} (min height $minHeightDp dp)."
            )
            return
        }

        perfTracker.start(getPerfScreenName(), getPerfSpec())
        perfTrackingStarted = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater
        .inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!disablePerfTracking()) {
            perfTracker.onViewCreated(getPerfSpec())
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onStop() {
        super.onStop()
        if (perfTrackingStarted) {
            perfTracker.cancel(getPerfSpec())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (perfTrackingStarted) {
            perfTracker.clear(getPerfSpec())
            perfTrackingStarted = false
        }
    }

    protected fun setPerfTransitionStart() {
        perfTracker.onTransitionStarted(getPerfSpec())
    }

    protected fun setPerfPartialLoadComplete() {
        perfTracker.onPartialContentLoad(getPerfSpec())
    }

    protected fun setPerfFulllLoadComplete() {
        perfTracker.onFullContentLoad(getPerfSpec())
    }

    protected fun showError(
        error: Throwable,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0
    ) {
        val message = error.message ?: error::class.simpleName ?: "Unknown Error"
        showError(message, action, actionLabel)
    }

    protected fun showError(
        message: String,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0
    ) {
        Timber.e("Displayed error: $message")
        tracker.logError(message)
        showSnackbar(message, action, actionLabel)
    }

    protected fun showSnackbar(
        message: String,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0,
        length: Int = Snackbar.LENGTH_LONG
    ): Snackbar? {
        val toplevel = view?.parent as? CoordinatorLayout ?: view ?: return null
        val snackbar = Snackbar.make(toplevel, message, length)

        if (action != null && actionLabel > 0) {
            snackbar.setAction(actionLabel, action)
        }

        snackbar.show()
        return snackbar
    }

    protected fun getFragmentRouter() = (activity as FragmentRouter)
}
