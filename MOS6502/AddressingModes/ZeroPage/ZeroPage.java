package Atari2600.MOS6502.AddressingModes.ZeroPage;

import Atari2600.MOS6502.AddressingMode;
import Atari2600.MOS6502.CPU;

/**
 * A single byte specifies an address in the first page of memory ($00xx),
 * also known as the zero page, and the byte at that address is used to
 * perform the computation.
 */
public class ZeroPage implements AddressingMode {
    /**
     * 0: addr0
     */
    @Override
    public byte[] fetch(CPU cpu) {
        // Cycle 2
        byte addr0 = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        cpu.step();

        // Cycle 3
        return new byte[] { addr0 };
    }
}
