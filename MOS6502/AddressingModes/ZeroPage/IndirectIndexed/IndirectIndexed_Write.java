package Atari2600.MOS6502.AddressingModes.ZeroPage.IndirectIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Write instructions (STA, SHA)
 *
 *  #    address   R/W description
 * --- ----------- --- ------------------------------------------
 *  1      PC       R  fetch opcode, increment PC
 *  2      PC       R  fetch pointer address, increment PC
 *  3    pointer    R  fetch effective address low
 *  4   pointer+1   R  fetch effective address high,
 *                     add Y to low byte of effective address
 *  5   address+Y*  R  read from effective address,
 *                     fix high byte of effective address
 *  6   address+Y   W  write to effective address
 *
 *  Notes: The effective address is always fetched from zero page,
 *         i.e. the zero page boundary crossing is not handled.
 *
 *  * The high byte of the effective address may be invalid
 *    at this time, i.e. it may be smaller by $100.
 */
public class IndirectIndexed_Write extends IndirectIndexed {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        return new byte[] { data[1], data[2] };
    }
}
