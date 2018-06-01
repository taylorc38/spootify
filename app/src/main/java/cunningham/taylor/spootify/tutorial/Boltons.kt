package cunningham.taylor.spootify.tutorial

import javax.inject.Inject

class Boltons @Inject constructor() : House {

    override fun prepareForWar() {
        System.out.println("${this.javaClass.simpleName} prepared for war!")
    }

    override fun reportForWar() {
        System.out.println("${this.javaClass.simpleName} reporting for war!")
    }

}