package Atari2600.MOS6502.Instructions.Stack;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Pull processor status from stack.
 */
public class PLP implements Instruction {
    /**
     * P -> A
     * Flags: all
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
        cpu.end();
    }

    @Override
    public String toString() {
        return "PLP";
    }
}
