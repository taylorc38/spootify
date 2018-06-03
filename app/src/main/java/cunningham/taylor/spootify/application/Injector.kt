package cunningham.taylor.spootify.application

class Injector private constructor() {
    companion object {
        fun get() : ApplicationComponent =
                SpootifyApplication.INSTANCE.applicationComponent
    }
}