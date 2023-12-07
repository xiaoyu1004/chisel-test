import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CounterTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "counter"
    it should "pass" in {
        test(new BlinkingLed) { c=>
            c.clock.setTimeout(0)
            var led_status = BigInt(-1)
            println("start the blinking led!")

            for (_ <- 0 until 100) {
                c.clock.step(5)
                val led_now = c.io.led.peek().litValue
                val s = if (led_now == 0) "o" else "*"

                if (led_status != led_now) {
                    System.out.println(s)
                    led_status = led_now
                }
            }

            println("\n end the blinking led!")
        }
    }
}