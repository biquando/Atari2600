package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteX;

import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteIndexed_RMW;
import Atari2600.MOS6502.CPU;

/**
 * RMW instructions in Absolute Indexed with X addressing mode.
 */
public class AbsoluteX_RMW extends AbsoluteIndexed_RMW {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getX());
    }
}
