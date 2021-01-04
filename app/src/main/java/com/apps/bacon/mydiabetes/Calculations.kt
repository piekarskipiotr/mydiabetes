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

    fun proteinFatExchangersByWeight(proteinFatExchangers: Double, pieces: Double, correctPieces: Double): Double{
        return roundToOneDecimal((proteinFatExchangers * correctPieces) / pieces)
    }

    fun carbohydrateExchangesByPieces(carbohydrateExchanges: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((carbohydrateExchanges * correctPieces) / pieces)
    }

    fun proteinFatExchangersByPieces(proteinFatExchangers: Double, weight: Int, correctWeight: Int): Double{
        return roundToOneDecimal((proteinFatExchangers * correctWeight) / weight)
    }

    private fun roundToOneDecimal(value: Double): Double{
        return round(value * 10.0) / 10.0
    }

}