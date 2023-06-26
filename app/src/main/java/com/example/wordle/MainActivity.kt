package com.example.wordle

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import android.util.Log
import kotlin.random.Random

/**
 * https://www.nytimes.com/games/wordle/index.html
 * https://wordplay.com/
 * Author: SUVARNA SAHU
 * Description:We have tried to implement the wordle game, which reads a list of word from the file and
 * prompts the user to guess the 5-letter word and also checks if user enters a legitimate word
 * from the file.The color-coded guess shows the correct letters in green,
 * the correct letters in the wrong spot in yellow, and incorrect letters in Grey.
 * The program allows the user to make up to 6 guesses and shows the selected word
 * If the user correctly guesses the word, the program congratulates the user and exits.
 * We have used several functions to achieve this functionality of the game.
 **/

class MainActivity : AppCompatActivity() {

    var row = 1;
    var col = 1;
    var word = "";
    var WordleWords: List<String> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word = selectWord()
        findViewById<TextView>(R.id.message).text = "The word is $word"
        //findViewById<TextView>(R.id.message).text = "Lets Play Wordle"
    }

    //Returns a random word from the file wordle.txt
    //The readLines() function is called on the BufferedReader instance, which reads all the lines from the "wordle" file
    // and returns them as a list of strings.
    //The function then generates a random number within the range of the size of the wordList list using the Random.nextInt() function.
    fun selectWord():String {
        val wordList = BufferedReader(
            InputStreamReader(
                resources.openRawResource(
                    resources.getIdentifier("wordle", "raw", "com.example.wordle")
                )
            )
        ).readLines()
        val random= Random.nextInt(wordList.size)
        WordleWords=wordList;
        return wordList[random]
    }

    //legitGuess takes a string guess as input and returns a boolean value.
    // The function checks if the input string guess is present in the WordleWords collection or not.
    // If the string is present in the collection, the function returns true, indicating that the guess is legitimate, otherwise it returns false.
    fun legitGuess(guess: String): Boolean=WordleWords.contains(guess)

    // function countCharacterOccurrences takes a string str as input and returns a map that contains each character in the input string as a key,
    // and the number of occurrences of that character in the string as the corresponding value.
    //It returns a key value pair for each word with a number
    fun countCharacterOccurrences(str: String): Map<Char, Int> {
        val charCountMap = mutableMapOf<Char, Int>()
        for (char in str) {
            charCountMap[char] = charCountMap.getOrDefault(char, 0) + 1
        }
        return charCountMap
    }

    //function disableButtons will disable the backspacebutton and enter button, this will be used in the enterhandler function when user wins the game
    fun disableButtons() {
        findViewById<TextView>(R.id.buttonBack).isEnabled = false
        findViewById<TextView>(R.id.buttonEnter).isEnabled = false
    }

    //backspaceHandler is an event handler function that is called when a view is clicked.
    // The function takes a view object v as its input parameter.
    //The function first checks if the current column (col) is greater than 1. If it is, it decrements the value of col by 1.
    // The purpose of this is to move the cursor back one position to the left within a row of text views.
    //The function then retrieves the TextView object corresponding to the row and column using the resources.getIdentifier method.
    //textView$row$col naming convention is used to generate the ids of a grid of TextView objects

    fun backspaceHandler(v: View) {
        col = if (col > 1) col - 1 else col
        findViewById<TextView>(resources.getIdentifier("textView$row$col", "id", getPackageName())).text = ""
    }


    //Function letterHandler is an event handler function that is called when a view is clicked.
    // The function takes a view object viewable as its input parameter.
    //checks if the current column (col) is less than or equal to 5
    // If it is, the function retrieves the text of the button that was clicked and stores it in a variable called buttonText.
    // The purpose of this is to get the text of the button that the user clicked and use it as input to populate the TextView object
    // at the current cursor position.
    //The function then increments the value of col by 1, effectively moving the cursor to the next column in the grid.

    fun letterHandler(viewable: View) {
        when { col <= 5 -> {
            val buttons = viewable as Button
            val buttonText = buttons.text.toString()
            findViewById<TextView>(resources.getIdentifier("textView$row$col", "id", getPackageName())).text = buttonText
            col++
        }
        }
    }
    //function gameState takes two strings as input: guess and word, and returns a string representing the current game state
    // based on the user's guess.
    // The function first initializes an empty string array res to store the results of the game state.
    //It then creates a mutable map countMap by calling the countCharacterOccurrences function on the word parameter.
    // This map stores the count of each character in the word.
    //The function then loops through each character in the guess string using a for loop.
    // For each character, it checks whether it is in the countMap and if the character is in the same position as it is in the word string.
    // If both conditions are true, it changes the background color of the corresponding text view and button to green,
    // indicating that the character is correct and in the correct position. It also decrements the count of that character in the countMap by 1.
    //If the character is not in the same position as in the word string or is not present in the countMap,
    // it changes the background color of the corresponding text view and button to light gray, indicating that the character is incorrect
    //Finally, the function returns the empty string array res joined into a single string using joinToString("").

    fun gameState(guess: String, word: String): String {
        var res: Array<String> = arrayOf()
        val countMap = countCharacterOccurrences(word).toMutableMap()
        for (i in guess.indices) {
            val count = countMap.getOrDefault(guess[i], 0)
            if (count > 0 && word[i] == guess[i]) {
                findViewById<TextView>(resources.getIdentifier("textView$row${i+1}", "id", getPackageName())).background = getDrawable(R.color.green)
                findViewById<Button>(resources.getIdentifier("button${guess[i].uppercase()}", "id", getPackageName())).setBackgroundColor(Color.GREEN)
                countMap[guess[i]] = count - 1

            } else {
                findViewById<TextView>(resources.getIdentifier("textView$row${i+1}", "id", getPackageName())).background = getDrawable(R.color.gray)
                findViewById<Button>(resources.getIdentifier("button${guess[i].uppercase()}", "id", getPackageName())).setBackgroundColor(Color.GRAY)
            }
        }
        for (i in guess.indices) {
            val count = countMap.getOrDefault(guess[i], 0)
            if (count > 0) {

                findViewById<TextView>(resources.getIdentifier("textView$row${i+1}", "id", getPackageName())).background = getDrawable(R.color.yellow)
                findViewById<Button>(resources.getIdentifier("button${guess[i].uppercase()}", "id", getPackageName())).setBackgroundColor(Color.YELLOW)
                countMap[guess[i]] = count - 1
            }
        }
        return res.joinToString("")
    }
    //function is an event handler for when the "enter" button is clicked in a word-guessing game.
    // It first checks if the user has entered a complete 5-letter word by verifying that the col variable is equal to 6.
    // If the user has not entered a complete word, it displays a message telling them to do so.
    //If the user has entered a complete word, the function extracts the guessed word by iterating over the TextViews that display the user's input,
    // concatenating their text values, and converting the resulting string to lowercase.
    // It then checks if the guessed word is a legitimate word by calling the legitGuess function.
    //If the guessed word is not a legitimate word, the function displays a message informing the user that their guess is not a word.
    // If the guessed word is the correct word, the function displays a message informing the user that they have won and disables the input buttons.
    // If the guessed word is not the correct word but the user has not used up all their guesses
    //the function displays a message informing the user that their guess is a word, updates the game state by calling the gameState function,
    // increments the row variable to move on to the next row of input TextViews, and sets col to 1 to reset the input position
    //Finally, if the user has used up all their guesses
    // the function displays a message revealing the correct word and disables the input buttons

    fun enterHandler(v: View) {
        if (col == 6) {
            val guessedWord = (1..5).joinToString("") {
                findViewById<TextView>(
                    resources.getIdentifier(
                        "textView$row$it",
                        "id",
                        packageName
                    )
                ).text.toString().toLowerCase()
            }
            Log.d("GuessWord", guessedWord)
            if (!legitGuess(guessedWord)) {
                findViewById<TextView>(R.id.message).text = "Not a word"
                //gameState(guessedWord, Wordleword)
            } else if (guessedWord == word) {
                gameState(guessedWord,word)
                findViewById<TextView>(R.id.message).text = "You win!"
                disableButtons()
            } else if (row == 6) {
                findViewById<TextView>(R.id.message).text = "The word was $word"
                gameState(guessedWord,word)
                disableButtons()
            } else if (legitGuess(guessedWord)) {
                findViewById<TextView>(R.id.message).text = "It's a word"
                gameState(guessedWord, word)
                row++
                col = 1
            }
        } else {
            findViewById<TextView>(R.id.message).text = "Enter a Complete Word"
        }

    }

}




