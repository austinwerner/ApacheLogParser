package android.werner.apachelogparser.extensions

fun MutableMap<String,Int>.incrementIntValue(key: String) {
    if (this.containsKey(key)) {
        val curVal = this[key]
        this[key] = curVal!!.plus(1)
    } else {
        this[key] = 1
    }
}

fun MutableMap<String,MutableList<String>>.addToList(key: String, newVal: String) {
    if (this.containsKey(key)) {
        this[key]?.let {
            it.add(newVal)
            this[key] = it
        }
    } else {
        val list = mutableListOf(newVal)
        this[key] = list
    }
}