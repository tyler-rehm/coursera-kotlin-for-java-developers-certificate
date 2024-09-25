package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPos = 0
    var wrongPos = 0
    val secretRemainder = secret.toCharArray()

    guess.forEachIndexed { index, c ->
        if(secret[index] == c) {
            rightPos++
            secretRemainder[index] = '0'
        }
    }

    guess.forEachIndexed { index, c ->
        if(secret[index] != c) {
            val ii = secretRemainder.indexOf(c)
            if (ii != -1 && guess[ii] != c) {
                wrongPos++
                secretRemainder[ii] = '0'
            }
        }
    }

    return Evaluation(rightPos, wrongPos)
}