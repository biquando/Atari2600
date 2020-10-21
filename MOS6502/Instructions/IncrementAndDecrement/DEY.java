package Atari2600.MOS6502.Instructions.IncrementAndDecrement;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Decrement Y by one.
 */
public class DEY implements Instruction {
    /**
     * Y - 1 -> Y
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setY((byte)(cpu.getY() - 1));
        cpu.sr.N(cpu.getY());
        cpu.sr.Z(cpu.getY());
        cpu.end();
    }

    @Override
    public String toString() {
        return "DEY";
    }
}
