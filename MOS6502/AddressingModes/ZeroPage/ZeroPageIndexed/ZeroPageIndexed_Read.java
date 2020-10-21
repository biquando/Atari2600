package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Read instructions (LDA, LDX, LDY, EOR, AND, ORA, ADC, SBC, CMP, BIT,
 *                    LAX, NOP)
 *
 *  #   address  R/W description
 * --- --------- --- ------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch address, increment PC
 *  3   address   R  read from address, add index register to it
 *  4  address+I* R  read from effective address
 *
 *  Notes: I denotes either index register (X or Y).
 *
 *         * The high byte of the effective address is always zero,
 *           i.e. page boundary crossings are not handled.
 */
public class ZeroPageIndexed_Read extends ZeroPageIndexed {
    /**
     * 0: value
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = super.fetch(cpu, offset);
        data[0] = cpu.mem.read(data[1], data[2]);
        return new byte[] { data[0] };
    }
}
