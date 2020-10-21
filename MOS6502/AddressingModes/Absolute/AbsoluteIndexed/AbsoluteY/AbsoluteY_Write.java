package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteY;

import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteIndexed_Write;
import Atari2600.MOS6502.CPU;

/**
 * Write instructions in Absolute Indexed with Y addressing mode.
 */
public class AbsoluteY_Write extends AbsoluteIndexed_Write {
    /**
     * 0: addr0
     * 1: addr1
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getY());
    }
}
