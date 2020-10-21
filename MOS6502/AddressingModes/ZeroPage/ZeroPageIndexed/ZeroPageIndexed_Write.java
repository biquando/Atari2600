package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Write instructions (STA, STX, STY, SAX)
 *
 *  #   address  R/W description
 * --- --------- --- -------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch address, increment PC
 *  3   address   R  read from address, add index register to it
 *  4  address+I* W  write to effective address
 *
 *  Notes: I denotes either index register (X or Y).
 *
 *         * The high byte of the effective address is always zero,
 *           i.e. page boundary crossings are not handled.
 */
public class ZeroPageIndexed_Write extends ZeroPageIndexed {
    /**
     * 0: addr0
     * 1: addr1 = 0
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        return new byte[] { super.fetch(cpu, offset)[1], 0 };
    }
}
