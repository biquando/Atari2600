package Atari2600.MOS6502.Instructions.Transfer;

import Atari2600.MOS6502.CPU;
import Atari2600.MOS6502.Instruction;

/**
 * Transfer stack pointer to X.
 */
public class TSX implements Instruction {
    /**
     * S -> X
     * flags: N, Z
     * @param cpu processor that executes the instruction
     * @param data empty byte[]
     */
    @Override
    public void execute(CPU cpu, byte[] data) {
        cpu.setX(cpu.getSP());
        cpu.sr.N(cpu.getX());
        cpu.sr.Z(cpu.getX());
        cpu.end();
    }

    @Override
    public String toString() {
        return "TSX";
    }
}
