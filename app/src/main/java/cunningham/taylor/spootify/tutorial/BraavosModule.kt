package cunningham.taylor.spootify.tutorial

import dagger.Module
import dagger.Provides

@Module
class BraavosModule (val cash: Cash, val soldiers: Soldiers) {

    @Provides
    fun provideCash() : Cash {
        return cash
    }

    @Provides
    fun provideSoldiers() : Soldiers {
        return soldiers
    }

}