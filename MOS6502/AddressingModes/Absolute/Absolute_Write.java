package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.CPU;

/**
 * Write instructions in absolute addressing mode.
 *
 * STA/STX/STY
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch low byte of address, increment PC
 *  3    PC     R  fetch high byte of address, increment PC
 *  4  address  W  write register to effective address
 */
public class Absolute_Write extends Absolute {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        cpu.step();

        // Cycle 4
        return data;
    }
}
