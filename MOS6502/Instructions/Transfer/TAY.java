package Atari2600.MOS6502.Instructions.Transfer;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Transfer accumulator to Y.
 */
public class TAY implements Instruction {
    /**
     * A -> Y
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setY(cpu.getA());
        cpu.sr.N(cpu.getY());
        cpu.sr.Z(cpu.getY());
        cpu.end();
    }

    @Override
    public String toString() {
        return "TAY";
    }
}
