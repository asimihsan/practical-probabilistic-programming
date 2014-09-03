import com.cra.figaro.language.Flip
import com.cra.figaro.library.compound.If
import com.cra.figaro.language.Select
import com.cra.figaro.algorithm.factored.VariableElimination

object HelloWorld {
  val sunnyToday = Flip(0.2)
  val greetingToday = If(sunnyToday,
      Select(0.6 -> "Hello world!", 0.4 -> "Howdy, universe!"),
      Select(0.2 -> "Hello world!", 0.8 -> "Oh no, not again"))
  val sunnyTomorrow = If(sunnyToday, Flip(0.8), Flip(0.05))
  val greetingTomorrow = If(sunnyTomorrow,
      Select(0.6 -> "Hello world!", 0.4 -> "Howdy, universe!"),
      Select(0.2 -> "Hello world!", 0.8 -> "Oh no, not again"))
  
  def predict() {
    val algorithm = VariableElimination(greetingToday)
    algorithm.start()
    val result = algorithm.probability(greetingToday, "Hello world!")
    println("Tomorrow's greeting is 'Hello world!' with probability " + result + ".")
    algorithm.kill()
  }
  
  def infer() {
    greetingToday.observe("Hello world!")
    val algorithm = VariableElimination(sunnyToday)
    algorithm.start()
    val result = algorithm.probability(sunnyToday, true)
    println("If today's greeting is 'Hello world!' then today's weather is sunny with probability " + result + ".")
    algorithm.kill()
    greetingToday.unobserve()
  }
  
  def learnAndPredict() {
    greetingToday.observe("Hello world!")
    val algorithm = VariableElimination(greetingTomorrow)
    algorithm.start()
    val result = algorithm.probability(greetingTomorrow, "Hello world!")
    println("If today's greeting is 'Hello world!' tomorrow's greeting will be 'Hello world!' with probability " + result + ".")
    algorithm.kill()
    greetingToday.unobserve()
  }
  
  def main(args : Array[String]) {
    predict()
    infer()
    learnAndPredict()
  }
}