class SpecialHashMap {
    val hashMap: HashMap<String, Int> = HashMap()
    var sortKeyArr = hashMap.toSortedMap().values
    var iloc = sortKeyArr.toIntArray()
    var ploc = ploc(hashMap)

    operator fun set(s: String, value: Int) {
        hashMap[s] = value
        sortKeyArr = hashMap.toSortedMap().values
        iloc = sortKeyArr.toIntArray()
    }

    fun printSpecialHashMap(){
        if(hashMap.isEmpty()){
            println("SpecialhashMap is empty")
        }else{
            println("SpecialhashMap : $hashMap")
        }
    }
}

class ploc(val arr:  HashMap<String, Int> = HashMap()) {
    operator fun get(s: String): Any {
        var str: String = s
        var res_arr: HashMap<String, Int> = HashMap()
        for (i in s) {
            if (i !in "^,.0123456789<>=") {
                str = str.replace("$i", " ")
            }
        }
        str = str.replace("\\s+".toRegex(), ",")
        val n = str.count{ it == ','}

        for (i in arr.keys){
            if (i.count{ it == ','} == n) {
                val new_i = ((i.replace("(", " ")).replace(")", " ")).replace(" ", "")
                if (new_i.replace(",", "").contains("^\\d+\$".toRegex())) {
                    val con_arr = str.split(",").toList()
                    val key_arr = new_i.split(",").toList()
                    var counter = 0
                    for (j in con_arr.indices) {
                        if (con_arr[j].contains("^>=\\d+".toRegex())) {
                            val num = (con_arr[j].replace(">=", "")).toInt()
                            if (key_arr[j].toInt() >= num) counter += 1
                        } else if (con_arr[j].contains("^<=\\d+".toRegex())) {
                            val num = (con_arr[j].replace("<=", "")).toInt()
                            if (key_arr[j].toInt() <= num) print(arr[i])
                        } else if (con_arr[j].contains("^<\\d+".toRegex())){
                            val num = (con_arr[j].replace("<", "")).toInt()
                            if (key_arr[j].toInt() < num) counter += 1
                        } else if (con_arr[j].contains("^>\\d+".toRegex())){
                            val num = (con_arr[j].replace(">", "")).toInt()
                            if (key_arr[j].toInt() >= num) counter += 1
                        } else if (con_arr[j].contains("^<>\\d+".toRegex())) {
                            val num = (con_arr[j].replace("<>", "")).toInt()
                            if (key_arr[j].toInt() != num) counter += 1
                        }
                    }
                    if (counter == n + 1) {
                        res_arr[i] = (arr[i].toString()).toInt()
                    }
                }
            }
        }
        return res_arr
    }
}

fun main() {
    val map = SpecialHashMap()
    map["value1"] = 1
    map["value2"] = 2
    map["value3"] = 3
    map["1"] = 10
    map["2"] = 20
    map["3"] = 30
    map["1, 5"] = 100
    map["5, 5"] = 200
    map["10, 5"] = 300
    map.printSpecialHashMap()
    println(map.iloc[0])  // >>> 10
    println(map.iloc[2])  // >>> 300
    println(map.iloc[5])  // >>> 200
    println(map.iloc[8])  // >>> 3
    val map2 = SpecialHashMap()
    map2["value1"] = 1
    map2["value2"] = 2
    map2["value3"] = 3
    map2["1"] = 10
    map2["2"] = 20
    map2["3"] = 30
    map2["(1, 5)"] = 100
    map2["(5, 5)"] = 200
    map2["(10, 5)"] = 300
    map2["(1, 5, 3)"] = 400
    map2["(5, 5, 4)"] = 500
    map2["(10, 5, 5)"] = 600
    map2.printSpecialHashMap()
    println(map2.ploc[">=1"]) // >>> {1=10, 2=20, 3=30}
    println(map2.ploc["<3"]) // >>> {1=10, 2=20}

    println(map2.ploc[">0? >0"]) // >>> {(1, 5)=100, (5, 5)=200, (10, 5)=300}
    println(map2.ploc[">=10/    >0"]) // >>> {(10, 5)=300}

    println(map2.ploc["<5* >=5/ >=3"]) // >>> {(1, 5, 3)=400}
    println(map2.ploc["<>5* >=5/ >=3"]) // >>> {(1, 5, 3)=400}
}
