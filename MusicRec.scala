// Import necessary libraries for file reading, user input, and utility functions
import scala.io.Source
import scala.io.StdIn.readLine
import scala.util.Try

// Main object containing the music recommendation logic
object MusicRec {
  def main(args: Array[String]): Unit = {
    // Main loop to repeatedly provide recommendations until the user exits
    while (true) {
      musicRecommendation() // Call the recommendation logic

      // Prompt the user to continue or exit
      val continuePrompt = prompt("Do you wish to continue? (Yes|No): ")
      if (continuePrompt.toLowerCase != "yes") {
        // Exit message and terminate the program
        println("\nThank you for using our music recommendation software. Goodbye!")
        sys.exit(0)
      }

      clearScreen() // Clear the console for a fresh start
    }
  }

  // Core function to handle the music recommendation process
  def musicRecommendation(): Unit = {
    val albumsData = loadAlbums("albums.txt") // Load album data from a file

    // Introduction message for the user
    println(
      """This is a music recommendation software.
Answer the following prompts to have acclaimed albums recommended.""")
    println()

    // Gather user preferences through prompts
    val genre = promptGenre()
    val decade = promptDecade()
    val energy = promptInt("How much energy are you looking for? (1 = low, 5 = high): ", 1, 5)
    val mood = promptInt("How is your mood? (1 = negative, 5 = positive): ", 1, 5)

    // Calculate similarity scores for each album based on user preferences
    val matches = albumsData.map { album =>
      (album, calculateSimilarity(album, genre, decade, energy, mood))
    }

    // Display the top 5 closest matches to the user
    println("\nHere are your 5 closest matches:\n")
    matches.sortBy(-_._2).take(5).foreach { case (album, score) =>
      println(s"${album.title} by ${album.artist}")
      if (score >= 15) println("Perfect Match!")
      else if (score >= 12) println("Close Match")
      else if (score >= 10) println("Rough Match")
      else println("This match is likely inaccurate, we lack closer matches in our database.")
      println()
    }
  }

  // Case class to represent an album with its attributes
  case class Album(title: String, artist: String, genre: String, decade: String, energy: Int, mood: Int)

  // Function to load album data from a file
  def loadAlbums(filename: String): List[Album] = {
    val source = Source.fromFile(filename) // Open the file
    val lines = try source.getLines().toList finally source.close() // Read all lines and close the file

    lines.flatMap(parseAlbum) // Parse each line into an Album object
  }

  // Function to parse a single line of album data into an Album object
  def parseAlbum(line: String): Option[Album] = {
    // Helper function to extract string fields from JSON-like data
    def extract(field: String): Option[String] = {
      val pattern = s""""$field"\\s*:\\s*"(.*?)"""".r
      pattern.findFirstMatchIn(line).map(_.group(1))
    }

    // Helper function to extract integer fields from JSON-like data
    def extractInt(field: String): Option[Int] = {
      val pattern = s""""$field"\\s*:\\s*(\\d+)""".r
      pattern.findFirstMatchIn(line).map(_.group(1).toInt)
    }

    // Extract all fields and construct an Album object if successful
    for {
      title  <- extract("title")
      artist <- extract("artist")
      genre  <- extract("genre")
      decade <- extract("decade")
      energy <- extractInt("energy")
      mood   <- extractInt("mood")
    } yield Album(title, artist, genre, decade, energy, mood)
  }

  // Function to prompt the user for input with a custom message
  def prompt(message: String): String = {
    print(message) // Display the prompt message
    readLine() // Read user input
  }

  // Function to prompt the user for a valid genre
  def promptGenre(): String = {
    val validGenres = List("Rock", "Hip-Hop", "Jazz", "Electronic", "Metal", "Pop") // List of valid genres
    println("The available genres are:")
    println(validGenres.mkString("|")) // Display valid genres

    var genre = ""
    while (genre.isEmpty) {
      val input = prompt("Enter your preferred genre (Case Sensitive!): ")
      if (validGenres.contains(input)) genre = input // Validate input
      else println("Invalid genre. Please choose one from the list.")
    }
    genre
  }

  // Function to prompt the user for a valid decade
  def promptDecade(): String = {
    println("Enter decades as the first year of them followed by 's")
    println("Examples: 1990's | 2000's | 2010's")
    var decade = ""
    while (decade.isEmpty) {
      val input = prompt("Enter your preferred decade: ")
      if (input.endsWith("'s") && Try(input.dropRight(2).toInt).isSuccess) {
        val year = input.dropRight(2).toInt
        if (year % 10 == 0 && year <= 2020) decade = input // Validate input
        else println("Invalid decade. Please check formatting and range.")
      } else println("Invalid format. Please follow examples like 1990's.")
    }
    decade
  }

  // Function to prompt the user for an integer within a range
  def promptInt(message: String, min: Int, max: Int): Int = {
    var value = -1
    while (value < min || value > max) {
      Try(prompt(message).toInt).toOption match {
        case Some(num) if (min to max).contains(num) => value = num // Validate input
        case _ => println(s"Invalid input. Please enter a number between $min and $max.")
      }
    }
    value
  }

  // Function to calculate the similarity score between an album and user preferences
  def calculateSimilarity(album: Album, genre: String, decade: String, energy: Int, mood: Int): Int = {
    var score = 0
    if (album.genre == genre) score += 8 // Higher score for matching genre
    if (album.decade == decade) score += 3 // Moderate score for matching decade
    score += (2 - Math.min(2, Math.abs(album.energy - energy))) // Score based on energy difference
    score += (2 - Math.min(2, Math.abs(album.mood - mood))) // Score based on mood difference
    score
  }

  // Function to clear the console screen
  def clearScreen(): Unit = {
    if (System.getProperty("os.name").toLowerCase.contains("win"))
      new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor() // Clear screen on Windows
    else print("\u001b[H\u001b[2J") // Clear screen on Unix-based systems
  }
}