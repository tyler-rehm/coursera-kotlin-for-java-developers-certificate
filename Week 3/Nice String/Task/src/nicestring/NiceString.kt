package nicestring

fun String.isNice(): Boolean {
    // Condition 1: It doesn't contain substrings "bu", "ba" or "be"
    val condition1 = !this.contains(Regex("bu|ba|be"))

    // Condition 2: It contains at least three vowels (a, e, i, o, u)
    val vowels = this.count { it in "aeiou" }
    val condition2 = vowels >= 3

    // Condition 3: It contains a double letter
    val condition3 = Regex("(.)\\1").containsMatchIn(this)

    // A string is nice if at least two of the three conditions are met
    val satisfiedConditions = listOf(condition1, condition2, condition3).count { it }

    return satisfiedConditions >= 2
}


// Test cases
fun main() {
    val testCases = listOf("bac", "aza", "abaca", "baaa", "aaab")
    for (test in testCases) {
        println("$test is nice: ${test.isNice()}")
    }
}