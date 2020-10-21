package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Write instructions (STA, STX, STY, SHA, SHX, SHY)
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
 *  5  address+I  W  write to effective address
 *
 *  Notes: I denotes either index register (X or Y).
 *
 *         * The high byte of the effective address may be invalid
 *           at this time, i.e. it may be smaller by $100. Because
 *           the processor cannot undo a write to an invalid
 *           address, it always reads from the address first.
 */
public class AbsoluteIndexed_Write extends AbsoluteIndexed {
    /**
     * 0: addr0
     * 1: addr1
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = super.fetch(cpu, offset);
        cpu.step();

        // Cycle 5
        return new byte[] { data[1], data[2] };
    }
}
