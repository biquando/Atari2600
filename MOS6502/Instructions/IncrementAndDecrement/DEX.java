package Atari2600.MOS6502.Instructions.IncrementAndDecrement;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Decrement X by one.
 */
public class DEX implements Instruction {
    /**
     * X - 1 -> X
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setX((byte)(cpu.getX() - 1));
        cpu.sr.N(cpu.getX());
        cpu.sr.Z(cpu.getX());
        cpu.end();
    }

    @Override
    public String toString() {
        return "DEX";
    }
}
