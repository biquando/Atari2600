package Atari2600.MOS6502.AddressingModes;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.AddressingMode;

/**
 * All instructions with immediate addressing.
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch value, increment PC
 */
public class Immediate implements AddressingMode {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu) {
        // Cycle 2
        byte value = cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        cpu.incrementPC();
        return new byte[] { value };
    }
}
