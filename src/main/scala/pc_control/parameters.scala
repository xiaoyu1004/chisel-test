package pc_control

import chisel3.util._

object parameters {
    var num_warp = 8
    def num_fetch = 2
    def icache_align = num_fetch * 4
}