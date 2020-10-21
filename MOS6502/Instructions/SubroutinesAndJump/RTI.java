package Atari2600.MOS6502.Instructions.SubroutinesAndJump;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * RTI
 *
 *  #  address R/W description
 * --- ------- --- -----------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  read next instruction byte (and throw it away)
 *  3  $0100,S  R  increment S
 *  4  $0100,S  R  pull P from stack, increment S
 *  5  $0100,S  R  pull PCL from stack, increment S
 *  6  $0100,S  R  pull PCH from stack
 */
public class RTI implements Instruction {
    /**
     * Return from an interrupt.
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.step();

        // Cycle 3
        cpu.setSP((byte)(cpu.getSP() + 1));
        cpu.step();

        // Cycle 4
        cpu.sr.set(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.sr.setFlag(4, false);
        cpu.setSP((byte)(cpu.getSP() + 1));
        cpu.step();

        // Cycle 5
        cpu.setPCL(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.setSP((byte)(cpu.getSP() + 1));
        cpu.step();

        // Cycle 6
        cpu.setPCH(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.end();
    }

    @Override
    public String toString() {
        return "RTI";
    }
}
