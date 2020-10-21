package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageX;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageIndexed_Read;
import Atari2600.MOS6502.CPU;

/**
 * Read instructions in Zero Page Indexed with X addressing mode.
 */
public class ZeroPageX_Read extends ZeroPageIndexed_Read {
    /**
     * 0: value
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getX());
    }
}
