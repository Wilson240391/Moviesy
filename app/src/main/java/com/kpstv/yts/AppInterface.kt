package com.kpstv.yts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.preference.PreferenceManager
import com.danimahardhika.cafebar.CafeBar
import com.kpstv.common_moviesy.extensions.utils.CommonUtils
import com.kpstv.yts.data.models.AppDatabase
import com.kpstv.yts.extensions.SearchType
import com.kpstv.yts.extensions.SimpleCallback
import com.kpstv.yts.extensions.errors.MovieNotFoundException
import com.kpstv.yts.extensions.errors.SSLHandshakeException
import com.kpstv.yts.extensions.utils.AppUtils
import com.kpstv.yts.ui.helpers.ThemeHelper
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class AppInterface {
    companion object {
        private var TAG = "AppInterface"

        var YIFY_BASE_URL = "https://yts-subs.com"
        var YTS_BASE_URL = "https://yts.mx"
        var YTS_BASE_API_URL = "$YTS_BASE_URL/api/v2/"
        var TMDB_BASE_URL = "https://api.themoviedb.org/3/"
        var TMDB_IMAGE_PREFIX = "https://image.tmdb.org/t/p/w500"
        var TMDB_API_KEY = "" // Will insert TMDB key here
        var COUNTRY_FLAG_JSON_URL = "https://pastebin.com/raw/H0CYRdJ9"
        var APP_DATABASE_URL =
            "https://raw.githubusercontent.com/KaustubhPatange/Moviesy/master/UPDATE"
        var APP_IMAGE_URL =
            "https://raw.githubusercontent.com/KaustubhPatange/Moviesy/master/app/src/main/ic_launcher-playstore.png"
        var SUGGESTION_URL =
            "https://suggestqueries.google.com/complete/search?ds=yt&client=firefox&q="
        var EASTER_EGG_URL = "https://youtu.be/dQw4w9WgXcQ"
        var STORAGE_LOCATION =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var STREAM_LOCATION = "torrents"
        var SUBTITLE_LOCATION = File(Environment.getExternalStorageDirectory(), "Subtitles")
        var ANONYMOUS_TORRENT_DOWNLOAD = true
        var DOWNLOAD_TIMEOUT_SECOND = 40
        var DOWNLOAD_CONNECTION_TIMEOUT = 100
        var MOVIE_SPAN_DIFFERENCE = 3
        var QUERY_SPAN_DIFFERENCE = 6
        var QUERY_SPAN_DIFFERENCE_UPCOMING = 1
        var CUSTOM_LAYOUT_YTS_SPAN = 8
        const val MOVIE_FETCH_SIZE = 10
        var IS_PREMIUM_UNLOCKED = true
        var IS_ADAPTIVE_SEARCH = true
        var SUGGESTION_SEARCH_TYPE = SearchType.TMDB

        const val FEATURED_QUERY = "movies=featured&client=yts"
        const val UPCOMING_QUERY = "movies=upcoming&client=yts"

        const val TORRENT_NOT_SUPPORTED = "com.kpstv.yts.TORRENT_NOT_SUPPORTED"
        const val MODEL_UPDATE = "com.kpstv.yts.MODEL_UPDATE"
        const val STOP_SERVICE = "com.kpstv.yts.STOP_SERVICE"
        const val PENDING_JOB_UPDATE = "com.kpstv.yts.PENDING_JOB_UPDATE"
        const val EMPTY_QUEUE = "com.kpstv.yts.EMPTY_QUEUE"
        const val PAUSE_JOB = "com.kpstv.yts.PAUSE_JOB"
        const val UNPAUSE_JOB = "com.kpstv.yts.ADD_ONLY_JOB"
        const val REMOVE_CURRENT_JOB = "com.kpstv.yts.REMOVE_CURRENT_JOB"

        const val UPDATE_URL = "com.kpstv.yts.UPDATE_URL"

        const val GENERAL_FRAG = "com.kpstv.yts.GENERAL_FRAG"
        const val STORAGE_FRAG = "com.kpstv.yts.STORAGE_FRAG"
        const val LOOK_FEEL_FRAG = "com.kpstv.yts.LOOK_FEEL_FRAG"
        const val ACCOUNT_FRAG = "com.kpstv.yts.ACCOUNT_FRAG"
        const val BACKUP_FRAG = "com.kpstv.yts.BACKUP_FRAG"
        const val DEVELOPER_FRAG = "com.kpstv.yts.DEVELOPER_FRAG"
        const val ABOUT_FRAG = "com.kpstv.yts.ABOUT_FRAG"

        const val ACTION_REPLACE_FRAG = "com.kpstv.yts.action_replace_frag"

        const val IS_FIRST_LAUNCH_PREF = "is_first_launch_pref"
        const val PROXY_CHECK_PREF = "proxy_check_pref"

        const val PURCHASE_REGEX_PATTERN = "moviesy_premium_[\\d]+.json"

        fun setAppThemeNoAction(activity: Activity) {
            if (AppPreference(activity).getTheme() == ThemeHelper.AppTheme.LIGHT)
                activity.setTheme(R.style.AppTheme_Light_NoAction)
            activity.window.statusBarColor = CommonUtils.getColorFromAttr(activity, R.attr.colorBackground)
            if (Build.VERSION.SDK_INT >= 23 && ThemeHelper.isLightVariantTheme())
                activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        fun setAppThemeMain(activity: Activity) {
            if (AppPreference(activity).getTheme() == ThemeHelper.AppTheme.LIGHT) {
                activity.setTheme(R.style.AppTheme_Light_Main)
            }
        }

        @SuppressLint("SimpleDateFormat")
        val MainDateFormatter = SimpleDateFormat("yyyyMMddHH")

        @SuppressLint("SimpleDateFormat")
        val HistoryDateFormatter = SimpleDateFormat("yyyyMMddHHmmss")

        private var isSSLDialogActive = false
        private var isDataDialogActive = false
        @SuppressLint("Range") // TODO: Use something like snackbar
        fun handleRetrofitError(context: Context, t: Exception?, onCancel: SimpleCallback = {}) {
            var message = "Site is not responding. Try to change proxy from settings."
            if (t?.message == null) {
                message = "Error: Unknown, could not be determined"
            } else if (t.message?.contains("timeout") != true) {
                message = "Error: ${t.message}"
            }

            /** We will also set a preference lookup to check for proxy
             *  when app is launched again. */

            AppPreference(context).setShouldCheckProxy(true)

            if (t is SSLHandshakeException || t is javax.net.ssl.SSLHandshakeException || t?.message?.trim() == "HTTP 525") {
                if (!isSSLDialogActive) {
                    isSSLDialogActive = true
                    AppUtils.showSSLHandshakeDialog(context) {
                        isSSLDialogActive = false
                        onCancel.invoke()
                    }
                }
                return
            }

            if (t is MovieNotFoundException) {
                AppUtils.showMovieNotFoundDialog(context) {
                    onCancel.invoke()
                }
                return
            }

            if (t?.message?.contains("failed to connect to yts") == true && AppUtils.isMobileDataEnabled(context)) {
                if (!isDataDialogActive) {
                    isDataDialogActive = true
                    AppUtils.showDataErrorDialog(context) {
                        isDataDialogActive = false
                        onCancel.invoke()
                    }
                }
                return
            }

            if (context is Activity) {
                CafeBar.builder(context)
                    .content(message)
                    .floating(true)
                    .duration(CafeBar.Duration.INDEFINITE)
                    .neutralText(context.getString(R.string.dismiss))
                    .onNeutral {
                        context.finish()
                    }
                    .autoDismiss(false)
                    .showShadow(true)
                    .show()
            } else {
                Toasty.error(context, message, Toasty.LENGTH_LONG).show()
            }
        }

        fun formatDownloadSpeed(downloadSpeed: Float): String {
            val speed = downloadSpeed.toDouble() / 1000.00
            return if (speed > 1000) {
                DecimalFormat("0.0").format(speed / 1000) + " MB/s"
            } else DecimalFormat("0.0").format(speed) + " KB/s"
        }
    }
}