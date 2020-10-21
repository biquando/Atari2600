package Atari2600.MOS6502.AddressingModes.ZeroPage;

import Atari2600.MOS6502.CPU;

/**
 * Read-Modify-Write instructions (ASL, LSR, ROL, ROR, INC, DEC,
 *                                 SLO, SRE, RLA, RRA, ISB, DCP)
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch address, increment PC
 *  3  address  R  read from effective address
 *  4  address  W  write the value back to effective address,
 *                 and do the operation on it
 *  5  address  W  write the new value to effective address
 */
public class ZeroPage_RMW extends ZeroPage {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1 = 0
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = new byte[3];
        data[1] = super.fetch(cpu)[0];
        data[0] = cpu.mem.read(data[1], data[2]);
        data[2] = 0;
        cpu.step();

        // Cycle 4
        cpu.mem.write(data[0], data[1], data[2]);
        return data;
    }
}
