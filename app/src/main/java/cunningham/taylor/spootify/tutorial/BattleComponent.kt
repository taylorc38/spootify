package cunningham.taylor.spootify.tutorial

import dagger.Component

@Component(modules = [BraavosModule::class, WarModule::class])
interface BattleComponent {
    fun getWar() : War
    fun getCash() : Cash
    fun getSoldiers() : Soldiers
}