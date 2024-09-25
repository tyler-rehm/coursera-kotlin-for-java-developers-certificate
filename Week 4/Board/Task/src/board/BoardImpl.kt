package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

// Implementation for SquareBoard
open class SquareBoardImpl(override val width: Int) : SquareBoard {
    // Create a list of cells for the board
    private val cells: List<Cell> = List(width * width) { index ->
        Cell(index / width + 1, index % width + 1)
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i in 1..width && j in 1..width) getCell(i, j) else null
    }

    override fun getCell(i: Int, j: Int): Cell {
        require(i in 1..width && j in 1..width) { "Invalid cell indices ($i, $j)" }
        return cells[(i - 1) * width + (j - 1)]
    }

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(this.i - 1, this.j)
            DOWN -> getCellOrNull(this.i + 1, this.j)
            LEFT -> getCellOrNull(this.i, this.j - 1)
            RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    // Map to store values associated with cells
    private val cellValues: MutableMap<Cell, T?> = mutableMapOf()

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return getAllCells().filter { cell -> predicate(cellValues[cell]) }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return getAllCells().firstOrNull { cell -> predicate(cellValues[cell]) }
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        // Check all cells, including those not initialized in the map (which default to null)
        return getAllCells().any { cell -> predicate(cellValues[cell]) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        // Check all cells, including those not initialized in the map (which default to null)
        return getAllCells().all { cell -> predicate(cellValues[cell]) }
    }
}