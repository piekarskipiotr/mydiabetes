package com.apps.bacon.mydiabetes

import kotlin.math.round

class Calculations {
    fun carbohydrateExchanges(carbohydrates: Double): Double {
        return roundToOneDecimal(carbohydrates / 10)
    }

    fun proteinFatExchangers(protein: Double, fat: Double): Double{
        return roundToOneDecimal((protein * 4 + fat * 9) / 100)
    }

    fun proteinFatExchangersByCal(cal: Double, carbohydrates: Double): Double{
        return roundToOneDecimal(cal - (carbohydrates * 4) / 100)
    }

    fun carbohydrateExchangesByWeight(carbohydrateExchanges: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((carbohydrateExchanges * correctWeight) / weight)
    }

    fun proteinFatExchangersByWeight(proteinFatExchangers: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((proteinFatExchangers * correctWeight) / weight)
    }

    fun carbohydrateExchangesByPieces(carbohydrateExchanges: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((carbohydrateExchanges * correctPieces) / pieces)
    }

    fun proteinFatExchangersByPieces(proteinFatExchangers: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((proteinFatExchangers * correctPieces) / pieces)
    }

    fun carbohydrateByPieces(carbohydrate: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((carbohydrate * correctPieces) / pieces)
    }

    fun proteinByPieces(protein: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((protein * correctPieces) / pieces)
    }

    fun fatByPieces(fat: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((fat * correctPieces) / pieces)
    }

    fun carbohydrateByWeight(carbohydrate: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((carbohydrate * correctWeight) / weight)
    }

    fun proteinByWeight(protein: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((protein * correctWeight) / weight)
    }

    fun fatByWeight(fat: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((fat * correctWeight) / weight)
    }

    private fun roundToOneDecimal(value: Double): Double{
        return round(value * 10.0) / 10.0
    }

}