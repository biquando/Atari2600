package Atari2600.MOS6502.AddressingModes.ZeroPage.IndexedIndirect;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions (LDA, ORA, EOR, AND, ADC, CMP, SBC, LAX)
 *
 *  #    address   R/W description
 * --- ----------- --- ------------------------------------------
 *  1      PC       R  fetch opcode, increment PC
 *  2      PC       R  fetch pointer address, increment PC
 *  3    pointer    R  read from the address, add X to it
 *  4   pointer+X   R  fetch effective address low
 *  5  pointer+X+1  R  fetch effective address high
 *  6    address    R  read from effective address
 *
 *  Note: The effective address is always fetched from zero page,
 *        i.e. the zero page boundary crossing is not handled.
 */
public class IndexedIndirect_Read extends IndexedIndirect {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        return new byte[] { cpu.mem.read(data[0], data[1]) };
    }
}
