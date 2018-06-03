package cunningham.taylor.spootify.application

import android.content.Context
import com.squareup.picasso.Picasso
import dagger.Component

@Component(modules = [ContextModule::class, PicassoModule::class])
interface ApplicationComponent {
    fun application() : Context
    fun picasso() : Picasso
}