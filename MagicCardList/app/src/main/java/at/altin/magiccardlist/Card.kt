package at.altin.magiccardlist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Data class for the card and its underlying data
 * @author Altin
 * @version 1.0
 * @since 2023-03-12
 */
data class Card(
    var name: String,
    var type: String,
    var subtypes: Subtype,
    var colors: List<Color>,
    var sep:String = "__________ \n"
)

data class Subtype(
    var rarity: String,
    var set: String,
    var setName: String,
    var artist: String,
    var number: String,
    var layout: String,
    var multiverseId: String
)

data class Color(
    var color: String
)

fun toString(card: Card): String {
    return "Name: ${card.name} \n " +
            "Type: ${card.type} \n" +
            "Rarity: ${card.subtypes.rarity} \n" +
            "Set: ${card.subtypes.set} \n" +
            "SetName: ${card.subtypes.setName} \n" +
            "Artist: ${card.subtypes.artist} \n" +
            "Number: ${card.subtypes.number} \n" +
            "Layout: ${card.subtypes.layout} \n" +
            "MultiverseId: ${card.subtypes.multiverseId} \n" +
            "at.altin.magiccardlist.Color: ${card.colors[0].color} \n ${card.sep}"
}

/**
 * Converts a list of cards to a string and sorts it by name
 * @param cardList the list of cards to convert
 * @return a string representation of the list of cards
 */
suspend fun sortedListToString(cardList: MutableList<Card>): String = withContext(Dispatchers.Default) {
    var result = ""
    for (card in cardList.sortedBy { it.name }) {
        result += toString(card)
    }
    return@withContext result
}

/**
 * Parses the JSON text and returns a list of cards
 * @param jsonText the JSON text to parse
 * @return a list of cards
 */
suspend fun parseJsonAddToCardList(jsonText:String): MutableList<Card> = withContext(Dispatchers.Default) {
    val cardList: MutableList<Card> = mutableListOf()
    val mainObject = JSONObject(jsonText)
    val cardsArray = mainObject.getJSONArray("cards")

    for(i in 0 until cardsArray.length()){
        val card = cardsArray.getJSONObject(i)
        for(k in 0 until (card.optJSONArray("colors")?.length() ?: 0)) {
            cardList.add(
                Card(
                    card.optString("name"),
                    card.optString("type"),
                    Subtype(
                        card.optString("rarity"),
                        card.optString("set"),
                        card.optString("setName"),
                        card.optString("artist"),
                        card.optString("number"),
                        card.optString("layout"),
                        card.optString("multiverseid")
                    ),
                    listOf(
                        Color(
                            card.optJSONArray("colors")?.getString(k) ?: ""
                        )
                    )
                )
            )
        }
    }
    return@withContext cardList
}
