package Atari2600.MOS6502.AddressingModes.ZeroPage.IndexedIndirect;

import Atari2600.MOS6502.CPU;

/**
 *  Write instructions (STA, SAX)
 *
 *  #    address   R/W description
 * --- ----------- --- ------------------------------------------
 *  1      PC       R  fetch opcode, increment PC
 *  2      PC       R  fetch pointer address, increment PC
 *  3    pointer    R  read from the address, add X to it
 *  4   pointer+X   R  fetch effective address low
 *  5  pointer+X+1  R  fetch effective address high
 *  6    address    W  write to effective address
 *
 *  Note: The effective address is always fetched from zero page,
 *        i.e. the zero page boundary crossing is not handled.
 */
public class IndexedIndirect_Write extends IndexedIndirect {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        return super.fetch(cpu);
    }
}
