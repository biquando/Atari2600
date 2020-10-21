package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions in absolute addressing mode.
 *
 * LDA/LDX/LDY, EOR/AND/ORA, ADC/SBC, CMP/CPX/CPY, BIT
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch low byte of address, increment PC
 *  3    PC     R  fetch high byte of address, increment PC
 *  4  address  R  read from effective address
 */
public class Absolute_Read extends Absolute {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        cpu.step();

        // Cycle 4
        return new byte[] { cpu.mem.read(data[0], data[1]) };
    }
}
