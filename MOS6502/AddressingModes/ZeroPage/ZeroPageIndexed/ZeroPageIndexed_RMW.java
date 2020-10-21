package Atari2600.MOS6502.AddressingModes.ZeroPage.ZeroPageIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Read-Modify-Write instructions (ASL, LSR, ROL, ROR, INC, DEC,
 *                                 SLO, SRE, RLA, RRA, ISB, DCP)
 *
 *  #   address  R/W description
 * --- --------- --- ---------------------------------------------
 *  1     PC      R  fetch opcode, increment PC
 *  2     PC      R  fetch address, increment PC
 *  3   address   R  read from address, add index register X to it
 *  4  address+X* R  read from effective address
 *  5  address+X* W  write the value back to effective address,
 *                   and do the operation on it
 *  6  address+X* W  write the new value to effective address
 *
 *  Note: * The high byte of the effective address is always zero,
 *          i.e. page boundary crossings are not handled.
 */
public class ZeroPageIndexed_RMW extends ZeroPageIndexed {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1 = 0
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = super.fetch(cpu, offset);
        data[0] = cpu.mem.read(data[1], data[2]);
        cpu.step();

        // Cycle 5
        cpu.mem.write(data[0], data[1], data[2]);
        return data;
    }
}
