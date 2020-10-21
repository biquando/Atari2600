package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteY;

import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteIndexed_Read;
import Atari2600.MOS6502.CPU;

/**
 * Read instructions in Absolute Indexed with Y addressing mode.
 */
public class AbsoluteY_Read extends AbsoluteIndexed_Read {
    /**
     * 0: value
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getY());
    }
}
