package Atari2600.MOS6502.AddressingModes.ZeroPage;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions (LDA, LDX, LDY, EOR, AND, ORA, ADC, SBC, CMP, BIT,
 *                    LAX, NOP)
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch address, increment PC
 *  3  address  R  read from effective address
 */
public class ZeroPage_Read extends ZeroPage {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte value = cpu.mem.read(super.fetch(cpu)[0], (byte)0);
        return new byte[] { value };
    }
}
