package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageX;

import Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed.ZeroPageIndexed_RMW;
import Atari2600.MOS6502.CPU;

/**
 * RMW instructions in Zero Page Indexed with X addressing mode.
 */
public class ZeroPageX_RMW extends ZeroPageIndexed_RMW {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1 = 0
     */
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu, cpu.getX());
    }
}
