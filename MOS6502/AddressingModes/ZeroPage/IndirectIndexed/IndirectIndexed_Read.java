package Atari2600.MOS6502.AddressingModes.ZeroPage.IndirectIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions (LDA, EOR, AND, ORA, ADC, SBC, CMP)
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
 *  6+  address+Y   R  read from effective address
 *
 *  Notes: The effective address is always fetched from zero page,
 *         i.e. the zero page boundary crossing is not handled.
 *
 *  * The high byte of the effective address may be invalid
 *    at this time, i.e. it may be smaller by $100.
 *
 *  + This cycle will be executed only if the effective address
 *    was invalid during cycle #5, i.e. page boundary was crossed.
 */
public class IndirectIndexed_Read extends IndirectIndexed {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        if (data[3] != 0) {
            cpu.step();
            // Cycle 6?
            data[0] = cpu.mem.read(data[1], data[2]);
        }
        return new byte[] { data[0] };
    }
}
