package pc_control

import chisel3._
import chisel3.util.{log2Ceil, DecoupledIO, Decoupled}
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}

import parameters._

class PCcontrolTop extends Module {
    val io = IO(new Bundle {
        val sel_pc      = Output(UInt(32.W))

        val branch_pc   = Input(UInt(32.W))
        val branch_wid  = Input(UInt(3.W))
    })

    val pcControl=VecInit(Seq.fill(num_warp)(Module(new PCcontrol()).io))
    pcControl.foreach {
        x=>{
            x.New_PC    := 0.U(32.W)
            x.PC_replay := true.B
            x.PC_src    := 0.U
            x.mask_i    := 0.U
        }
    }

    val idxReg = RegInit(0.U(3.W))
    idxReg := Mux(idxReg === 7.U(3.W), 0.U(3.W), idxReg + 1.U)

    pcControl(io.branch_wid).New_PC := io.branch_pc;

    io.sel_pc := pcControl(idxReg).New_PC;
}

/**
 * An object extending App to generate the Verilog code. systemverilog
 */
object PCcontrolTopMain extends App {
  (new ChiselStage).execute(Array("--target", "systemverilog", "--target-dir", "generated", "--split-verilog"), 
                            Seq(ChiselGeneratorAnnotation(() => new PCcontrolTop), 
                                FirtoolOption("--disable-all-randomization"),
                                FirtoolOption("-strip-debug-info"),
                                FirtoolOption("--lowering-options=disallowPackedArrays"),
                                FirtoolOption("--split-verilog")
                            ))
}