package Atari2600.MOS6502.AddressingModes.Absolute.AbsoluteIndexed;

import Atari2600.MOS6502.CPU;

/**
 * Read-Modify-Write instructions (ASL, LSR, ROL, ROR, INC, DEC,
 *                                 SLO, SRE, RLA, RRA, ISB, DCP)
 *
 *  #   address  R/W description
 * --- --------- --- ------------------------------------------
 *  1    PC       R  fetch opcode, increment PC
 *  2    PC       R  fetch low byte of address, increment PC
 *  3    PC       R  fetch high byte of address,
 *                   add index register X to low address byte,
 *                   increment PC
 *  4  address+X* R  read from effective address,
 *                   fix the high byte of effective address
 *  5  address+X  R  re-read from effective address
 *  6  address+X  W  write the value back to effective address,
 *                   and do the operation on it
 *  7  address+X  W  write the new value to effective address
 *
 *  Notes: * The high byte of the effective address may be invalid
 *           at this time, i.e. it may be smaller by $100.
 */
public class AbsoluteIndexed_RMW extends AbsoluteIndexed {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1
     */
    @Override
    public byte[] fetch(CPU cpu, byte offset) {
        byte[] data = super.fetch(cpu, offset);
        cpu.step();

        // Cycle 5
        data[0] = cpu.mem.read(data[1], data[2]);
        cpu.step();

        // Cycle 6
        cpu.mem.write(data[0], data[1], data[2]);
        return new byte[] { data[0], data[1], data[2] };
    }
}
