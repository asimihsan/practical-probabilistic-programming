import com.cra.figaro.language.Flip
import com.cra.figaro.library.compound.CPD
import com.cra.figaro.language.Select
import com.cra.figaro.library.compound.If
import com.cra.figaro.library.atomic.continuous.Uniform
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.compound.CPD1
import com.cra.figaro.library.compound.CPD2

object Painting {
    def makeGraph(probRembrandt: Double): (Flip, CPD1[Boolean, String], CPD2[Boolean, String, String]) = {
	    val rembrandt = Flip(probRembrandt)
	    val subject = CPD(rembrandt,
	    	true ->  Select(0.9 -> "people", 0.1 -> "landscape"),
	    	false -> Select(0.5 -> "people", 0.5 -> "landscape"))
	    val size = CPD(rembrandt, subject,
	        (true, "people") ->     Select(0.3 -> "small", 0.5 -> "medium", 0.2 -> "large"),
	        (true, "landscape") ->  Select(0.2 -> "small", 0.4 -> "medium", 0.4 -> "large"),
	        (false, "people") ->    Select(0.3 -> "small", 0.4 -> "medium", 0.3 -> "large"),
	        (false, "landscape") -> Select(0.1 -> "small", 0.3 -> "medium", 0.6 -> "large"))
	    return (rembrandt, subject, size)
    }
    
    def infer(probRembrandt: Double) = {
      val (rembrandt, subject, size) = makeGraph(probRembrandt)
      subject.observe("landscape")
      size.observe("small")
      val algorithm = VariableElimination(rembrandt)
      algorithm.start()
      val result = algorithm.probability(rembrandt, true)
      println("for small landscape paintings with prior rembrandt uniform probability of " + probRembrandt + ", posterior uniform probability of rembrandt is " + result)
      algorithm.kill()
      size.unobserve()
      subject.unobserve()
    }
    
    def main(args: Array[String]) {
    	infer(0.2)
    	infer(0.4)
    }
}
