package cunningham.taylor.spootify.player

data class Track(
        val title: String,
        val rawId: Int?
    // TODO add mid, URI, etc when implementing StreamingAudioPlayer
)