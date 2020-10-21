package Atari2600.MOS6502.Instructions.Stack;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Push processor status to stack.
 */
public class PHP implements Instruction {
    /**
     * P -> S
     * Flags: none
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.step();

        // Cycle 3
        cpu.mem.write(cpu.sr.toByte(), cpu.getSP(), (byte)0x01);
        cpu.setSP((byte)(cpu.getSP() - 1));
        cpu.end();
    }

    @Override
    public String toString() {
        return "PHP";
    }
}
