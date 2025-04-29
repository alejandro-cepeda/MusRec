import scala.io.Source
import scala.io.StdIn.readLine
import scala.util.Try

object MusicRec {
  def main(args: Array[String]): Unit = {
    while (true) {
      musicRecommendation()

      val continuePrompt = prompt("Do you wish to continue? (Yes|No): ")
      if (continuePrompt.toLowerCase != "yes") {
        println("\nThank you for using our music recommendation software. Goodbye!")
        sys.exit(0)
      }

      clearScreen()
    }
  }

  def musicRecommendation(): Unit = {
    val albumsData = loadAlbums("albums.txt")

    println(
      """This is a music recommendation software.
Answer the following prompts to have acclaimed albums recommended.""")
    println()

    val genre = promptGenre()
    val decade = promptDecade()
    val energy = promptInt("How much energy are you looking for? (1 = low, 5 = high): ", 1, 5)
    val mood = promptInt("How is your mood? (1 = negative, 5 = positive): ", 1, 5)

    val matches = albumsData.map { album =>
      (album, calculateSimilarity(album, genre, decade, energy, mood))
    }

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

  case class Album(title: String, artist: String, genre: String, decade: String, energy: Int, mood: Int)

  def loadAlbums(filename: String): List[Album] = {
    val source = Source.fromFile(filename)
    val lines = try source.getLines().toList finally source.close()

    lines.flatMap(parseAlbum)
  }

  def parseAlbum(line: String): Option[Album] = {
    // very basic and brittle JSON parsing - expects exact format!
    def extract(field: String): Option[String] = {
      val pattern = s""""$field"\\s*:\\s*"(.*?)"""".r
      pattern.findFirstMatchIn(line).map(_.group(1))
    }

    def extractInt(field: String): Option[Int] = {
      val pattern = s""""$field"\\s*:\\s*(\\d+)""".r
      pattern.findFirstMatchIn(line).map(_.group(1).toInt)
    }

    for {
      title  <- extract("title")
      artist <- extract("artist")
      genre  <- extract("genre")
      decade <- extract("decade")
      energy <- extractInt("energy")
      mood   <- extractInt("mood")
    } yield Album(title, artist, genre, decade, energy, mood)
  }

  def prompt(message: String): String = {
    print(message)
    readLine()
  }

  def promptGenre(): String = {
    val validGenres = List("Rock", "Hip-Hop", "Jazz", "Electronic", "Metal", "Pop")
    println("The available genres are:")
    println(validGenres.mkString("|"))

    var genre = ""
    while (genre.isEmpty) {
      val input = prompt("Enter your preferred genre (Case Sensitive!): ")
      if (validGenres.contains(input)) genre = input
      else println("Invalid genre. Please choose one from the list.")
    }
    genre
  }

  def promptDecade(): String = {
    println("Enter decades as the first year of them followed by 's")
    println("Examples: 1990's | 2000's | 2010's")
    var decade = ""
    while (decade.isEmpty) {
      val input = prompt("Enter your preferred decade: ")
      if (input.endsWith("'s") && Try(input.dropRight(2).toInt).isSuccess) {
        val year = input.dropRight(2).toInt
        if (year % 10 == 0 && year <= 2020) decade = input
        else println("Invalid decade. Please check formatting and range.")
      } else println("Invalid format. Please follow examples like 1990's.")
    }
    decade
  }

  def promptInt(message: String, min: Int, max: Int): Int = {
    var value = -1
    while (value < min || value > max) {
      Try(prompt(message).toInt).toOption match {
        case Some(num) if (min to max).contains(num) => value = num
        case _ => println(s"Invalid input. Please enter a number between $min and $max.")
      }
    }
    value
  }

  def calculateSimilarity(album: Album, genre: String, decade: String, energy: Int, mood: Int): Int = {
    var score = 0
    if (album.genre == genre) score += 8
    if (album.decade == decade) score += 3
    score += (2 - Math.min(2, Math.abs(album.energy - energy)))
    score += (2 - Math.min(2, Math.abs(album.mood - mood)))
    score
  }

  def clearScreen(): Unit = {
    if (System.getProperty("os.name").toLowerCase.contains("win"))
      new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
    else print("\u001b[H\u001b[2J")
  }
}
