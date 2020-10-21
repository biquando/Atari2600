package Atari2600.MOS6502.AddressingModes;

import Atari2600.MOS6502.AddressingMode;
import Atari2600.MOS6502.CPU;

/**
 * The operand is implied, so it does not need to be specified.
 *
 * An instruction must be at least 2 bytes, so the next byte is
 * read and thrown away.
 *
 * This addressing mode is also used for accumulator (A) because
 * they both do the same thing.
 *
 *  #  address R/W description
 * --- ------- --- -----------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  read next instruction byte (and throw it away)
 */
public class Implied implements AddressingMode {
    /**
     * Returns an empty byte[].
     */
    @Override
    public byte[] fetch(CPU cpu) {
        // Cycle 2
        cpu.mem.read(cpu.getPCL(), cpu.getPCH());
        return new byte[0];
    }
}
