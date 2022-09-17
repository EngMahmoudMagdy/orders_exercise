import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter


fun main() {
    val inputFileName = readLine()
    val inputFile = File(inputFileName ?: "")

    val listOfOrders = getListOfOrdersFromFile(inputFile)
    val mapOfItemsOccurrence = HashMap<String, Int>()
    val mapOfPopularItems = HashMap<Pair<String, String>, Int>()
    listOfOrders.sortBy { it.product }
    listOfOrders.forEach {
        //check how many orders for each product with specific brand
        mapOfPopularItems[Pair(it.product, it.brand)] = mapOfPopularItems[Pair(it.product, it.brand)]?.plus(1) ?: 1
        //get total quantity of each product
        mapOfItemsOccurrence[it.product] = mapOfItemsOccurrence[it.product]?.plus(it.quantity) ?: it.quantity
    }
    generateOutputFile0(mapOfItemsOccurrence, inputFileName, listOfOrders.size)

    val listOfMaxPairs = getPopularListFromItems(mapOfPopularItems)
    println(listOfMaxPairs)
    generateOutputFile1(listOfMaxPairs, inputFileName)
}

fun generateOutputFile0(
    mapOfItemsOccurrence: java.util.HashMap<String, Int>,
    inputFileName: String?,
    totalOrders: Int
) {

    //create and open 0_input_file_name.csv file to save results
    val output0 = BufferedWriter(FileWriter("0_$inputFileName", true))
    //traverse through the items map to write product name and the average quantity of the product purchased per order
    mapOfItemsOccurrence.forEach {
        output0.appendln("${it.key},${it.value.toFloat() / totalOrders}")
    }
    //close the file
    output0.close()
}

fun generateOutputFile1(
    listOfMaxPairs: ArrayList<Pair<String, String>>,
    inputFileName: String?
) {
    //create and open 1_input_file_name.csv file to save results
    val output1 = BufferedWriter(FileWriter("1_$inputFileName", true))
    //traverse through the items map to write product name and the most popular Brand for that product
    listOfMaxPairs.forEach {
        output1.appendln("${it.first},${it.second}")
    }
    //close the file
    output1.close()
}

fun getListOfOrdersFromFile(file: File): ArrayList<Order> {
    val list = ArrayList<Order>()

    file.forEachLine {
        val values = it.split(',')
        list.add(Order(values[0], values[1], values[2], values[3].toInt(), values[4]))
    }
    return list
}

fun getPopularListFromItems(mapOfPopularItems: HashMap<Pair<String, String>, Int>): ArrayList<Pair<String, String>> {
    //sorting the items in map for its number of orders and alphabets of its name
    val popularItems = mapOfPopularItems.toList().sortedWith(compareBy({ it.first.first }, { -it.second }))

    var lastProduct = ""
    val listOfMaxPairs = ArrayList<Pair<String, String>>()
    //collect the pairs to be generated in a file
    popularItems.forEach {
        if (lastProduct != it.first.first) {
            lastProduct = it.first.first
            listOfMaxPairs.add(it.first)
        }
    }
    return listOfMaxPairs
}

data class Order(
    val id: String,
    val area: String,
    val product: String,
    val quantity: Int,
    val brand: String
)