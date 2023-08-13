import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MyOperatorTester(c: MyOperators) extends PeekPokeTester(c) {
  expect(c.io.out_add, 5)
  expect(c.io.out_sub, 1)
  expect(c.io.out_mul, 8)
}
assert(Driver(() => new MyOperators) {c => new MyOperatorsTester(c)})
println("SUCCESS!!")
