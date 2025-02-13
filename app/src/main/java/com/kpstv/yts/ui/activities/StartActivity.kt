package com.kpstv.yts.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kpstv.common_moviesy.extensions.makeFullScreen
import com.kpstv.common_moviesy.extensions.registerFragmentLifecycleForLogging
import com.kpstv.common_moviesy.extensions.viewBinding
import com.kpstv.navigation.FragmentNavigator
import com.kpstv.navigation.autoChildElevation
import com.kpstv.navigation.canFinish
import com.kpstv.yts.AppPreference
import com.kpstv.yts.BuildConfig
import com.kpstv.yts.cast.CastHelper
import com.kpstv.yts.data.db.localized.MainDao
import com.kpstv.yts.databinding.ActivityStartBinding
import com.kpstv.yts.defaultPreference
import com.kpstv.yts.extensions.AbstractNavigationOptionsExtensions.consume
import com.kpstv.yts.extensions.utils.AppUtils
import com.kpstv.yts.ui.fragments.*
import com.kpstv.yts.ui.helpers.ActivityIntentHelper
import com.kpstv.yts.ui.helpers.InitializationHelper
import com.kpstv.yts.ui.helpers.MainCastHelper
import com.kpstv.yts.ui.viewmodels.StartViewModel
import com.kpstv.yts.vpn.VPNDialogFragment
import com.kpstv.yts.vpn.VPNHelper
import com.kpstv.yts.vpn.VPNViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.dkbai.tinyhttpd.nanohttpd.webserver.SimpleWebServer
import javax.inject.Inject
import kotlin.reflect.KClass

@AndroidEntryPoint
class StartActivity : AppCompatActivity(), FragmentNavigator.Transmitter, LibraryFragment.Callbacks {
    private val binding by viewBinding(ActivityStartBinding::inflate)
    private val navViewModel by viewModels<StartViewModel>()
    private val intentHelper by lazy { ActivityIntentHelper(navViewModel) { navigator.getCurrentFragment() } }
    private val castHelper = CastHelper()
    private val mainCastHelper by lazy { MainCastHelper(this, lifecycle, castHelper) }
    private val vpnHelper by lazy { VPNHelper(this) { navigator.show(it) } }
    private val appPreference by defaultPreference()

    private lateinit var navigator: FragmentNavigator

    @Inject
    lateinit var initializationHelper: InitializationHelper

    @Inject
    lateinit var repository: MainDao

    override fun getNavigator(): FragmentNavigator = navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        makeFullScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navigator = FragmentNavigator.with(this, savedInstanceState)
            .initialize(binding.fragmentContainer)
        navigator.autoChildElevation()

        navViewModel.navigation.observe(this, navigationObserver)
        navViewModel.errors.observe(this, errorObserver)

        if (savedInstanceState == null && !intentHelper.handle(intent)) {
            navViewModel.navigateTo(Screen.SPLASH)
        }

        if (CastHelper.isCastingSupported(this)) {
            SimpleWebServer.init(this, BuildConfig.DEBUG)
            castHelper.initCastSession(this)
            mainCastHelper.setUpCastRelatedStuff()
        }

        if (appPreference.isVPNEnabled())
            vpnHelper.initializeAndObserve()
       /* registerFragmentLifecycleForLogging { fragment, state ->
            if (BuildConfig.DEBUG) Log.e(fragment::class.simpleName, "=> $state")
        }*/
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intentHelper.handle(intent)
    }

    override fun onStart() {
        super.onStart()
        initializationHelper.initializeDependencies()
    }

    override fun getCastHelper(): CastHelper = castHelper

    private val navigationObserver = Observer { navOptions: StartViewModel.NavigationOption? ->
        navOptions?.consume { navigator.navigateTo(navOptions.clazz, navOptions.options) }
    }

    private val errorObserver = Observer { error: Exception? ->
        error?.let {
            AppUtils.showUnknownErrorDialog(this, error) { finish() }
        }
    }

    enum class Screen(val clazz: KClass<out Fragment>) {
        SPLASH (SplashFragment::class),
        WELCOME_DISCLAIMER (WelcomeDisclaimerFragment::class),
        WELCOME_CARRIER_DISCLAIMER (WelcomeCarrierFragment::class),
        MAIN (MainFragment::class),
        SETTING (SettingFragment::class),
        MORE (MoreFragment::class),
        SEARCH (SearchFragment::class),
        DETAIL (DetailFragment::class)
    }

    override fun onBackPressed() {
        if (navigator.canFinish()) super.onBackPressed()
    }

    override fun onDestroy() {
        SimpleWebServer.stopServer()
        super.onDestroy()
    }
}