package Atari2600.MOS6502.Instructions.Miscellaneous;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * BRK
 *
 *  #  address R/W description
 * --- ------- --- -----------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  read next instruction byte (and throw it away),
 *                 increment PC
 *  3  $0100,S  W  push PCH on stack, decrement S
 *  4  $0100,S  W  push PCL on stack, decrement S
 *  5  $0100,S  W  push P on stack (with B flag set), decrement S
 *  6   $FFFE   R  fetch PCL
 *  7   $FFFF   R  fetch PCH
 */
public class BRK implements Instruction {
    /**
     * Force an interrupt.
     * Flags: B = 1, I = 1
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.incrementPC();
        cpu.step();

        // Cycle 3
        cpu.mem.write(cpu.getPCH(), cpu.getSP(), (byte)0x10);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.step();

        // Cycle 4
        cpu.mem.write(cpu.getPCL(), cpu.getSP(), (byte)0x10);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.step();

        // Cycle 5
        cpu.sr.setFlag(4, true);
        cpu.mem.write(cpu.sr.toByte(), cpu.getSP(), (byte)0x10);
        cpu.sr.setFlag(4, false);
        cpu.sr.setFlag(2, true);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.step();

        // Cycle 6
        cpu.setPCL(cpu.mem.read((byte)0xFE, (byte)0xFF));
        cpu.step();

        // Cycle 7
        cpu.setPCH(cpu.mem.read((byte)0xFF, (byte)0xFF));
        cpu.end();
    }

    @Override
    public String toString() {
        return "BRK";
    }
}
