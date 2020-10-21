package Atari2600.MOS6502.Instructions.Transfer;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Transfer accumulator to X.
 */
public class TAX implements Instruction {
    /**
     * A -> X
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setX(cpu.getA());
        cpu.sr.N(cpu.getX());
        cpu.sr.Z(cpu.getX());
        cpu.end();
    }

    @Override
    public String toString() {
        return "TAX";
    }
}
