package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filter { driver ->
        trips.none { it.driver == driver }
    }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.count { trip -> passenger in trip.passengers } >= minTrips
    }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips.filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupBy { it }
        .filterValues { it.size > 1 }
        .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return allPassengers.filter { passenger ->
        // Filter trips involving this passenger
        val tripsForPassenger = trips.filter { passenger in it.passengers }

        // Count trips with discount and without discount
        val tripsWithDiscount = tripsForPassenger.count { it.discount != null && it.discount > 0.0 }
        val tripsWithoutDiscount = tripsForPassenger.size - tripsWithDiscount

        // A "smart" passenger should have more trips with discount than without
        tripsWithDiscount > tripsWithoutDiscount
    }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) return null

    val durationPeriods = trips.groupBy {
        (it.duration / 10) * 10..(it.duration / 10) * 10 + 9
    }

    return durationPeriods.maxByOrNull { it.value.size }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false

    val totalIncome = trips.sumOf { it.cost }
    val driversIncome = trips.groupBy { it.driver }
        .mapValues { (_, trips) -> trips.sumOf { it.cost } }
        .values
        .sortedDescending()

    val top20PercentDriversCount = (allDrivers.size * 0.2).toInt().coerceAtLeast(1)
    val topIncome = driversIncome.take(top20PercentDriversCount).sum()

    return topIncome >= 0.8 * totalIncome
}