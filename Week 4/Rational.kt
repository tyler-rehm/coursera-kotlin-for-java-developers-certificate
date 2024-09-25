package rationals

import java.math.BigInteger

class Rational private constructor(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    companion object {
        // Factory method to create a Rational
        fun create(numerator: BigInteger, denominator: BigInteger): Rational {
            if (denominator == BigInteger.ZERO) {
                throw IllegalArgumentException("Denominator cannot be zero")
            }
            return Rational(numerator, denominator).normalize()
        }

        // Extension function to create Rational from string
        fun String.toRational(): Rational {
            val parts = this.split("/")
            return if (parts.size == 1) {
                Rational.create(BigInteger(parts[0]), BigInteger.ONE)
            } else {
                Rational.create(BigInteger(parts[0]), BigInteger(parts[1]))
            }
        }

        // Extension function for converting Int, Long or BigInteger to Rational via divBy
        infix fun Int.divBy(denominator: Int) = Rational.create(BigInteger.valueOf(this.toLong()), BigInteger.valueOf(denominator.toLong()))
        infix fun Long.divBy(denominator: Long) = Rational.create(BigInteger.valueOf(this), BigInteger.valueOf(denominator))
        infix fun BigInteger.divBy(denominator: BigInteger) = Rational.create(this, denominator)
    }

    // Normalize the rational number by reducing the numerator and denominator by their GCD
    private fun normalize(): Rational {
        val gcd = numerator.gcd(denominator)
        val sign = denominator.signum().toBigInteger()
        return Rational(numerator.divide(gcd).multiply(sign), denominator.divide(gcd).multiply(sign))
    }

    // Overriding basic arithmetic operators for Rational class
    operator fun plus(other: Rational): Rational {
        val commonDenominator = denominator.multiply(other.denominator)
        val newNumerator = numerator.multiply(other.denominator).add(other.numerator.multiply(denominator))
        return Rational.create(newNumerator, commonDenominator)
    }

    operator fun minus(other: Rational): Rational {
        val commonDenominator = denominator.multiply(other.denominator)
        val newNumerator = numerator.multiply(other.denominator).subtract(other.numerator.multiply(denominator))
        return Rational.create(newNumerator, commonDenominator)
    }

    operator fun times(other: Rational): Rational {
        return Rational.create(numerator.multiply(other.numerator), denominator.multiply(other.denominator))
    }

    operator fun div(other: Rational): Rational {
        return Rational.create(numerator.multiply(other.denominator), denominator.multiply(other.numerator))
    }

    operator fun unaryMinus(): Rational {
        return Rational.create(numerator.negate(), denominator)
    }

    // Overriding comparison operators
    override fun compareTo(other: Rational): Int {
        return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator))
    }

    // String representation in normalized form
    override fun toString(): String {
        return if (denominator == BigInteger.ONE) {
            "$numerator"
        } else {
            "$numerator/$denominator"
        }
    }

    // Check if a rational number is within a range
    operator fun contains(rational: Rational): Boolean {
        return this <= rational && rational <= this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false
        return numerator == other.numerator && denominator == other.denominator
    }

    override fun hashCode(): Int {
        return 31 * numerator.hashCode() + denominator.hashCode()
    }
}

// Extension functions for creating Rational using `divBy`
infix fun Int.divBy(denominator: Int) = Rational.create(BigInteger.valueOf(this.toLong()), BigInteger.valueOf(denominator.toLong()))
infix fun Long.divBy(denominator: Long) = Rational.create(BigInteger.valueOf(this), BigInteger.valueOf(denominator))
infix fun BigInteger.divBy(denominator: BigInteger) = Rational.create(this, denominator)

// Extension function to convert String to Rational
fun String.toRational(): Rational {
    val parts = this.split("/")
    return if (parts.size == 1) {
        Rational.create(BigInteger(parts[0]), BigInteger.ONE)
    } else {
        Rational.create(BigInteger(parts[0]), BigInteger(parts[1]))
    }
}

// Extension function to convert String to BigInteger
fun String.toBigInteger() = BigInteger(this)


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}