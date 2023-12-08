import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import counter.{Counter}

class CounterTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "counter" it should "pass" in {
        test(new Counter(40)).withAnnotations(Seq(WriteVcdAnnotation)) { c=>
            c.clock.setTimeout(0)
            c.clock.step(100)
        }
    }
}