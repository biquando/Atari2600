package Atari2600.MOS6502.Instructions.Stack;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Pull accumulator from stack.
 */
public class PLA implements Instruction {
    /**
     * S -> A
     * Flags: N, Z
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
        cpu.setA(cpu.mem.read(cpu.getSP(), (byte)0x01));
        cpu.sr.N(cpu.getA());
        cpu.sr.Z(cpu.getA());
        cpu.end();
    }

    @Override
    public String toString() {
        return "PLA";
    }
}
