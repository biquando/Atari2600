package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions (LDA, LDX, LDY, EOR, AND, ORA, ADC, SBC, CMP, BIT,
 *                    LAX, LAE, SHS, NOP)
 *
 *  #   address  R/W description
 * --- --------- --- ------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch low byte of address, increment PC
 *  3     PC      R  fetch high byte of address,
 *                   add index register to low address byte,
 *                   increment PC
 *  4  address+I* R  read from effective address,
 *                   fix the high byte of effective address
 *  5+ address+I  R  re-read from effective address
 *
 *  Notes: I denotes either index register (X or Y).
 *
 *         * The high byte of the effective address may be invalid
 *         at this time, i.e. it may be smaller by $100.
 *
 *         + This cycle will be executed only if the effective address
 *         was invalid during cycle #4, i.e. page boundary was crossed.
 */
public class AbsoluteIndexed_Read extends AbsoluteIndexed {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = super.fetch(cpu, offset);
        if (data[3] != 0) {
            cpu.step();
            // Cycle 5?
            data[0] = cpu.mem.read(data[1], data[2]);
        }
        return new byte[] { data[0] };
    }
}
