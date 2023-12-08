package counter

import chisel3._
import chisel3.util.{log2Ceil}
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}

class Counter(CntMax: Int) extends Module {
    val io = IO(new Bundle{
        val cnt_o = Output(UInt(log2Ceil(CntMax).W))
    })

    val cnt_reg = RegInit(0.U(log2Ceil(CntMax).W))
    cnt_reg := Mux(cnt_reg === CntMax.U, 0.U, cnt_reg + 1.U)

    io.cnt_o := cnt_reg
}

/**
 * An object extending App to generate the Verilog code.
 */
object CounterMain extends App {
  (new ChiselStage).execute(Array("--target", "systemverilog", "--target-dir", "generated", "--split-verilog"), 
                            Seq(ChiselGeneratorAnnotation(() => new Counter(40)), 
                                FirtoolOption("--disable-all-randomization"),
                                FirtoolOption("-strip-debug-info"),
                                FirtoolOption("--lowering-options=disallowPackedArrays"),
                                FirtoolOption("--split-verilog")
                            ))
}