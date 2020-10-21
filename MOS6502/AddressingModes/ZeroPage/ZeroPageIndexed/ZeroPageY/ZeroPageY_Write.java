package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageY;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageIndexed_Write;
import Atari2600.MOS6502.CPU;

/**
 * Write instructions in Zero Page Indexed with Y addressing mode.
 */
public class ZeroPageY_Write extends ZeroPageIndexed_Write {
    /**
     * 0: addr0
     * 1: addr1 = 0
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getY());
    }
}
