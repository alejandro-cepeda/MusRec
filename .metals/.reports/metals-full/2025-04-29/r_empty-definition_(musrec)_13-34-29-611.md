error id: file://<WORKSPACE>/src/main/scala/MusicRec.scala:8
file://<WORKSPACE>/src/main/scala/MusicRec.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 166
uri: file://<WORKSPACE>/src/main/scala/MusicRec.scala
text:
```scala
import scala.io.Source
import scala.io.StdIn.readLine
import upickle.default._
import scala.util.Try
import scala.sys.process._

object MusicRecommendationApp {
  
  @@case class Album(title: String, artist: String, genre: String, decade: String, energy: Int, mood: Int)

  def main(args: Array[String]): Unit = {
    while (true) {
      musicRecommendation()

      val continuePrompt = promptString("Do you wish to continue? (Yes|No): ")
      if (continuePrompt.toLowerCase != "yes") {
        println()
        println("Thank you for using our music recommendation software. Goodbye!")
        return
      }

      clearScreen()
    }
  }

  def musicRecommendation(): Unit = {
    val albumsData: List[Album] = loadAlbums("albums.txt")

    println(
      """This is a music recommendation software.
Answer the following prompts to have acclaimed albums recommended."""
    )
    println()

    val genre = promptGenre()
    val decade = promptDecade()
    val energy = promptInt("How much energy are you looking for? (1 = low, 5 = high): ", 1, 5)
    val mood = promptInt("How is your mood? (1 = negative, 5 = positive): ", 1, 5)

    val matches = albumsData.map { album =>
      (album, calculateSimilarity(album, genre, decade, energy, mood))
    }

    println()
    println("Here are your 5 closest matches:")
    println()

    matches.sortBy(-_._2).take(5).foreach { case (album, score) =>
      println(s"${album.title} by ${album.artist}")

      if (score >= 15) println("Perfect Match!")
      else if (score >= 12) println("Close Match")
      else if (score >= 10) println("Rough Match")
      else println("This match is likely inaccurate, we lack closer matches in our database.")

      println()
    }
  }

  // --- Helper Functions ---

  def loadAlbums(filename: String): List[Album] = {
    val source = Source.fromFile(filename)
    val content = try source.mkString finally source.close()
    JSON.parseFull(content) match {
      case Some(data: List[Map[String, Any]] @unchecked) =>
        data.flatMap { albumMap =>
          for {
            title <- albumMap.get("title").collect { case s: String => s }
            artist <- albumMap.get("artist").collect { case s: String => s }
            genre <- albumMap.get("genre").collect { case s: String => s }
            decade <- albumMap.get("decade").collect { case s: String => s }
            energy <- albumMap.get("energy").collect { case d: Double => d.toInt }
            mood <- albumMap.get("mood").collect { case d: Double => d.toInt }
          } yield Album(title, artist, genre, decade, energy, mood)
        }
      case _ =>
        println("Error reading albums data.")
        List.empty
    }
  }

  def promptString(message: String): String = {
    print(message)
    readLine()
  }

  def promptGenre(): String = {
    val validGenres = List("Rock", "Hip-Hop", "Jazz", "Electronic", "Metal", "Pop")
    println("The available genres are:")
    println(validGenres.mkString("|"))
    var genre = ""
    while (genre.isEmpty) {
      val input = promptString("Enter your preferred genre (Case Insensitive!): ")
      validGenres.find(_.equalsIgnoreCase(input)) match {
        case Some(matchedGenre) => genre = matchedGenre
        case None => println("Invalid genre. Please choose one from the list.")
      }
    }
    genre
  }

  def promptDecade(): String = {
    println("Enter decades as the first year of them followed by 's")
    println("Examples: 1990's | 2000's | 2010's")
    var decade = ""
    while (decade.isEmpty) {
      val input = promptString("Enter your preferred decade: ")
      if (input.endsWith("'s") && Try(input.dropRight(2).toInt).isSuccess) {
        val year = input.dropRight(2).toInt
        if (year % 10 == 0 && year <= 2020) {
          decade = input
        } else {
          println("Invalid decade. Please check formatting and range.")
        }
      } else {
        println("Invalid format. Please follow examples like 1990's.")
      }
    }
    decade
  }

  def promptInt(message: String, min: Int, max: Int): Int = {
    var value = -1
    while (value < min || value > max) {
      Try(promptString(message).toInt).toOption match {
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

    if (album.energy == energy) score += 2
    else if (Math.abs(album.energy - energy) == 1) score += 1

    if (album.mood == mood) score += 2
    else if (Math.abs(album.mood - mood) == 1) score += 1

    score
  }

  def clearScreen(): Unit = {
    if (System.getProperty("os.name").toLowerCase.contains("win")) {
      new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
    } else {
      print("\u001b[H\u001b[2J")
      System.out.flush()
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 