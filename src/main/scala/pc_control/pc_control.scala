package pc_control

import chisel3._
import chisel3.util.{log2Ceil, DecoupledIO, Decoupled}
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}

import parameters._

class PCcontrol() extends Module {
    val io=IO(new Bundle{
        val New_PC=Input(UInt(32.W))
        val PC_replay=Input(Bool())
        val PC_src=Input(UInt(2.W))
        val PC_next=Output(UInt(32.W))
        val mask_o=Output(UInt(num_fetch.W))
        val mask_i=Input(UInt(num_fetch.W)) //used for miss ack replay.
    })
    val pout=RegInit(0.U(32.W))
    val mask=Reg(UInt(num_fetch.W))

    def align(pc: UInt) = {
        val offset_mask = (icache_align - 1).U(32.W) // e.g. num_fetch = 4 (16B align) => offset_mask = "b1111".U(32.W)
        val pc_aligned = pc & (~offset_mask).asUInt
        val pc_mask = VecInit(Seq.fill(num_fetch)(false.B))
        (0 until num_fetch).foreach(i =>
            pc_mask(i) := Mux(pc_aligned + (i * 4).U >= pc, true.B, false.B) // e.g. num_fetch = 4, pc = 28 => pc_aligned = 16, pc_mask = "b1000"
        )
        (pc_aligned, pc_mask.asUInt)
    }

    //val warpID=RegInit(ID.U(1.W))//PC_src=0:PC+4  PC_src=1:new PC PC_src=2:PC
    when(io.PC_replay){
        pout:=pout
        mask:=mask
    }.elsewhen(io.PC_src===2.U){
        pout:=pout+ (num_fetch.U<<2)
        mask:=VecInit(Seq.fill(num_fetch)(true.B)).asUInt
    }.elsewhen(io.PC_src===1.U){
        //pout:=Cat(io.New_PC(31,log2Ceil(num_fetch)+2),0.U((log2Ceil(num_fetch)+2).W))
        val pc_req_tmp = align(io.New_PC)
        pout:=pc_req_tmp._1
        mask:=pc_req_tmp._2
    }.elsewhen(io.PC_src===3.U){ // replay
        pout:=io.New_PC
        mask:=io.mask_i
    }.otherwise{
        pout:=pout
        mask:=mask
    }
    io.PC_next:=pout
    io.mask_o:=mask
}

/**
 * An object extending App to generate the Verilog code. systemverilog
 */
object PCcontrolMain extends App {
  (new ChiselStage).execute(Array("--target", "systemverilog", "--target-dir", "generated", "--split-verilog"), 
                            Seq(ChiselGeneratorAnnotation(() => new PCcontrol), 
                                FirtoolOption("--disable-all-randomization"),
                                FirtoolOption("-strip-debug-info"),
                                FirtoolOption("--lowering-options=disallowPackedArrays"),
                                FirtoolOption("--split-verilog")
                            ))
}