package Atari2600.MOS6502.AddressingModes.Absolute;

import Atari2600.MOS6502.CPU;

/**
 * Read-Modify-Write instructions in absolute addressing mode.
 *
 * ASL, LSR, ROL, ROR, INC, DEC
 *
 *  #  address R/W description
 * --- ------- --- ------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  fetch low byte of address, increment PC
 *  3    PC     R  fetch high byte of address, increment PC
 *  4  address  R  read from effective address
 *  5  address  W  write the value back to effective address,
 *                 and do the operation on it
 *  6  address  W  write the new value to effective address
 */
public class Absolute_RMW extends Absolute {
    /**
     * 0: value
     * 1: addr0
     * 2: addr1
     */
    @Override
    public byte[] fetch(CPU cpu) {
        byte[] data = super.fetch(cpu);
        cpu.step();

        // Cycle 4
        byte value = cpu.mem.read(data[0], data[1]);
        cpu.step();

        // Cycle 5
        cpu.mem.write(value, data[0], data[1]);
        return new byte[] { value, data[0], data[1] };
    }
}
