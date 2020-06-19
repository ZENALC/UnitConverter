package converter
import java.util.Scanner

fun main() {
    loop@ while (true) {
        val scanner = Scanner(System.`in`)
        var finalNumber: Double
        print("Enter what you want to convert (or exit): ")
        val input = scanner.nextLine()
        if (input == "exit") break

        val (baseNumber, baseUnit, finalUnit) = parseArgs(input)
        if (baseNumber == null) {
            println("Parse error")
            continue
        } // parse error
        if (baseUnit == null && finalUnit != null) {
            println("Conversion from ??? to ${finalUnit.plural} is impossible")
            continue
        } // baseUnit null
        if (baseUnit != null && finalUnit == null) {
            println("Conversion from ${baseUnit.plural} to ??? is impossible")
            continue
        } // finalUnit null
        if (baseUnit == null && finalUnit == null) {
            println("Conversion from ??? to ??? is impossible")
            continue
        } // both units null
        if (baseUnit != null && finalUnit != null) { // none null
            if (baseUnit.type != finalUnit.type) {
                println("Conversion from ${baseUnit.plural} to ${finalUnit.plural} is impossible")
                continue
            } // different types of units
            else if (baseNumber < 0 && baseUnit.type != "Temperature") {
                println("${baseUnit.type} shouldn't be negative")
                continue
            } // invalid length or weight
            else if (baseUnit.type == "Temperature") {
                if (baseUnit == Unit.CELSIUS && finalUnit == Unit.FAHRENHEIT) {
                    finalNumber = baseNumber * 9/5 + 32
                }
                else if (baseUnit == Unit.CELSIUS && finalUnit == Unit.KELVIN) {
                    finalNumber = baseNumber + 273.15
                }
                else if (baseUnit == Unit.FAHRENHEIT && finalUnit == Unit.CELSIUS) {
                    finalNumber = (baseNumber - 32) * 5/9
                }
                else if (baseUnit == Unit.FAHRENHEIT && finalUnit == Unit.KELVIN) {
                    finalNumber = (baseNumber + 459.67) * 5/9
                }
                else if (baseUnit == Unit.KELVIN && finalUnit == Unit.CELSIUS) {
                    finalNumber = baseNumber - 273.15
                }
                else if (baseUnit == Unit.KELVIN && finalUnit == Unit.FAHRENHEIT) {
                    finalNumber = baseNumber * 9/5 - 459.67
                }
                else {
                    finalNumber = baseNumber
                }
            } // convert temperature
            else {
                finalNumber = baseNumber * baseUnit.ratio / finalUnit.ratio
            } // convert length of weight
            val baseSuffix = if (baseNumber == 1.0) baseUnit.singular else baseUnit.plural // get base suffix
            val finalSuffix = if (finalNumber == 1.0) finalUnit.singular else finalUnit.plural // get final suffix
            println("$baseNumber $baseSuffix is $finalNumber $finalSuffix")
        }
    }
}

fun parseArgs(input: String): Triple<Double?, Unit?, Unit?> {
    val inputArray = input.toLowerCase().split(" ")
    val baseNumber = try { inputArray[0].toDouble()
    } catch (e: java.lang.NumberFormatException) { null }

    val baseUnit: Unit? = try {if (inputArray[1] == "degree" || inputArray[1] == "degrees") getUnit(inputArray[2]) else getUnit(inputArray[1])
    } catch (e: java.lang.IllegalArgumentException) { null }

    val finalUnit: Unit? = try {getUnit(inputArray.last())
    } catch (e: java.lang.IllegalArgumentException) { null }

    return Triple(baseNumber, baseUnit, finalUnit)
}

fun getUnit(input: String): Unit =
    when (input) {
        "m", "meter", "meters" -> Unit.METER
        "km", "kilometer", "kilometers" -> Unit.KILOMETER
        "cm", "centimeter", "centimeters" -> Unit.CENTIMETER
        "mm", "millimeter", "millimeters" -> Unit.MILLIMETER
        "mi", "mile", "miles" -> Unit.MILE
        "yd", "yard", "yards" -> Unit.YARD
        "ft", "foot", "feet" -> Unit.FOOT
        "in", "inch", "inches" -> Unit.INCH
        "g", "gram", "grams" -> Unit.GRAM
        "kg", "kilogram", "kilograms" -> Unit.KILOGRAM
        "mg", "milligram", "milligrams" -> Unit.MILLIGRAM
        "lb", "pound", "pounds" -> Unit.POUND
        "oz", "ounce", "ounces" -> Unit.OUNCE
        "degree celsius", "degrees celsius", "celsius", "dc", "c" -> Unit.CELSIUS
        "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df", "f" -> Unit.FAHRENHEIT
        "kelvin", "kelvins", "k" -> Unit.KELVIN
        else -> throw IllegalArgumentException("unknown unit: $input")
    }

enum class Unit(val singular: String, val plural: String, val ratio: Double, val type: String) {
    GRAM("gram", "grams", 1.0, "Weight"),
    KILOGRAM("kilogram", "kilograms", 1000.0, "Weight"),
    MILLIGRAM("milligram", "milligrams", 0.001, "Weight"),
    POUND("pound", "pounds", 453.592, "Weight"),
    OUNCE("ounce", "ounces", 28.3495, "Weight"),
    METER("meter", "meters", 1.0, "Length"),
    KILOMETER("kilometer", "kilometers", 1000.0, "Length"),
    CENTIMETER("centimeter", "centimeters", 0.01, "Length"),
    MILLIMETER("millimeter", "millimeters", 0.001, "Length"),
    MILE("mile", "miles", 1609.35, "Length"),
    YARD("yard", "yards", 0.9144, "Length"),
    FOOT("foot", "feet", 0.3048, "Length"),
    INCH("inch", "inches", 0.0254, "Length"),
    CELSIUS("degree Celsius", "degrees Celsius", 1.0, "Temperature"),
    KELVIN("Kelvin", "Kelvins", 1.0, "Temperature"),
    FAHRENHEIT("degree Fahrenheit", "degrees Fahrenheit", 1.0, "Temperature")
}