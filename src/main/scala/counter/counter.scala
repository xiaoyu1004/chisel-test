package counter

import chisel3._
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}

/**
 * The blinking LED component.
 */

class BlinkingLedIO extends Bundle {
    val led = Output(UInt(1.W))
}

class BlinkingLed extends Module {
    val io = IO(new BlinkingLedIO)

    val CNT_MAX = 10.U

    val cnt_reg = RegInit(0.U(32.W))
    val blk_reg = RegInit(0.U(1.W))

    // cnt_reg
    when (cnt_reg === CNT_MAX) {
        cnt_reg := 0.U(32.W)
    }.otherwise {
        cnt_reg := cnt_reg + 1.U(1.W)
    }

    // blk_reg
    when (cnt_reg === CNT_MAX) {
        blk_reg := ~blk_reg
    }.otherwise {
        blk_reg := blk_reg
    }

    io.led := blk_reg
}

/**
 * An object extending App to generate the Verilog code.
 */

object Main extends App {
  (new ChiselStage).execute(Array("--target", "verilog", "--target-dir", "generated"), Seq(ChiselGeneratorAnnotation(() => new BlinkingLed), FirtoolOption("--disable-all-randomization")))

// (new chisel3.stage.ChiselStage).emitVerilog(new BlinkingLed, Array("--target-dir", "generated"))

// val pretty = Array(
//   "--emission-options", "disableMemRandomization,disableRegisterRandomization"
// )
//     val verilogString = chisel3.getVerilogString(new BlinkingLed, pretty)
//     println(verilogString)
}

// import chisel3._
// import chisel3.util.Counter
// import circt.stage.ChiselStage

// class Blinky(freq: Int, startOn: Boolean = false) extends Module {
//   val io = IO(new Bundle {
//     val led0 = Output(Bool())
//   })
//   // Blink LED every second using Chisel built-in util.Counter
//   val led = RegInit(startOn.B)
//   val (_, counterWrap) = Counter(true.B, freq / 2)
//   when(counterWrap) {
//     led := ~led
//   }
//   io.led0 := led
// }

// object Main extends App {
//   // These lines generate the Verilog output
//   println(
//     ChiselStage.emitSystemVerilog(
//       new Blinky(1000),
//       firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
//     )
//   )
// }