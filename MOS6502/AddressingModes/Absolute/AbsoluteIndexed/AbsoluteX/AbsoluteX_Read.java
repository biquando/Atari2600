package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteX;

import Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed.AbsoluteIndexed_Read;
import Atari2600.MOS6502.CPU;

/**
 * Read instructions in Absolute Indexed with X addressing mode.
 */
public class AbsoluteX_Read extends AbsoluteIndexed_Read {
    /**
     * 0: value
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getX());
    }
}
