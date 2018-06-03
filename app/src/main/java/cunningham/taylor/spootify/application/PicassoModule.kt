package cunningham.taylor.spootify.application

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PicassoModule {
    @Provides
    fun providePicasso() : Picasso {
        return Picasso.get()
    }
}