package Atari2600.MOS6502.Instructions.Stack;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Push accumulator to stack.
 */
public class PHA implements Instruction {
    /**
     * A -> S
     * Flags: none
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.step();

        // Cycle 3
        cpu.mem.write(cpu.getA(), cpu.getSP(), (byte)0x01);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.end();
    }

    @Override
    public String toString() {
        return "PHA";
    }
}
