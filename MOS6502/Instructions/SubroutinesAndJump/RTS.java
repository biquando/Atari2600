package Atari2600.MOS6502.Instructions.SubroutinesAndJump;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * RTS
 *
 *  #  address R/W description
 * --- ------- --- -----------------------------------------------
 *  1    PC     R  fetch opcode, increment PC
 *  2    PC     R  read next instruction byte (and throw it away)
 *  3  $0100,S  R  increment S
 *  4  $0100,S  R  pull PCL from stack, increment S
 *  5  $0100,S  R  pull PCH from stack
 *  6    PC     R  increment PC
 */
public class RTS implements Instruction {
    /**
     * Return from subroutine.
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
        cpu.setPCL(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.setSP((byte)(cpu.getSP() + 1));
        cpu.step();

        // Cycle 5
        cpu.setPCH(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.step();

        // Cycle 6
        cpu.incrementPC();
        cpu.end();
    }

    @Override
    public String toString() {
        return "RTS";
    }
}
