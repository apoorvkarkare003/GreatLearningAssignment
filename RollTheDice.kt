import java.util.*

fun assignOrder(numOfPlayers: Int, sum: Int) {
    val randomOrderSet: MutableSet<Int> = mutableSetOf()
    while (randomOrderSet.size < numOfPlayers) {
        randomOrderSet.add((1..numOfPlayers).random())
    }
    val randomList = randomOrderSet.toList()

    val skoreMap: MutableMap<Int, Int> = mutableMapOf()
    for (i in 0..numOfPlayers - 1) {
        skoreMap.put(randomList.get(i), 0) // default ranking
    }

    startGame(randomList, skoreMap, sum)

}

fun startGame(playerList: List<Int>, ranking: MutableMap<Int, Int>, sum: Int) {
    val restrictedList: MutableMap<Int, Int> = mutableMapOf()

    fun internal(randomList: List<Int>, ranking: MutableMap<Int, Int>, sum: Int) {
        var count = 0
        while (count < randomList.size) {

            if (restrictedList.get(randomList.get(count)) != null && restrictedList.get(randomList.get(count))!! > 1) {
                println("Player-${randomList.get(count)} you have to skip this chance since you have rolled 1 twice in a row")
                restrictedList.remove(randomList.get(count))
                count++
                continue
            }

            println("Player-${randomList.get(count)} it's your turn (Press 'r' to roll the dice)")

            val scanner = Scanner(System.`in`)
            val char = scanner.next().single()

            if (char == 'r') {
                //Simulating dice roll
                val rollingDice: MutableSet<Int> = mutableSetOf()
                while (rollingDice.size < 6) {
                    rollingDice.add((1..6).random())
                }

                rollingDice.forEachIndexed { index, skore ->
                    Thread.sleep(100)
                    if (index == rollingDice.size - 1) {
                        // Keeping last number as rolled value
                        ranking.put(randomList.get(count), (ranking.get(randomList.get(count))
                                ?: 0) + skore)
                        println("==> Rolled ${skore}")
                        if (ranking.get(randomList.get(count)) ?: 0 >= sum) {
                            println("Hurrah! Player-${randomList.get(count)}, you finished at position ${playerList.size - ranking.size + 1} in the game")
                            count++
                        }else if (skore == 1) {
                            restrictedList.put(randomList.get(count), (restrictedList.get(randomList.get(count))
                                    ?: 0) + 1)
                            count++
                        } else {
                            // Removing player from restricted list (if exists) if he rolls other than 1
                            restrictedList.remove(randomList.get(count))
                            if (skore == 6) {
                                // Removing player from restricted list (if exists) if he rolls other than 1
                                println("Great Player-${randomList.get(count)}! you rolled 6, roll it again")
                            }else{
                                count++
                            }
                        }
                    } else {
                        print("..${skore}")
                    }
                }
                println(ranking.toList().sortedByDescending { (_, value) -> value }.toMap())
            }
        }

        val unfinishedRankingTable = ranking.filter { it.value < sum }.toMutableMap()
        val finishedPlayers = ranking.filter { it.value >= sum }.toMutableMap()
        if (unfinishedRankingTable.size > 0) {
            internal(randomList.filter { !(it in finishedPlayers.keys) }
                    , unfinishedRankingTable, sum)
        }
    }

    internal(playerList, ranking, sum)
}

fun main(args: Array<String>) {
    assignOrder(3, 15)
}
