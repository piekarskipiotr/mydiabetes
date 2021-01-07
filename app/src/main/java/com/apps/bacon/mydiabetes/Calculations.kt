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
        return roundToOneDecimal((cal - (carbohydrates * 4) ) / 100)
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

    fun carbohydratesByPieces(carbohydrates: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((carbohydrates * correctPieces) / pieces)
    }

    fun caloriesByPieces(calories: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((calories * correctPieces) / pieces)
    }

    fun proteinByPieces(protein: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((protein * correctPieces) / pieces)
    }

    fun fatByPieces(fat: Double, pieces: Int, correctPieces: Int): Double{
        return roundToOneDecimal((fat * correctPieces) / pieces)
    }

    fun carbohydratesByWeight(carbohydrates: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((carbohydrates * correctWeight) / weight)
    }

    fun caloriesByWeight(calories: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((calories * correctWeight) / weight)
    }

    fun proteinByWeight(protein: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((protein * correctWeight) / weight)
    }

    fun fatByWeight(fat: Double, weight: Double, correctWeight: Double): Double{
        return roundToOneDecimal((fat * correctWeight) / weight)
    }

    fun caloriesByValues(carbohydrates: Double, protein: Double, fat: Double): Double{
        return roundToOneDecimal((protein * 4) + (fat * 9) + (carbohydrates * 4))
    }

    private fun roundToOneDecimal(value: Double): Double{
        return round(value * 10.0) / 10.0
    }

}