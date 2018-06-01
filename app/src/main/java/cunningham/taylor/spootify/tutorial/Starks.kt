package cunningham.taylor.spootify.tutorial

import javax.inject.Inject

class Starks @Inject constructor() : House {

    override fun prepareForWar() {
        System.out.println("${this.javaClass.simpleName} prepared for war!")
    }

    override fun reportForWar() {
        System.out.println("${this.javaClass.simpleName} reporting for war!")
    }
}