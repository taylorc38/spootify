package cunningham.taylor.spootify.application

import android.app.Application

class SpootifyApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }

    companion object {
        lateinit var INSTANCE : SpootifyApplication
            private set
    }
}