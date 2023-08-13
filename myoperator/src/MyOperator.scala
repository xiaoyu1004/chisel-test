import chisel3._
// import chisel3.util._
// import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MyOperator extends Module {
    val io = IO(new Bundle {
        val in      = Input(UInt(4.W))
        val out_add = Output(UInt(4.W))
        val out_sub = Output(UInt(4.W))
        val out_mul = Output(UInt(4.W))
    } )

    io.out_add := 1.U + 4.U
    io.out_sub := 2.U - 1.U
    io.out_mul := 4.U * 2.U
}

// The Main object extending App to generate the Verilog code.
object MyOperator extends App {
    // Generate Verilog
    val verilog = (new chisel3.stage.ChiselStage).emitVerilog(new MyOperator(), args)
    // Print the generated Verilog code to the console
    // println(verilog)
}
