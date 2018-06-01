package cunningham.taylor.spootify.tutorial

import dagger.Module
import dagger.Provides

@Module
class WarModule(private val starks: Starks, private val boltons: Boltons) {

    @Provides
    fun provideWar() : War {
        return War(starks, boltons)
    }

}