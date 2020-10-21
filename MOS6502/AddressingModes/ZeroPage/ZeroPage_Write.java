package Atari2600.MOS6502.AddressingModes.ZeroPage;

import Atari2600.MOS6502.CPU;

/**
 * Write instructions (STA, STX, STY, SAX)
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch address, increment PC
 *  3  address  W  write register to effective address
 */
public class ZeroPage_Write extends ZeroPage {
    /**
     * 0: addr0
     * 1: addr1 = 0
     */
    @Override
    public byte[] fetch(CPU cpu) {
        return new byte[] { super.fetch(cpu)[0], 0 };
    }
}
